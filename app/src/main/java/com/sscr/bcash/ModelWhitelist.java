package com.sscr.bcash;

public class ModelWhitelist {
    private String AccountAddress ;
    private String WhitelistedAddress ;
    private String WhitelistedName;
    private String Timestamp ;

    public ModelWhitelist(String accountAddress, String whitelistedAddress, String whitelistedName, String timestamp) {
        AccountAddress = accountAddress;
        WhitelistedAddress = whitelistedAddress;
        WhitelistedName = whitelistedName;
        Timestamp = timestamp;
    }

    public String getAccountAddress() {
        return AccountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        AccountAddress = accountAddress;
    }

    public String getWhitelistedAddress() {
        return WhitelistedAddress;
    }

    public void setWhitelistedAddress(String whitelistedAddress) {
        WhitelistedAddress = whitelistedAddress;
    }

    public String getWhitelistedName() {
        return WhitelistedName;
    }

    public void setWhitelistedName(String whitelistedName) {
        WhitelistedName = whitelistedName;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
