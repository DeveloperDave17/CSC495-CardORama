package edu.oswego.cs.CardORamaBackend.model.friendRequest;

import java.io.Serializable;

public class FriendRequestID implements Serializable {
   
   private String email;
   private String friendEmail;

   public FriendRequestID() {}

   public FriendRequestID(String email, String friendEmail) {
      this.email = email;
      this.friendEmail = friendEmail;
   }

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
