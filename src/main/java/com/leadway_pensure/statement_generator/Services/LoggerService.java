package com.leadway_pensure.statement_generator.Services;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.ArrayList;
@Component
public class LoggerService {
    private String user;
    private static final Logger logger = Logger.getLogger(LoggerService.class.getName());
    private FileHandler fileHandler;

    public LoggerService(String user) {
        this.user = user;
        initializeLogger();
    }

    public LoggerService() {
        this.user = "defaultUser"; // Provide a default user if none is specified
        initializeLogger();
    }

    private void initializeLogger() {
        try {
            String networkPath = "//flsv/public/Statement_Folder/" + user + "/application.log";
            Path logDirectory = Paths.get("//flsv/public/Statement_Folder/" + user);
            if (!Files.exists(logDirectory)) {
                Files.createDirectories(logDirectory);
            }
            fileHandler = new FileHandler(networkPath, true);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void logSinglePin(String message) {
        logger.info(message);
    }
    public void logMultiplePins(ArrayList<String> pins) {
        for(String pin : pins) {
            logger.info(pin);
        }
    }
    public void logSevere(String message) {
        logger.severe(message);
    }
}