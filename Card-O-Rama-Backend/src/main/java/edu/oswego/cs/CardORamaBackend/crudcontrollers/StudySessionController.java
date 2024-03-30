package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/StudySession")
public class StudySessionController {
   
   @Autowired
   FlashcardRepository flashcardRepository;

   @PostMapping("/create")
   public ResponseEntity<Flashcard> createStudySession(@AuthenticationPrincipal OAuth2User principal, @RequestBody List<Long> flashcardSetIDs) {
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

         return ResponseEntity.ok(null);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }
}
