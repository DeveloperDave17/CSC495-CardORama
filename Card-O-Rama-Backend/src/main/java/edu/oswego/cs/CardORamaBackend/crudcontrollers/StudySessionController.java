package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.oswego.cs.CardORamaBackend.model.StudySet;
import edu.oswego.cs.CardORamaBackend.model.flashcard.Flashcard;
import edu.oswego.cs.CardORamaBackend.model.flashcard.FlashcardRepository;
import edu.oswego.cs.CardORamaBackend.utils.DBUtils;
import edu.oswego.cs.CardORamaBackend.utils.TFIDF;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/StudySession")
public class StudySessionController {
   
   @Autowired
   FlashcardRepository flashcardRepository;

   @PostMapping("/create")
   public ResponseEntity<List<StudySet>> createStudySession(@AuthenticationPrincipal OAuth2User principal, @RequestBody List<Long> flashcardSetIDs) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         List<Flashcard> flashcards = new ArrayList<>();
         // get all of the flashcards
         for (Long flashcardSetID : flashcardSetIDs) {
            if(DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), flashcardSetID)) {
               flashcards.addAll(this.flashcardRepository.findBySetID(flashcardSetID));
            }
         }

         // shuffle the flashcards
         Collections.shuffle(flashcards);

         // generate the studysets
         List<StudySet> studySets = new ArrayList<>();

         // get all of the info for tf-idf
         HashMap<String,HashMap<String,Integer>> flashcardCountMap = new HashMap<>();
         for (Flashcard flashcard: flashcards) {
            flashcardCountMap.put(flashcard.getTerm(), TFIDF.generateTFCountMap(flashcard));
         }
         HashMap<String,HashMap<String,Double>> flashcardTFMap = new HashMap<>();
         for (Flashcard flashcard: flashcards) {
            flashcardTFMap.put(flashcard.getTerm(), TFIDF.generateTFMap(flashcardCountMap.get(flashcard.getTerm())));
         }
         HashMap<String,Double> idfMap = TFIDF.generateIDFMap(flashcardCountMap);

         // find the most similar terms.
         for (Flashcard flashcard : flashcards) {
            Flashcard connection1 = null;
            double idfScore1 = 0.0;
            Flashcard connection2 = null;
            double idfScore2 = 0.0;
            Flashcard connection3 = null;
            double idfScore3 = 0.0;

            for (Flashcard otherFlashcard : flashcards) {
               if (flashcard == otherFlashcard) continue;
               // generate tf-idf score for all words
               double idfScore = 0;
               Set<String> words = flashcardTFMap.get(otherFlashcard.getTerm()).keySet();
               HashMap<String,Double> tfMap = flashcardTFMap.get(flashcard.getTerm());
               for (String word : words) {
                  idfScore += tfMap.getOrDefault(word, 0.0) * idfMap.get(word);
               }
               if (idfScore > idfScore1) {
                  // move the other connections down
                  connection3 = connection2;
                  idfScore3 = idfScore2;
                  connection2 = connection1;
                  idfScore2 = idfScore1;

                  // add the new connection at the top
                  connection1 = otherFlashcard;
                  idfScore1 = idfScore;
               } else if (idfScore > idfScore2) {
                  // move the below connections down
                  connection3 = connection2;
                  idfScore3 = idfScore2;

                  // add the new connection
                  connection2 = otherFlashcard;
                  idfScore2 = idfScore;
               } else if (idfScore > idfScore3) {
                  connection3 = otherFlashcard;
                  idfScore3 = idfScore;
               }
            }
            studySets.add(new StudySet(flashcard, connection1, connection2, connection3));
         }
         return ResponseEntity.ok(studySets);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }
}
