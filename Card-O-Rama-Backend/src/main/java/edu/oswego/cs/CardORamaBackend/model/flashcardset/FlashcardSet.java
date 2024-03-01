package edu.oswego.cs.CardORamaBackend.model.flashcardset;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FlashcardSets")
public class FlashcardSet {
   
   @Id @GeneratedValue
   private Long setID;
   private String email;
   private String setName;
   @Enumerated(EnumType.STRING)
   private FlashcardSetPrivacy privacy;
   private String color;
   private int priority;

   public FlashcardSet() {}

   public FlashcardSet(String email, String setName, FlashcardSetPrivacy privacy, String color, int priority) {
      this.email = email;
      this.setName = setName;
      this.privacy = privacy;
      this.color = color;
      this.priority = priority;
   }

   public Long getSetID() {
      return setID;
   }

   public void setSetID(Long setID) {
      this.setID = setID;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getSetName() {
      return setName;
   }

   public void setSetName(String setName) {
      this.setName = setName;
   }

   public FlashcardSetPrivacy getPrivacy() {
      return privacy;
   }

   public void setPrivacy(FlashcardSetPrivacy privacy) {
      this.privacy = privacy;
   }

   public String getColor() {
      return color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public int getPriority() {
      return priority;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }
}
