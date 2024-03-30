package edu.oswego.cs.CardORamaBackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.oswego.cs.CardORamaBackend.model.flashcard.FlashcardRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FlashcardService {
   
   @Autowired
   FlashcardRepository flashcardRepository;

   @Transactional
   public void updatePositions(List<Long> flashcardIds) {
      for (int i = 0; i < flashcardIds.size(); i++) {
         flashcardRepository.updatePosition(flashcardIds.get(i), i);
      }
   }

}
