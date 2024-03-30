package edu.oswego.cs.CardORamaBackend.model.flashcardset;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface FlashcardSetRepository extends CrudRepository<FlashcardSet, Long> {
  
   List<FlashcardSet> findByEmailOrderByPriority(String email);

   FlashcardSet findByEmailAndSetID(String email, Long setID);

   @Modifying
   @Transactional
   @Query("update FlashcardSet f set f.setName = :setName where f.setID = :setID")
   void updateSetName(@Param(value = "setID") Long setID, @Param(value = "setName") String setName);

   @Modifying
   @Transactional
   @Query("update FlashcardSet f set f.privacy = :privacy where f.setID = :setID")
   void updatePrivacy(@Param(value = "setID") Long setID, @Param(value = "privacy") FlashcardSetPrivacy flashcardSetPrivacy);

   @Modifying
   @Transactional
   @Query("update FlashcardSet f set f.color = :color where f.setID = :setID")
   void updateColor(@Param(value = "setID") Long setID, @Param(value = "color") String color);

   @Modifying
   @Transactional
   @Query("update FlashcardSet f set f.priority = :priority where f.setID = :setID")
   void updatePriority(@Param(value = "setID") Long setID, @Param(value = "priority") int priority);
}
