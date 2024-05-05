package edu.oswego.cs.CardORamaBackend.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.oswego.cs.CardORamaBackend.model.user.User;
import edu.oswego.cs.CardORamaBackend.model.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

   @Autowired
   private UserRepository userRepository;

   @Value("${reverse.proxy.port}")
   private String reverseProxyPort;

   @Value("${reverse.proxy.domain}")
   private String reverseProxyDomain;

   @RequestMapping("/")
   public void loginRedirect(HttpServletResponse httpServletResponse, @AuthenticationPrincipal OAuth2User principal) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         httpServletResponse.setHeader("Location", "http://" + reverseProxyDomain + ":" + reverseProxyPort + "/home");
         httpServletResponse.setStatus(302);
         String email = principal.getAttribute("email");
         String name = principal.getAttribute("name");

         if (!this.userRepository.findById(email).isPresent()) {
            User user = new User(email, name);
            this.userRepository.save(user);
         }
      }
   }
}
