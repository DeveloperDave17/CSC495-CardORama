package edu.oswego.cs.CardORamaBackend.model;

import java.util.List;

import edu.oswego.cs.CardORamaBackend.model.flashcard.Flashcard;

public class StudySet {

   private Flashcard flashcard;
   private List<Flashcard> connections;
   private List<String> flashcardColors;

   public StudySet() {}

   public StudySet(Flashcard flashcard, List<Flashcard> connections, List<String> flashcardColors) {
      this.flashcard = flashcard;
      this.connections = connections;
      this.flashcardColors = flashcardColors;
   }

   public Flashcard getFlashcard() {
      return flashcard;
   }

   public void setFlashcard(Flashcard flashcard) {
      this.flashcard = flashcard;
   }

   public List<Flashcard> getConnections() {
      return connections;
   }

   public void setConnections(List<Flashcard> connections) {
      this.connections = connections;
   }

   public List<String> getFlashcardColors() {
      return flashcardColors;
   }

   public void setFlashcardColors(List<String> flashcardColors) {
      this.flashcardColors = flashcardColors;
   }
}
