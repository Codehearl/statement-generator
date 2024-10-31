package com.leadway_pensure.statement_generator.Models;

public class UserDetails {
    private int fundType;
    private String userName;

    public UserDetails(int fundType, String userName) {
        this.fundType = fundType;
        this.userName = userName;
    }

    public int getFundType() {
        return fundType;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "fundType=" + fundType +
                ", userName='" + userName + '\'' +
                '}';
    }
}