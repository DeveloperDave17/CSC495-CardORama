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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.oswego.cs.CardORamaBackend.model.friend.Friend;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendRepository;
import edu.oswego.cs.CardORamaBackend.model.friendRequest.FriendRequest;
import edu.oswego.cs.CardORamaBackend.model.friendRequest.FriendRequestRepository;
import edu.oswego.cs.CardORamaBackend.services.FriendService;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:26910")
@RestController
@RequestMapping("/Friend")
public class FriendController {
   
   @Autowired
   FriendRepository friendRepository;
   @Autowired
   FriendRequestRepository friendRequestRepository;
   @Autowired
   FriendService friendService;

   @PostMapping("/sendFriendRequest/{friendEmail}")
   public void sendFriendRequest(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "friendEmail") String friendEmail) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String userEmail = principal.getAttribute("email");
         this.friendService.sendFriendRequest(userEmail, friendEmail);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @PostMapping("/acceptFriendRequest/{senderEmail}")
   public void acceptFriendRequest(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "senderEmail") String senderEmail) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String userEmail = principal.getAttribute("email");
         this.friendService.createFriendship(userEmail, senderEmail);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @PostMapping("/declineFriendRequest/{senderEmail}")
   public void declineFriendRequest(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "senderEmail") String senderEmail) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String userEmail = principal.getAttribute("email");
         this.friendRequestRepository.declineFriendRequest(userEmail, senderEmail);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @PostMapping("/removeFriendship/{friendEmail}")
   public void removeFriendship(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "friendEmail") String friendEmail) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String userEmail = principal.getAttribute("email");
         this.friendService.removeFriendship(userEmail, friendEmail);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

   @GetMapping("/getFriendsList")
   public ResponseEntity<List<Friend>> getFriendsList(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String userEmail = principal.getAttribute("email");
         List<Friend> friends = this.friendRepository.findByEmailOrderByFriendEmail(userEmail);
         return ResponseEntity.ok(friends);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }

   @GetMapping("/getFriendRequestsList")
   public ResponseEntity<List<FriendRequest>> getFriendRequestsList(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String userEmail = principal.getAttribute("email");
         List<FriendRequest> friendRequests = this.friendRequestRepository.getFriendRequestsList(userEmail);
         return ResponseEntity.ok(friendRequests);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
   }

}
