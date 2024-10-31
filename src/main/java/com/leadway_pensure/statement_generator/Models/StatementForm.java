package com.leadway_pensure.statement_generator.Models;

// import java.util.Date;


    
public class StatementForm {
    private String pin;
    private String StartDate;
    private String EndDate;
    private int FundType;
    private boolean isPassworded;
    private boolean includeLogo;
    public boolean isIncludeLogo() {
        return includeLogo;
    }

    public void setIncludeLogo(boolean includeLogo) {
        this.includeLogo = includeLogo;
    }

    private String statementType;

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    // Getters and Setters
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        this.StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        this.EndDate = endDate;
    }

    public int getFundType() {
        return FundType;
    }

    public void setFundType(int fundType) {
        this.FundType = fundType;
    }

    public boolean isPassworded() {
        return isPassworded;
    }

    public void setPassworded(boolean passwordedFile) {
        this.isPassworded = passwordedFile;
    }
}
