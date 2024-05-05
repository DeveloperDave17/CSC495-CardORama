package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.oswego.cs.CardORamaBackend.model.flashcard.Flashcard;
import edu.oswego.cs.CardORamaBackend.model.flashcard.FlashcardRepository;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetPrivacy;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetRepository;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendID;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendRepository;
import edu.oswego.cs.CardORamaBackend.services.FlashcardService;
import edu.oswego.cs.CardORamaBackend.utils.DBUtils;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:26910")
@RestController
@RequestMapping("/Flashcard")
public class FlashcardController {
   
   @Autowired
   FlashcardRepository flashcardRepository;

   @Autowired
   FlashcardSetRepository flashcardSetRepository;

   @Autowired
   FlashcardService flashcardService;

   @Autowired
   FriendRepository friendRepository;

   /**
    * Retrieves all of the users flashcards for a provided set id.
    * @param principal The oauth token associated with the user.
    * @param setID The set id of the flashcards.
    * @return A list of flashcards relative to the given set id.
    */
   @GetMapping("/getAll/{setID}")
   public ResponseEntity<List<Flashcard>> getFlashcards(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") long setID) {
      Optional<FlashcardSet> flashcardSetOptional = this.flashcardSetRepository.findById(setID);
      List<Flashcard> flashcards = new ArrayList<>();
      String userEmail = principal.getAttribute("email");
      if (flashcardSetOptional.isPresent()) {
         FlashcardSet flashcardSet = flashcardSetOptional.get();
         if (DBUtils.userHasWriteAccessForFlashcardSet(userEmail, setID) 
         || flashcardSet.getPrivacy() == FlashcardSetPrivacy.PUBLIC 
         || (flashcardSet.getPrivacy() == FlashcardSetPrivacy.FRIENDS 
            && this.friendRepository.findById(new FriendID(userEmail, flashcardSet.getEmail())).isPresent())) {
            flashcards = this.flashcardRepository.findBySetIDOrderByPosition(setID);
         }  
      } 
      return ResponseEntity.ok(flashcards);
   }

   /**
    * Creates a flashcard dependent upon the user's write permissions for the specified set.
    * @param principal The oauth token associated with the user.
    * @param flashcard The flashcard to be saved.
    * @return A copy of the flashcard upon a successful save or null if unsuccessful.
    */
   @PostMapping("/create")
   public ResponseEntity<Flashcard> createFlashcard(@AuthenticationPrincipal OAuth2User principal, @RequestBody Flashcard flashcard) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), flashcard.getSetID())) {
         flashcard = this.flashcardRepository.save(flashcard);
         return ResponseEntity.ok(flashcard);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }

   /**
    * Removes a flashcard from the repository.
    * @param principal The oauth token associated with the user.
    * @param flashcardID The flashcard id associated with the flashcard to be removed.
    * @return A boolean indicating deletion status.
    */
   @PostMapping("/remove/{flashcardID}")
   public ResponseEntity<Boolean> removeFlashcard(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "flashcardID") Long flashcardID) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && this.flashcardRepository.checkIfUserOwnsFlashcard(flashcardID, principal.getAttribute("email"))) {
         this.flashcardRepository.deleteById(flashcardID);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   /**
    * Updates a flashcard's term value.
    * @param principal The oauth token associated with the user.
    * @param flashcardID The flashcard id associated with the term.
    * @param term The term to be used.
    * @return A boolean representing the update status.
    */
   @PostMapping(value = {"/updateTerm/{flashcardID}/{term}", "/updateTerm/{flashcardID}/"})
   public ResponseEntity<Boolean> updateFlashcardTerm(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "flashcardID") Long flashcardID, @PathVariable(name = "term") Optional<String> term) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && this.flashcardRepository.checkIfUserOwnsFlashcard(flashcardID, principal.getAttribute("email"))) {
         String termUpdate = term.isPresent() ? term.get() : "";
         this.flashcardRepository.updateTerm(flashcardID, termUpdate);
         return ResponseEntity.ok().body(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   /**
    * Updates a flashcard's definition.
    * @param principal The oauth token associated with the user.
    * @param flashcardID The flashcard id associated with the flashcard to be updated.
    * @param definition The new defintion for the flashcard.
    * @return A boolean representing whether the update was successful or not.
    */
   @PostMapping(value = {"/updateDefinition/{flashcardID}/{definition}", "/updateDefinition/{flashcardID}/"})
   public ResponseEntity<Boolean> updateFlashcardDefintion(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "flashcardID") Long flashcardID, @PathVariable(name = "definition") Optional<String> definition) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && this.flashcardRepository.checkIfUserOwnsFlashcard(flashcardID, principal.getAttribute("email"))) {
         String definitionUpdate = definition.isPresent() ? definition.get() : "";
         this.flashcardRepository.updateDefinition(flashcardID, definitionUpdate);
         return ResponseEntity.ok().body(true); 
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   /**
    * A bulk update function for flashcard positions in a set.
    * @param principal The oauth token associated with the user.
    * @param flashcardIDs A List of flashcard ids in position order.
    * @return
    */
   @PostMapping("/updatePositions")
   public ResponseEntity<Boolean> updateFlashcardPosition(@AuthenticationPrincipal OAuth2User principal, @RequestBody List<Long> flashcardIDs) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && !flashcardIDs.isEmpty() && this.flashcardRepository.checkIfUserOwnsFlashcard(flashcardIDs.get(0), principal.getAttribute("email"))) {
         this.flashcardService.updatePositions(flashcardIDs);
         return ResponseEntity.ok().body(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }
}
