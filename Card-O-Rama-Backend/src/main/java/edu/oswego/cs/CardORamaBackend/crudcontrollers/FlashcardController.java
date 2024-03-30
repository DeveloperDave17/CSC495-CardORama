package edu.oswego.cs.CardORamaBackend.crudcontrollers;

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
import edu.oswego.cs.CardORamaBackend.services.FlashcardService;
import edu.oswego.cs.CardORamaBackend.utils.DBUtils;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/Flashcard")
public class FlashcardController {
   
   @Autowired
   FlashcardRepository flashcardRepository;

   @Autowired
   FlashcardService flashcardService;

   @GetMapping("/getAll/{setID}")
   public ResponseEntity<List<Flashcard>> getFlashcard(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") long setID) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         List<Flashcard> flashcards = this.flashcardRepository.findBySetIDOrderByPosition(setID);
         return ResponseEntity.ok(flashcards);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }

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
