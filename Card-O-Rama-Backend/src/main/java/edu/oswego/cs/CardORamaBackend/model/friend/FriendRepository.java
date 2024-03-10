package edu.oswego.cs.CardORamaBackend.model.friend;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FriendRepository extends CrudRepository<Friend, FriendID> {
   
   List<Friend> findByEmailOrderByFriendEmail(String email);
}
