package edu.oswego.cs.CardORamaBackend.model.friendRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
   @Enumerated(EnumType.STRING)
   private FriendRequestStatus friendRequestStatus;
   private int requestCount;
   
   public FriendRequest() {}

   public FriendRequest(String email, String friendEmail) {
      this.email = email;
      this.friendEmail = friendEmail;
      this.friendRequestStatus = FriendRequestStatus.PENDING;
      this.requestCount = 1;
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

   public FriendRequestStatus getFriendRequestStatus() {
      return friendRequestStatus;
   }

   public void setFriendRequestStatus(FriendRequestStatus friendRequestStatus) {
      this.friendRequestStatus = friendRequestStatus;
   }

   public int getRequestCount() {
      return requestCount;
   }

   public void setRequestCount(int requestCount) {
      this.requestCount = requestCount;
   }
}

