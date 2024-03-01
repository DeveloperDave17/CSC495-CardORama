package edu.oswego.cs.CardORamaBackend.model.flashcard;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Flashcards")
public class Flashcard {
   
   @Id @GeneratedValue
   private Long flashcardID;
   private Long setID;
   private String term;
   private String definition;
   private int position;

   public Flashcard() {}

   public Flashcard(Long setID, String term, String definition, int position) {
      this.setID = setID;
      this.term = term;
      this.definition = definition;
      this.position = position;
   }

   public Long getFlashcardID() {
      return flashcardID;
   }

   public void setFlashcardID(Long flashcardID) {
      this.flashcardID = flashcardID;
   }

   public Long getSetID() {
      return setID;
   }

   public void setSetID(Long setID) {
      this.setID = setID;
   }

   public String getTerm() {
      return term;
   }

   public void setTerm(String term) {
      this.term = term;
   }

   public String getDefinition() {
      return definition;
   }

   public void setDefinition(String definition) {
      this.definition = definition;
   }

   public int getPosition() {
      return position;
   }

   public void setPosition(int position) {
      this.position = position;
   }
}
