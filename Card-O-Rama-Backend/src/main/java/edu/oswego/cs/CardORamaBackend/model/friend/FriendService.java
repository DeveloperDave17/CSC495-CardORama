package edu.oswego.cs.CardORamaBackend.model.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
