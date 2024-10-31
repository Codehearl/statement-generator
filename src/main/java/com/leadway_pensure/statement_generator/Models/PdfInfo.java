package com.leadway_pensure.statement_generator.Models;

public class PdfInfo {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private String base64Data;
    public PdfInfo(String name, String base64Data) {
        this.name = name;
        this.base64Data = base64Data;
    }
    public String getBase64Data() {
        return base64Data;
    }
    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }
    
}
