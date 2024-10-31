package com.leadway_pensure.statement_generator.Controllers;

import com.leadway_pensure.statement_generator.Services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

  @Autowired private AuthenticationService authenticationService;

  @GetMapping("/")
  public String showLoginForm() {
    return "login"; // Return the name of the login view (login.html)
  }

  @PostMapping("/login")
  public String login(
      @RequestParam String username,
      @RequestParam String password,
      Model model,
      HttpSession session) {
    boolean isAuthenticated = authenticationService.login(username, password);
    if (isAuthenticated) {
      session.setAttribute("user", username);
      return "redirect:/form"; // Redirect to home page on successful login
    } else {
      model.addAttribute("error", "Invalid credentials");
      return "/"; // Return to login page with error message
    }
  }
}
