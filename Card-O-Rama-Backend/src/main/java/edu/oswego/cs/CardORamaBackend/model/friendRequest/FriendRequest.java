package edu.oswego.cs.CardORamaBackend.model.friendRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "FriendRequests")
@IdClass(FriendRequestID.class)
public class FriendRequest {

   @Id
   private String email;
   @Id
   private String friendEmail;
   
   public FriendRequest() {}

   public FriendRequest(String email, String friendEmail) {
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

