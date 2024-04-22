package edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FavoritedFlashcardSetRepository extends CrudRepository<FavoritedFlashcardSet, FavoritedFlashcardSetID>  {
   List<FavoritedFlashcardSet> findByEmail(String email);
}
