package edu.oswego.cs.CardORamaBackend.crudcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.http.HttpStatusCodes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:26910")
@RestController
@RequestMapping("/OAuthUtil")
public class OAuthController {
   
   @GetMapping("/getToken")
   public ResponseEntity<String> getAccessToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
      // Cookie cookie = new Cookie("_Access_Token", client.getAccessToken().getTokenValue());
      // cookie.setMaxAge(3600);
      // cookie.setPath("/");
      // response.addCookie(cookie);
      // response.setStatus(HttpStatusCodes.STATUS_CODE_OK);
      return ResponseEntity.ok().body(client.getAccessToken().getTokenValue());
   }
}
