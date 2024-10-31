package com.leadway_pensure.statement_generator.Services;

import com.leadway_pensure.statement_generator.API.NavisionWebClient;
import com.leadway_pensure.statement_generator.Exceptions.UserNotFoundException;
import com.leadway_pensure.statement_generator.Models.UserDetails;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NavisionService {
  private static final Logger logger = Logger.getLogger(NavisionService.class.getName());
  @Autowired private NavisionWebClient webClient;

  public UserDetails getUserDetails(String pin) {
    JSONObject jsonResponse = webClient.searchEmployee(pin);
    logger.log(Level.INFO, jsonResponse.toString());
    boolean success = jsonResponse.getBoolean("status");
    if (!success) {
      throw new UserNotFoundException("User not found");
    }
    JSONObject employee = jsonResponse.getJSONObject("employee");

    int fundType = employee.getInt("fundId");
    String userName = employee.getString("fullName");
    UserDetails userDetails = new UserDetails(fundType, userName);

    return userDetails;
  }
}
