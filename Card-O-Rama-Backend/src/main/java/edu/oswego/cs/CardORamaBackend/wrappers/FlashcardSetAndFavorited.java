package edu.oswego.cs.CardORamaBackend.wrappers;

import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSet;

public class FlashcardSetAndFavorited {

  FlashcardSet flashcardSet;
  boolean favorited;
  
  public FlashcardSetAndFavorited() {}

  public FlashcardSetAndFavorited(FlashcardSet flashcardSet, boolean favorited) {
    this.flashcardSet = flashcardSet;
    this.favorited = favorited;
  }

  public void setFlashcardSet(FlashcardSet flashcardSet) {
    this.flashcardSet = flashcardSet;
  }

  public void setFavorited(boolean favorited) {
    this.favorited = favorited;
  }

  public FlashcardSet getFlashcardSet() {
    return flashcardSet;
  }

  public boolean getFavorited() {
    return favorited;
  }
}
