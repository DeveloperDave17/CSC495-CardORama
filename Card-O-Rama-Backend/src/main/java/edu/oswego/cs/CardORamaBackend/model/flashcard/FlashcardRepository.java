package edu.oswego.cs.CardORamaBackend.model.flashcard;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FlashcardRepository extends CrudRepository<Flashcard, Long> {

   List<Flashcard> findBySetIDOrderByPosition(long setID);

   @Query("select COUNT(*) > 0 from Flashcard as f inner join FlashcardSet as fs on f.setID = fs.setID where f.flashcardID = :flashcardID and fs.email = :email")
   boolean checkIfUserOwnsFlashcard(@Param(value = "flashcardID") Long flashcardID, @Param(value = "email") String email);

   @Modifying
   @Query("update Flashcard f set f.term = :term where f.flashcardID = :flashcardID")
   void updateTerm(@Param(value = "flashcardID") Long flashcardID, @Param(value = "term") String term);

   @Modifying
   @Query("update Flashcard f set f.definition = :definition where f.flashcardID = :flashcardID")
   void updateDefinition(@Param(value = "flashcardID") Long flashcardID, @Param(value = "definition") String definition);

   @Modifying
   @Query("update Flashcard f set f.position = :position where f.flashcardID = :flashcardID")
   void updatePosition(@Param(value = "flashcardID") Long flashcardID, @Param(value = "position") int position);
}
