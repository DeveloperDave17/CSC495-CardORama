package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.oswego.cs.CardORamaBackend.model.friend.FriendRepository;
import edu.oswego.cs.CardORamaBackend.model.friendRequest.FriendRequestRepository;
import edu.oswego.cs.CardORamaBackend.services.FriendService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
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

   @PostMapping("/acceptFriendRequest/{friendEmail}")
   public void acceptFriendRequest(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response, @PathVariable(name = "friendEmail") String friendEmail) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         String userEmail = principal.getAttribute("email");
         this.friendService.createFriendship(userEmail, friendEmail);
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
   }

}
