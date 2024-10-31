package com.leadway_pensure.statement_generator.Controllers;

import com.leadway_pensure.statement_generator.Exceptions.InvalidFileTypeException;
import com.leadway_pensure.statement_generator.Models.PdfInfo;
import com.leadway_pensure.statement_generator.Models.StatementForm;
import com.leadway_pensure.statement_generator.Services.FileDownloadService;
import com.leadway_pensure.statement_generator.Services.LoggerService;
import com.leadway_pensure.statement_generator.Services.StatementService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class StatementController {
  private Logger logger = Logger.getLogger(StatementController.class.getName());
  @Autowired private FileDownloadService fileDownloadService;
  @Autowired private StatementService statementService;


  @GetMapping("/form")
  public String displayForm(HttpSession session) {
    if (session.getAttribute("user") == null) {
      return "redirect:/"; // Redirect to login if not logged in
    }
    return "form";
  }
  @GetMapping("/upload")
  public String displayUploadForm(HttpSession session) {
    if (session.getAttribute("user") == null) {
      return "redirect:/"; // Redirect to login if not logged in
    }
    return "upload";
  }


  @PostMapping("/uploadForm")
  public ResponseEntity<String> uploadForm(
          HttpSession session,
          @ModelAttribute StatementForm statementForm,
          @RequestParam("file") MultipartFile file,
          Model model,
          HttpServletResponse response) {
    String user = (String) session.getAttribute("user");
    if (session.getAttribute("user") == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set unauthorized status if not logged in
      return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
    }
      try{
        ArrayList<String> pins = statementService.parseCsv(file);
        return ResponseEntity.ok(pins.toString());
      } catch (
  InvalidFileTypeException e) {
       return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(e.getMessage());
      }

  }



  @PostMapping("/submitForm")
  public ResponseEntity<Map<String, Object>> submitForm(
          HttpSession session,
          @ModelAttribute StatementForm statementForm,
          Model model,
          HttpServletResponse response) {
    String user = (String) session.getAttribute("user");
    if (session.getAttribute("user") == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set unauthorized status if not logged in
      return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
    }

    ArrayList<PdfInfo> pdfInfoList = statementService.parsePins(statementForm);
    return getResponse(pdfInfoList, user);
  }

  private @NotNull ResponseEntity<Map<String, Object>> getResponse(ArrayList<PdfInfo> pdfInfoList, String user) {
    ArrayList<String> failedPins = new ArrayList<>();
    int successCount = 0;

    for (PdfInfo pdfInfo : pdfInfoList) {
      if (pdfInfo.getBase64Data() != null) {
        fileDownloadService.DownloadFile(pdfInfo.getName(), user, pdfInfo.getBase64Data());
        successCount++;
      } else {
        LoggerService loggerService = new LoggerService(user);
        loggerService.logSinglePin(pdfInfo.getName());
        failedPins.add(pdfInfo.getName());
      }
    }

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("successfulPins", successCount);
    responseMap.put("failedPinsCount", failedPins.size());
    responseMap.put("failedPins", failedPins);

    return ResponseEntity.ok(responseMap);
  }
//    ArrayList<String> errorPins = new ArrayList<String>();
//    if (pdfInfoList == null) {
//
//            model.addAttribute("notification", "Failed to generate statement(s).");
//
//            return "form";
//    }
//
//    logger.info("Generating statement(s)..." + pdfInfoList.size());
//    boolean isMultiple = pdfInfoList.size() > 1;
////    setReturnType(response, isMultiple, isMultiple ? "name" : pdfInfoList.get(0).getName());
//    LoggerService loggerService = new LoggerService(user);
//    if (isMultiple) {
//        errorPins = downloadMultipleStatements(pdfInfoList, model, response);
//        if (errorPins != null) {
//          model.addAttribute("success",true);
//            model.addAttribute(
//                    "notification",
//                    "Failed to generate statements for the following pins: " + errorPins.toString());
//                    loggerService.logMultiplePins(errorPins);
//                    return "form";
//
//        }
//    } else {
//        PdfInfo pdfInfo = pdfInfoList.get(0);
//        String base64Data = pdfInfo.getBase64Data();
//        if (base64Data != null) {
//          model.addAttribute("success",true);
//            logger.info("Downloading statement " + pdfInfo.getName());
//            downloadStatement(base64Data, model, response);
//        } else {
//            loggerService.logSinglePin(pdfInfo.getName());
//            model.addAttribute("notification", pdfInfo.getName() + " failed to generate statement.");
//            try {
//                response.sendRedirect("/form");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    return "form";


  private void setReturnType(HttpServletResponse response, boolean isMultiple, String name) {
    String contentType = isMultiple ? "application/zip" : "application/pdf";
    String fileName = isMultiple ? "statements.zip" : name + ".pdf";
    response.setContentType(contentType);
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
  }

  private ArrayList<String> downloadMultipleStatements(
      ArrayList<PdfInfo> pdfInfoList, Model model, HttpServletResponse response) {
    try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
      ArrayList<String> errorPins = new ArrayList<String>();
      for (PdfInfo pdfInfo : pdfInfoList) {
        String base64Data = pdfInfo.getBase64Data();

        if (base64Data == null) {
          errorPins.add(pdfInfo.getName());
        }
        byte[] pdfBytes = Base64.getDecoder().decode(base64Data);
        ZipEntry zipEntry = new ZipEntry(pdfInfo.getName() + ".pdf");
        zos.putNextEntry(zipEntry);
        zos.write(pdfBytes);
        zos.closeEntry();
      }
      model.addAttribute("success",true);

      logger.info("ZIP file generated successfully.");
      return errorPins;
    } catch (IOException e) {
      e.printStackTrace();
      model.addAttribute("success",false);
    }
    return null;
  }

  private void downloadStatement(String base64Data, Model model, HttpServletResponse response) {
    if (base64Data != null) {
      try {
        byte[] pdfBytes = Base64.getDecoder().decode(base64Data);

        try (OutputStream os = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os)) {
          bos.write(pdfBytes);
          bos.flush();
        }
        model.addAttribute("success",true);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      model.addAttribute("success",false);
    }
  }
}
