package com.leadway_pensure.statement_generator.Services;

import com.leadway_pensure.statement_generator.API.SoapClient;
import com.leadway_pensure.statement_generator.Exceptions.InvalidFileTypeException;
import com.leadway_pensure.statement_generator.Exceptions.UserNotFoundException;
import com.leadway_pensure.statement_generator.Models.PdfInfo;
import com.leadway_pensure.statement_generator.Models.StatementForm;
import com.leadway_pensure.statement_generator.Models.UserDetails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StatementService {

  private static final Logger logger = Logger.getLogger(StatementService.class.getName());
  @Autowired private SoapClient soapClient;
  @Autowired private NavisionService navisionService;

  public ArrayList<PdfInfo> parsePins(StatementForm statementForm) {
    ArrayList<PdfInfo> pdfInfos = new ArrayList<PdfInfo>();
    String pin = statementForm.getPin();
    String[] pins = pin.split("\\r?\\n"); // Split by newlines (handles both \r\n and \n)

    logger.log(Level.INFO, "Number of pins: " + pins.length);
    if (pins.length == 1) {
      logger.log(Level.INFO, "Single pin");

      pdfInfos.add(getPdfData(statementForm, pdfInfos, pin));
      return pdfInfos;
    } else if (pins.length > 1) {
      if (pins.length < 100) {
        logger.log(Level.INFO, "Multiple pins" + pins.length);
        for (String individualPin : pins) {
          logger.log(Level.INFO, "Processing pin: " + individualPin);
          statementForm.setPin(individualPin);
          PdfInfo info = (getPdfData(statementForm, pdfInfos, individualPin));
          if (info.getBase64Data() != null) {
            pdfInfos.add(info);
          }
          logger.log(Level.INFO, "Result strings: " + pdfInfos.size());
        }
        return pdfInfos;
      } else {
        logger.log(Level.INFO, "Too many pins");
        return null;
      }
    } else {
      logger.log(Level.INFO, "No pin");
    }
    return null;
  }

    public ArrayList<String> parseCsv(MultipartFile file) throws InvalidFileTypeException {
        ArrayList<String> pins = new ArrayList<>();
        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            logger.log(Level.SEVERE, "Uploaded file is not a CSV file");
            throw new InvalidFileTypeException("Invalid file type. Please upload a CSV file");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String pin = line.trim();
                if (!pin.isEmpty()) {
                    pins.add(pin);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading CSV file", e);
        }
        return pins;
}

  private PdfInfo getPdfData(StatementForm statementForm, ArrayList<PdfInfo> pdfInfos, String pin) {
    try {
      
      UserDetails user = navisionService.getUserDetails(pin);
      statementForm.setFundType(user.getFundType());
      String soapRequeString = soapClient.createSoapRequest(statementForm);
      String pdfData = soapClient.getStatement(soapRequeString);
      return new PdfInfo(user.getUserName(), pdfData);
    } catch (UserNotFoundException ex) {
      logger.log(Level.INFO, "User not found");

      return new PdfInfo(pin, null);
    }
  }
}
