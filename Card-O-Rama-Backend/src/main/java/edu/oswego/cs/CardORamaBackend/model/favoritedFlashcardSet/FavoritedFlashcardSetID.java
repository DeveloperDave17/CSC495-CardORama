package edu.oswego.cs.CardORamaBackend.model.favoritedFlashcardSet;

import java.io.Serializable;

public class FavoritedFlashcardSetID implements Serializable {
   private String email;
   private long setID;

   public FavoritedFlashcardSetID() {}

   public FavoritedFlashcardSetID(String email, long setID) {
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
