package edu.oswego.cs.CardORamaBackend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.oswego.cs.CardORamaBackend.model.friend.Friend;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendID;
import edu.oswego.cs.CardORamaBackend.model.friend.FriendRepository;

@Service
@Transactional
public class FriendService {
   
   @Autowired
   FriendRepository friendRepository;

   @Transactional
   public void createFriendship(String email1, String email2) {
      Friend friend1 = new Friend(email1, email2);
      this.friendRepository.save(friend1);
      Friend friend2 = new Friend(email2, email1);
      this.friendRepository.save(friend2);
   }

   @Transactional
   public void removeFriendship(String email1, String email2) {
      FriendID friendID1 = new FriendID(email1, email2);
      this.friendRepository.deleteById(friendID1);
      FriendID friendID2 = new FriendID(email2, email1);
      this.friendRepository.deleteById(friendID2);
   }

}