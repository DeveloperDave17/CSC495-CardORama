package edu.oswego.cs.CardORamaBackend.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

   @RequestMapping("/")
   public void loginRedirect(HttpServletResponse httpServletResponse) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth.isAuthenticated()) {
         httpServletResponse.setHeader("Location", "http://localhost:5173");
         httpServletResponse.setStatus(302);
      }
   }
}
