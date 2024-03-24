package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import java.util.List;

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

import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetPrivacy;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetRepository;
import edu.oswego.cs.CardORamaBackend.utils.DBUtils;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/FlashcardSet")
public class FlashcardSetController {
   
   @Autowired
   FlashcardSetRepository flashcardSetRepository;

   @GetMapping("/getAll")
   public ResponseEntity<List<FlashcardSet>> getFlashcardSets(@AuthenticationPrincipal OAuth2User principal) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String email = principal.getAttribute("email");
         List<FlashcardSet> flashcardSets = this.flashcardSetRepository.findByEmailOrderByPriority(email);
         return ResponseEntity.ok(flashcardSets);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }

   @PostMapping("/create")
   public ResponseEntity<FlashcardSet> createFlashcardSet(@AuthenticationPrincipal OAuth2User principal, @RequestBody FlashcardSet flashcardSet) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String email = principal.getAttribute("email");
         flashcardSet.setEmail(email);
         flashcardSet = this.flashcardSetRepository.save(flashcardSet);
         return ResponseEntity.ok(flashcardSet);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }

   @PostMapping("/remove/{setID}")
   public ResponseEntity<Boolean> removeFlashcardSet(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         this.flashcardSetRepository.deleteById(setID);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   @PostMapping("/updateName/{setID}/{setName}")
   public ResponseEntity<Boolean> updateFlashcardSetName(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID, @PathVariable(name = "setName") String setName) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         this.flashcardSetRepository.updateSetName(setID, setName);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   @PostMapping("/updatePrivacy/{setID}/{privacy}")
   public ResponseEntity<Boolean> updateFlashcardSetPrivacy(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID, @PathVariable(name = "privacy") FlashcardSetPrivacy flashcardSetPrivacy) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         this.flashcardSetRepository.updatePrivacy(setID, flashcardSetPrivacy);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   @PostMapping("/updateColor/{setID}/{color}")
   public ResponseEntity<Boolean> updateFlashcardSetColor(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID, @PathVariable(name = "color") String color) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         this.flashcardSetRepository.updateColor(setID, color);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   @PostMapping("/updatePriority/{setID}/{priority}")
   public ResponseEntity<Boolean> updateFlashcardSetPriority(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID, @PathVariable(name = "priority") int priority) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         this.flashcardSetRepository.updatePriority(setID, priority);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }
}
