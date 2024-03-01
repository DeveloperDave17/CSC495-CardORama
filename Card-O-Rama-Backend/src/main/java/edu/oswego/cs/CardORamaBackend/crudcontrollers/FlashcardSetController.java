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

import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetRepository;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/FlashcardSet")
public class FlashcardSetController {
   
   @Autowired
   FlashcardSetRepository flashcardSetRepository;

   @GetMapping("/getAll")
   public ResponseEntity<List<FlashcardSet>> getFlashcardSets(@AuthenticationPrincipal OAuth2User principal) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String email = principal.getAttribute("email");
         List<FlashcardSet> flashcardSets = flashcardSetRepository.findByEmailOrderByPriority(email);
         return ResponseEntity.ok(flashcardSets);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }

   @PostMapping("/create")
   public void createFlashcardSet(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @RequestBody FlashcardSet flashcardSet) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String email = principal.getAttribute("email");
         flashcardSet.setEmail(email);
         flashcardSetRepository.save(flashcardSet);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @PostMapping("/remove/{setID}")
   public void removeFlashcardSet(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "setID") Long setID) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         flashcardSetRepository.deleteById(setID);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }
}
