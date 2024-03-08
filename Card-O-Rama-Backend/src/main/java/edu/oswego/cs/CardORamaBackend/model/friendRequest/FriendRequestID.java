package edu.oswego.cs.CardORamaBackend.model.friendRequest;

import java.io.Serializable;

public class FriendRequestID implements Serializable {
   
   private String email;
   private String friendEmail;

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getFriendEmail() {
      return friendEmail;
   }

   public void setFriendEmail(String friendEmail) {
      this.friendEmail = friendEmail;
   }
}
