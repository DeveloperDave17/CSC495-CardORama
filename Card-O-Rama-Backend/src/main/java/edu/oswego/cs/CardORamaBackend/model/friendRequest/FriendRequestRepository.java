package edu.oswego.cs.CardORamaBackend.model.friendRequest;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, FriendRequestID> {
   
   @Modifying
   @Transactional
   @Query("update FriendRequest fr set fr.friendRequestStatus = DECLINED where fr.email = :senderEmail and fr.friendEmail = :email")
   void declineFriendRequest(@Param(value = "email") String email, @Param(value = "senderEmail") String senderEmail);

   @Query("select fr from FriendRequest fr where fr.friendEmail = :userEmail and fr.friendRequestStatus = PENDING order by fr.email")
   List<FriendRequest> getFriendRequestsList(@Param(value = "userEmail") String userEmail);
}
