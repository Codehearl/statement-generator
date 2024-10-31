package com.leadway_pensure.statement_generator.API;

import java.net.URI;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NavisionWebClient {

  public static final String HOST = "172.16.27.13";
  public static final String SEARCH_EMPLOYEE_PATH = "/CustomerIntegration/SearchEmployee";
  public static final String GET_CONTRIBUTION_PATH = "/AccountIntegration/GetContribution";
  public static final String GET_TOTAL_CONTRIBUTION_PATH =
      "/AccountIntegration/GetTotalContribution";
  public static final String GET_STATEMENT_PATH = "/AccountIntegration/GetStatement";
  public static final String GET_CONTRIBUTION_TRANSACTIONS_PATH =
      "/AccountIntegration/GetContributionTransactions";

  public JSONObject searchEmployee(String penComID) {
    URI uri =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host(HOST)
            .path(SEARCH_EMPLOYEE_PATH)
            .queryParam("rsapin", penComID)
            .build()
            .toUri();

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

    String body = response.getBody();
    try {
      return new JSONObject(body);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public JSONObject getContributions(
      String penComID, String fundAdminfundID, String start, String end) {
    URI uri =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host(HOST)
            .path(GET_CONTRIBUTION_PATH)
            .queryParam("rsapin", penComID)
            .queryParam("fundid", fundAdminfundID)
            .queryParam("startDate", start)
            .queryParam("endDate", end)
            .build()
            .toUri();
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

    String body = response.getBody();
    try {
      return new JSONObject(body);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public JSONObject getTotalContributions(
      String penComID, String fundAdminfundID, String start, String end) {
    URI uri =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host(HOST)
            .path(GET_TOTAL_CONTRIBUTION_PATH)
            .queryParam("rsapin", penComID)
            .queryParam("fundid", fundAdminfundID)
            .build()
            .toUri();
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

    String body = response.getBody();
    try {
      return new JSONObject(body);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public JSONObject getStatement(
      String penComID, String fundAdminfundID, String start, String end) {
    URI uri =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host(HOST)
            .path(GET_STATEMENT_PATH)
            .queryParam("rsapin", penComID)
            .queryParam("fundid", fundAdminfundID)
            .queryParam("startDate", start)
            .queryParam("endDate", end)
            .build()
            .toUri();
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

    String body = response.getBody();
    try {
      return new JSONObject(body);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public JSONObject getContributionTransactions(
      String penComID, String fundAdminfundID, String start, String end) {
    URI uri =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host(HOST)
            .path(GET_CONTRIBUTION_TRANSACTIONS_PATH)
            .queryParam("rsapin", penComID)
            .queryParam("fundid", fundAdminfundID)
            .queryParam("startDate", start)
            .queryParam("endDate", end)
            .build()
            .toUri();
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

    String body = response.getBody();
    try {
      return new JSONObject(body);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
