package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.docs.v1.model.ParagraphElement;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.google.api.services.docs.v1.model.TextRun;

import edu.oswego.cs.CardORamaBackend.model.flashcard.Flashcard;
import edu.oswego.cs.CardORamaBackend.model.flashcard.FlashcardRepository;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSet;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetPrivacy;
import edu.oswego.cs.CardORamaBackend.model.flashcardset.FlashcardSetRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/GoogleDrive")
public class GoogleDriveController {
   
   @Autowired
   FlashcardSetRepository flashcardSetRepository;

   @Autowired
   FlashcardRepository flashcardRepository;

   private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

   @PostMapping("/loadFlashcardSet/{docID}")
   public void loadFlashcardSet(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client, @PathVariable(name = "docID") String docID, @AuthenticationPrincipal OAuth2User principal) throws IOException, GeneralSecurityException {
      HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).build();
      credential.setAccessToken(client.getAccessToken().getTokenValue());
      Docs service = new Docs.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName("Card-O-Rama").build();
      Document doc = service.documents().get(docID).execute();
      List<Flashcard> flashcards = parseDocumentContent(doc.getBody().getContent());
      FlashcardSet flashcardSet = new FlashcardSet();
      flashcardSet.setSetName(doc.getTitle());
      flashcardSet.setColor("FFFFFF");
      flashcardSet.setEmail(principal.getAttribute("email"));
      flashcardSet.setPrivacy(FlashcardSetPrivacy.PRIVATE);
      List<FlashcardSet> flashcardSets = this.flashcardSetRepository.findByEmailOrderByPriority(principal.getAttribute("email"));
      flashcardSet.setPriority(flashcardSets.get(flashcardSets.size() - 1).getPriority() + 1);
      flashcardSet = this.flashcardSetRepository.save(flashcardSet);
      storeFlashcards(flashcardSet.getSetID(), flashcards);
   }

   private List<Flashcard> parseDocumentContent(List<StructuralElement> elements) {
      List<Flashcard> flashcards = new ArrayList<>();
      boolean firstParagraph = true;
      String separator = "";
      for (StructuralElement element : elements) {
         if (element.getParagraph() != null) {
            for (ParagraphElement paragraphElement : element.getParagraph().getElements()) {
               TextRun run = paragraphElement.getTextRun();
               if (run != null && run.getContent() != null) {
                  String termAndDefintion = run.getContent();
                  if (firstParagraph) {
                     separator = findSeparator(termAndDefintion);
                     if (separator.equals("")) continue;
                     firstParagraph = false;
                  }
                  if (!separator.equals("") && termAndDefintion.contains(separator)) {
                     flashcards.add(seperateTermAndDefinition(termAndDefintion, separator));
                  }
               }
            }
         }
      }
      return flashcards; 
   }

   private Flashcard seperateTermAndDefinition(String termAndDefString, String seperator) {
      Flashcard flashcard = new Flashcard();
      String[] termAndDef = termAndDefString.split(seperator, 2);
      flashcard.setTerm(termAndDef[0].trim());
      flashcard.setDefinition(termAndDef[1].trim());
      return flashcard;
   }

   private void storeFlashcards(long flashcardSetID, List<Flashcard> flashcards) {
      int size = flashcards.size();
      for (int i = 0; i < size; i++) {
         Flashcard flashcard = flashcards.get(i);
         flashcard.setPosition(i);
         flashcard.setSetID(flashcardSetID);
         this.flashcardRepository.save(flashcard);
      }
   }

   private String findSeparator(String termAndDef) {
      if (termAndDef.contains(":")) {
         return ":";
      } else if (termAndDef.contains("=")) {
         return "=";
      } else if (termAndDef.contains("-")) {
         return "-";
      } else if (termAndDef.contains("is")) {
         return "is";
      } else if (termAndDef.contains(",")) {
         return ",";
      } else {
         // no seperator found
         return "";
      }
   }
}
