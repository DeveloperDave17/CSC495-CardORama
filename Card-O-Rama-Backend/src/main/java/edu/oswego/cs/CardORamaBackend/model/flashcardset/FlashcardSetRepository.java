package edu.oswego.cs.CardORamaBackend.model.flashcardset;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FlashcardSetRepository extends CrudRepository<FlashcardSet, Long> {
  
   List<FlashcardSet> findByEmailOrderByPriority(String email);
}
