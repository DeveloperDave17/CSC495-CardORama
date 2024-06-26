package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet.FavoritedFlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet.FavoritedFlashcardSetID;
import edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet.FavoritedFlashcardSetRepository;
import edu.oswego.cs.CardORamaBackend.model.flashcard.FlashcardRepository;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetPrivacy;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetRepository;
import edu.oswego.cs.CardORamaBackend.model.friend.Friend;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendID;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendRepository;
import edu.oswego.cs.CardORamaBackend.utils.DBUtils;
import edu.oswego.cs.CardORamaBackend.wrappers.FlashcardSetAndFavorited;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:26910")
@RestController
@RequestMapping("/FlashcardSet")
public class FlashcardSetController {
   
   @Autowired
   FlashcardSetRepository flashcardSetRepository;

   @Autowired
   FlashcardRepository flashcardRepository;

   @Autowired
   FriendRepository friendRepository;

   @Autowired
   FavoritedFlashcardSetRepository favoritedFlashcardSetRepository;

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
         this.flashcardRepository.deleteBySetID(setID);
         this.flashcardSetRepository.deleteById(setID);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   @PostMapping(value = {"/updateName/{setID}/{setName}", "/updateName/{setID}/"})
   public ResponseEntity<Boolean> updateFlashcardSetName(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID, @PathVariable(name = "setName") Optional<String> setName) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         String setNameUpdate = setName.isPresent() ? setName.get() : "Flashcard Set";
         this.flashcardSetRepository.updateSetName(setID, setNameUpdate);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   @PostMapping(value = {"/updatePrivacy/{setID}/{privacy}", "/updatePrivacy/{setID}/"})
   public ResponseEntity<Boolean> updateFlashcardSetPrivacy(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID, @PathVariable(name = "privacy") Optional<FlashcardSetPrivacy> flashcardSetPrivacy) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         FlashcardSetPrivacy flashcardSetPrivacyUpdate = flashcardSetPrivacy.isPresent() ? flashcardSetPrivacy.get() : FlashcardSetPrivacy.PRIVATE;
         this.flashcardSetRepository.updatePrivacy(setID, flashcardSetPrivacyUpdate);
         return ResponseEntity.ok(true);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
      }
   }

   @PostMapping(value = {"/updateColor/{setID}/{color}", "/updateColor/{setID}/"})
   public ResponseEntity<Boolean> updateFlashcardSetColor(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID, @PathVariable(name = "color") Optional<String> color) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated() && DBUtils.userHasWriteAccessForFlashcardSet(principal.getAttribute("email"), setID)) {
         String colorUpdate = color.isPresent() ? color.get() : "FFFFFF";
         this.flashcardSetRepository.updateColor(setID, colorUpdate);
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

   @GetMapping("/getAllSharedFlashcardSets/{targetUserEmail}")
   public ResponseEntity<List<FlashcardSetAndFavorited>> getSharedFlashcardSets(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "targetUserEmail") String targetUserEmail) {
      List<FlashcardSet> targetUserFlashcardSets = this.flashcardSetRepository.findByEmailOrderByPriority(targetUserEmail);
      List<FlashcardSet> sharedFlashcardSets = new ArrayList<>();
      String userEmail = principal.getAttribute("email");
      for (FlashcardSet flashcardSet : targetUserFlashcardSets) {
         if (flashcardSet.getPrivacy() == FlashcardSetPrivacy.PUBLIC) {
            sharedFlashcardSets.add(flashcardSet);
         } else if (flashcardSet.getPrivacy() == FlashcardSetPrivacy.FRIENDS) {
            FriendID friendID = new FriendID(userEmail, targetUserEmail);
            Optional<Friend> friendship = this.friendRepository.findById(friendID);
            if (friendship.isPresent()) {
               sharedFlashcardSets.add(flashcardSet);
            }
         }
      }

      List<FlashcardSetAndFavorited> flashcardSetsAndFavorites = new ArrayList<>();
      for (FlashcardSet flashcardSet : sharedFlashcardSets) {
         Optional<FavoritedFlashcardSet> optionalFavorited = this.favoritedFlashcardSetRepository.findById(new FavoritedFlashcardSetID(userEmail, flashcardSet.getSetID()));
         flashcardSetsAndFavorites.add(new FlashcardSetAndFavorited(flashcardSet, optionalFavorited.isPresent()));
      }

      return ResponseEntity.ok(flashcardSetsAndFavorites);
   }
}
