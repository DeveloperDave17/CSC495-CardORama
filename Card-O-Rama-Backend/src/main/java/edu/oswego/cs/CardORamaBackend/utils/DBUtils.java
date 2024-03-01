package edu.oswego.cs.CardORamaBackend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetRepository;

@Component
public class DBUtils {
   
   private static FlashcardSetRepository flashcardSetRepository;

   @Autowired
   public DBUtils(FlashcardSetRepository flashcardSetRepository) {
      DBUtils.flashcardSetRepository = flashcardSetRepository;
   }

   public static boolean userHasWriteAccessForFlashcardSet(String email, Long setID) {
      FlashcardSet ownerSet = DBUtils.flashcardSetRepository.findByEmailAndSetID(email, setID);
      return ownerSet != null;
   }

}
