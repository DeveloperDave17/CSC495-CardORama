package edu.oswego.cs.CardORamaBackend.model;

import edu.oswego.cs.CardORamaBackend.model.flashcard.Flashcard;

public class StudySet {

   private Flashcard flashcard;
   private Flashcard connection1;
   private Flashcard connection2;
   private Flashcard connection3;

   public StudySet() {}

   public StudySet(Flashcard flashcard, Flashcard connection1, Flashcard connection2, Flashcard connection3) {
      this.flashcard = flashcard;
      this.connection1 = connection1;
      this.connection2 = connection2;
      this.connection3 = connection3;
   }

   public Flashcard getFlashcard() {
      return flashcard;
   }

   public void setFlashcard(Flashcard flashcard) {
      this.flashcard = flashcard;
   }

   public Flashcard getConnection1() {
      return connection1;
   }

   public void setConnection1(Flashcard connection1) {
      this.connection1 = connection1;
   }
   
   public Flashcard getConnection2() {
      return connection2;
   }

   public void setConnection2(Flashcard connection2) {
      this.connection2 = connection2;
   }

   public Flashcard getConnection3() {
      return connection3;
   }

   public void setConnection3(Flashcard connection3) {
      this.connection3 = connection3;
   }
}
