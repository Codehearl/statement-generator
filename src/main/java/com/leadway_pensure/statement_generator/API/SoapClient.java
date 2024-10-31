package com.leadway_pensure.statement_generator.API;

import com.leadway_pensure.statement_generator.Models.StatementForm;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Component
public class SoapClient {

  // Create Soap Request without PIN specified
  public String createSoapRequest(StatementForm statementForm) {
    return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
        + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
        + "xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n"
        + "  <soap12:Body>\n"
        + "    <GetStatement xmlns=\"http://tempuri.org/\">\n"
        + "      <rq>\n"
        + "        <RSAPIN>"
        + statementForm.getPin()
        + "</RSAPIN>\n"
        + "        <StartDate>"
        + statementForm.getStartDate()
        + "</StartDate>\n"
        + "        <EndDate>"
        + statementForm.getEndDate()
        + "</EndDate>\n"
        + "        <FundId>"
        + statementForm.getFundType()
        + "</FundId>\n"
        + "        <StatementType>"
        + statementForm.getStatementType()
        + "</StatementType>\n"
        + "        <IncludeLogo>true</IncludeLogo>\n"
        + "        <PasswordFile>false</PasswordFile>\n"
        + "      </rq>\n"
        + "    </GetStatement>\n"
        + "  </soap12:Body>\n"
        + "</soap12:Envelope>\n";
  }

  // Create Soap Request With PIN specified
  public String createSoapRequest(String RsaPin, StatementForm statementForm) {
    return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
        + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
        + "xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n"
        + "  <soap12:Body>\n"
        + "    <GetStatement xmlns=\"http://tempuri.org/\">\n"
        + "      <rq>\n"
        + "        <RSAPIN>"
        + RsaPin
        + "</RSAPIN>\n"
        + "        <StartDate>"
        + statementForm.getStartDate()
        + "</StartDate>\n"
        + "        <EndDate>"
        + statementForm.getEndDate()
        + "</EndDate>\n"
        + "        <FundId>"
        + statementForm.getFundType()
        + "</FundId>\n"
        + "        <StatementType>"
        + statementForm.getStatementType()
        + "</StatementType>\n"
        + "        <IncludeLogo>true</IncludeLogo>\n"
        + "        <PasswordFile>false</PasswordFile>\n"
        + "      </rq>\n"
        + "    </GetStatement>\n"
        + "  </soap12:Body>\n"
        + "</soap12:Envelope>\n";
  }

  public String getStatement(String soapRequest) {
    int OK = 200;
    try {
      // Define the URL and open the connection
      URL url = new URL("http://172.16.26.16/WebStatement/Statement.asmx");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Set up the connection properties
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
      connection.setDoOutput(true);

      // Write the SOAP request to the output stream
      try (OutputStream outputStream = connection.getOutputStream()) {
        byte[] input = soapRequest.getBytes(StandardCharsets.UTF_8);
        outputStream.write(input, 0, input.length);
      }
      // Write the SOAP request to the output stream
      try (OutputStream outputStream = connection.getOutputStream()) {
        byte[] input = soapRequest.getBytes(StandardCharsets.UTF_8);
        outputStream.write(input, 0, input.length);
      }

      // Check the response code
      int responseCode = connection.getResponseCode();
      if (responseCode == OK) {
        // Read the response
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
          String line;
          while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
          }
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document =
            builder.parse(
                new java.io.ByteArrayInputStream(
                    response.toString().getBytes(StandardCharsets.UTF_8)));

        // Normalize the XML structure
        document.getDocumentElement().normalize();

        // Extract the <data> tag content
        NodeList nodeList = document.getElementsByTagName("Data");
        if (nodeList.getLength() > 0) {
          String dataContent = nodeList.item(0).getTextContent();
          return dataContent;
        } else {
          System.out.println("No <data> tag found in the response.");
        }
      } else {
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Message: " + connection.getResponseMessage());
        System.out.println("Response Content: " + connection.getContent());
      }
      connection.disconnect();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
