package edu.oswego.cs.CardORamaBackend.model.flashcardset;

public enum FlashcardSetPrivacy {
   PRIVATE, FRIENDS, PUBLIC;

   @Override
   public String toString() {
      return this.name();
   }
}
