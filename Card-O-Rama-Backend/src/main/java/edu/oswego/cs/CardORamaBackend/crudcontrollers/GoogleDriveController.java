package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.auth.openidconnect.HttpTransportFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/GoogleDrive")
public class GoogleDriveController {
   
   private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

   @GetMapping("/info")
   public List<File> getDriveInfo(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) throws IOException, GeneralSecurityException {
      HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).build();
      credential.setAccessToken(client.getAccessToken().getTokenValue());
      Drive drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName("Card-O-Rama").build();
      FileList result = drive.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
      List<File> files = result.getFiles();
      return files;
   }
}
