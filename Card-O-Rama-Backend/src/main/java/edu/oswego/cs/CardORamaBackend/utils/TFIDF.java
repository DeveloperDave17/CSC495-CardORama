package edu.oswego.cs.CardORamaBackend.utils;

import java.util.HashMap;
import java.util.Set;

import edu.oswego.cs.CardORamaBackend.model.flashcard.Flashcard;

public class TFIDF {
  
   
   public static HashMap<String,Integer> generateTFCountMap(Flashcard flashcard) {
      HashMap<String,Integer> tfIntMap = new HashMap<>();
      String wordString = flashcard.getDefinition().replaceAll("[,.]", "");
      String[] words = wordString.split(" ");
      for (String word : words) {
         tfIntMap.put(word, tfIntMap.getOrDefault(word, 0) + 1);
      }
      // filter out some words to heighten the associations
      filterTFMap(tfIntMap);

      return tfIntMap;
   }

   public static HashMap<String,Double> generateTFMap(HashMap<String,Integer> tfIntMap) {
      HashMap<String,Double> tfMap = new HashMap<>();
      Set<String> wordSet = tfIntMap.keySet();
      int wordCount = 0;
      for (String word : wordSet) {
         wordCount++;
      }

      for (String word : wordSet) {
         tfMap.put(word, (double)tfIntMap.get(word) / wordCount);
      }

      return tfMap;
   }

   private static void filterTFMap(HashMap<String,Integer> tfMap) {
      tfMap.remove("the");
      tfMap.remove("a");
      tfMap.remove("is");
      tfMap.remove("of");
      tfMap.remove("and");
      tfMap.remove("or");
   }

   public static HashMap<String,Double> generateIDFMap(HashMap<String,HashMap<String,Integer>> flashcardTFMap) {
      HashMap<String,Integer> dfMap = new HashMap<>();
      Set<String> flashcards = flashcardTFMap.keySet();
      for (String flashcard : flashcards) {
         Set<String> words = flashcardTFMap.get(flashcard).keySet();
         for (String word : words) {
            dfMap.put(word, dfMap.getOrDefault(word, 0) + 1);
         }
      }
      
      HashMap<String,Double> idfMap = new HashMap<>();
      Set<String> uniqueWords = dfMap.keySet();
      int numFlashcards = flashcards.size();
      for (String uniqueWord : uniqueWords) {
         idfMap.put(uniqueWord, Math.log((double) numFlashcards / dfMap.get(uniqueWord)));
      }

      return idfMap;
   }
}
