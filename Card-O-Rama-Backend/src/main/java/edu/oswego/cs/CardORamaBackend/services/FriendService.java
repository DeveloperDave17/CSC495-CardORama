package edu.oswego.cs.CardORamaBackend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.oswego.cs.CardORamaBackend.model.friend.Friend;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendID;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendRepository;
import edu.oswego.cs.CardORamaBackend.model.friendRequest.FriendRequest;
import edu.oswego.cs.CardORamaBackend.model.friendRequest.FriendRequestID;
import edu.oswego.cs.CardORamaBackend.model.friendRequest.FriendRequestRepository;
import edu.oswego.cs.CardORamaBackend.model.friendRequest.FriendRequestStatus;

@Service
@Transactional
public class FriendService {
   
   @Autowired
   FriendRepository friendRepository;

   @Autowired
   FriendRequestRepository friendRequestRepository;

   @Transactional
   public void createFriendship(String email1, String email2) {
      // Add users to each others friend requests.
      Friend friend1 = new Friend(email1, email2);
      this.friendRepository.save(friend1);
      Friend friend2 = new Friend(email2, email1);
      this.friendRepository.save(friend2);
      // Remove pending friend requests for both users.
      FriendRequestID possibleFriendRequest1 = new FriendRequestID(email1, email2);
      this.friendRequestRepository.deleteById(possibleFriendRequest1);
      FriendRequestID possibleFriendRequest2 = new FriendRequestID(email2, email1);
      this.friendRequestRepository.deleteById(possibleFriendRequest2);
   }

   @Transactional
   public void removeFriendship(String email1, String email2) {
      FriendID friendID1 = new FriendID(email1, email2);
      this.friendRepository.deleteById(friendID1);
      FriendID friendID2 = new FriendID(email2, email1);
      this.friendRepository.deleteById(friendID2);
   }

   @Transactional
   public void sendFriendRequest(String userEmail, String friendReqEmail) {
      Optional<Friend> friend = this.friendRepository.findById(new FriendID(userEmail, friendReqEmail));
      // if the friendship doesn't exist
      if (!friend.isPresent()) {
         Optional<FriendRequest> friendRequestOptional = this.friendRequestRepository.findById(new FriendRequestID(userEmail, friendReqEmail));
         if (friendRequestOptional.isPresent()) {
            FriendRequest friendRequest = friendRequestOptional.get();
            int friendRequestsSent = friendRequest.getRequestCount();
            if (friendRequest.getFriendRequestStatus() == FriendRequestStatus.DECLINED && friendRequestsSent < 5) {
               friendRequest.setFriendRequestStatus(FriendRequestStatus.PENDING);
               friendRequest.setRequestCount(friendRequestsSent + 1);
               this.friendRequestRepository.save(friendRequest);
            }
         } else {
            FriendRequest friendRequest = new FriendRequest(userEmail, friendReqEmail);
            this.friendRequestRepository.save(friendRequest);
         }
      }
   }

}
