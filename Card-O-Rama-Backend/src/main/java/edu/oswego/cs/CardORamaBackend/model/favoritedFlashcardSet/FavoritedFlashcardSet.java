package edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "FavoritedFlashcardSets")
@IdClass(FavoritedFlashcardSetID.class)
public class FavoritedFlashcardSet {
   
   @Id
   private String email;
   @Id
   private long setID;

   public FavoritedFlashcardSet() {}

   public FavoritedFlashcardSet(String email, long setID) {
      this.email = email;
      this.setID = setID;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getEmail() {
      return email;
   }

   public void setSetID(long setID) {
      this.setID = setID;
   }

   public long getSetID() {
      return setID;
   }
}
