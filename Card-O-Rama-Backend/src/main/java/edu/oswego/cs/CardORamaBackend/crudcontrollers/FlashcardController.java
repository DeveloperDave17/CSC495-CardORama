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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.oswego.cs.CardORamaBackend.model.flashcard.Flashcard;
import edu.oswego.cs.CardORamaBackend.model.flashcard.FlashcardRepository;
import edu.oswego.cs.CardORamaBackend.utils.DBUtils;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Flashcard")
public class FlashcardController {
   
   @Autowired
   FlashcardRepository flashcardRepository;

   @GetMapping("/getAll/{setID}")
   public ResponseEntity<List<Flashcard>> getFlashcardSets(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") long setID) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         List<Flashcard> flashcards = this.flashcardRepository.findBySetIDOrderByPosition(setID);
         return ResponseEntity.ok(flashcards);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }

   @PostMapping("/create")
   public void createFlashcardSet(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @RequestBody Flashcard flashcard) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), flashcard.getSetID())) {
         this.flashcardRepository.save(flashcard);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @PostMapping("/remove/{flashcardID}")
   public void removeFlashcardSet(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "flashcardID") Long flashcardID) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && this.flashcardRepository.checkIfUserOwnsFlashcard(flashcardID, principal.getAttribute("email"))) {
         this.flashcardRepository.deleteById(flashcardID);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @PostMapping("/updateTerm/{flashcardID}/{term}")
   public void updateFlashcardTerm(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "flashcardID") Long flashcardID, @PathVariable(name = "term") String term) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && this.flashcardRepository.checkIfUserOwnsFlashcard(flashcardID, principal.getAttribute("email"))) {
         this.flashcardRepository.updateTerm(flashcardID, term);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @PostMapping("/updateDefinition/{flashcardID}/{definition}")
   public void updateFlashcardDefintion(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "flashcardID") Long flashcardID, @PathVariable(name = "definition") String definition) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && this.flashcardRepository.checkIfUserOwnsFlashcard(flashcardID, principal.getAttribute("email"))) {
         this.flashcardRepository.updateDefinition(flashcardID, definition);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @PostMapping("/updatePosition/{flashcardID}/{position}")
   public void updateFlashcardPosition(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "flashcardID") Long flashcardID, @PathVariable(name = "position") int position) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && this.flashcardRepository.checkIfUserOwnsFlashcard(flashcardID, principal.getAttribute("email"))) {
         this.flashcardRepository.updatePosition(flashcardID, position);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }
}
