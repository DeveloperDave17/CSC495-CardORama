package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet.FavoritedFlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet.FavoritedFlashcardSetID;
import edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet.FavoritedFlashcardSetRepository;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetPrivacy;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetRepository;
import edu.oswego.cs.CardORamaBackend.model.friend.Friend;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendID;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendRepository;

@CrossOrigin(origins = "http://localhost:26910")
@RestController
@RequestMapping("/Favorite")
public class FavoritedController {
   
   @Autowired
   FlashcardSetRepository flashcardSetRepository;

   @Autowired
   FriendRepository friendRepository;

   @Autowired
   FavoritedFlashcardSetRepository favoritedFlashcardSetRepository;

   @PostMapping("/add/{setID}")
   public ResponseEntity<Long> add(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID) {
      String userEmail = principal.getAttribute("email");
      Optional<FlashcardSet> optionalFlashcardSet = this.flashcardSetRepository.findById(setID);
      if (optionalFlashcardSet.isPresent()) {
         FlashcardSet flashcardSet = optionalFlashcardSet.get();
         FriendID friendID = new FriendID(userEmail, flashcardSet.getEmail());
         Optional<Friend> friendship = this.friendRepository.findById(friendID);
         if (flashcardSet.getPrivacy() == FlashcardSetPrivacy.PUBLIC || (flashcardSet.getPrivacy() == FlashcardSetPrivacy.FRIENDS && friendship.isPresent())) {
            Optional<FavoritedFlashcardSet> favoritedFlashcardSet = this.favoritedFlashcardSetRepository.findById(new FavoritedFlashcardSetID(userEmail, setID));
            if (!favoritedFlashcardSet.isPresent()) this.favoritedFlashcardSetRepository.save(new FavoritedFlashcardSet(userEmail, setID));
         }
      }
      return ResponseEntity.ok(setID);
   }

   @PostMapping("/remove/{setID}")
   public ResponseEntity<Long> remove(@AuthenticationPrincipal OAuth2User principal, @PathVariable(name = "setID") Long setID) {
      String userEmail = principal.getAttribute("email");
      this.favoritedFlashcardSetRepository.deleteById(new FavoritedFlashcardSetID(userEmail, setID));
      return ResponseEntity.ok(setID);
   }

   @GetMapping("/getAll")
   public ResponseEntity<List<FlashcardSet>> getAllFavorites(@AuthenticationPrincipal OAuth2User principal) {
      List<FavoritedFlashcardSet> favoritedFlashcardSets = this.favoritedFlashcardSetRepository.findByEmail(principal.getAttribute("email"));
      List<FlashcardSet> flashcardSets = new ArrayList<>();
      for (FavoritedFlashcardSet favoritedFlashcardSet : favoritedFlashcardSets) {
         Optional<FlashcardSet> flashcardSet = this.flashcardSetRepository.findById(favoritedFlashcardSet.getSetID());
         if (flashcardSet.isPresent()) {
            flashcardSets.add(flashcardSet.get());
         }
      }
      return ResponseEntity.ok(flashcardSets);
   }
}
