package edu.oswego.cs.CardORamaBackend.model.friendRequest;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, FriendRequestID> {
   
   @Modifying
   @Query("update FriendRequest fr set fr.friendRequestStatus = DECLINED where fr.email = :senderEmail and fr.friendEmail = :email")
   void declineFriendRequest(@Param(value = "email") String email, @Param(value = "senderEmail") String senderEmail);
}
