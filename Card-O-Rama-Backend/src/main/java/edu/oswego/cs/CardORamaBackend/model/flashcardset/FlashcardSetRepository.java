package edu.oswego.cs.CardORamaBackend.model.flashcardset;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FlashcardSetRepository extends CrudRepository<FlashcardSet, Long> {
  
   List<FlashcardSet> findByEmailOrderByPriority(String email);

   FlashcardSet findByEmailAndSetID(String email, Long setID);

   @Modifying
   @Query("update FlashcardSets f set f.setName = :setName where f.setID = :setID")
   void updateSetName(@Param(value = "setID") Long setID, @Param(value = "setName") String setName);

   @Modifying
   @Query("update FlashcardSets f set f.privacy = :privacy where f.setID = :setID")
   void updateSetPrivacy(@Param(value = "setID") Long setID, @Param(value = "privacy") FlashcardSetPrivacy flashcardSetPrivacy);

   @Modifying
   @Query("update FlashcardSets f set f.color = :color where f.setID = :setID")
   void updateSetColor(@Param(value = "setID") Long setID, @Param(value = "color") String color);

   @Modifying
   @Query("update FlashcardSets f set f.priority = :priority where f.setID = :setID")
   void updateSetPriority(@Param(value = "setID") Long setID, @Param(value = "priority") int priority);
}
