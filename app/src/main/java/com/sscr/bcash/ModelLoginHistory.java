package com.sscr.bcash;

public class ModelLoginHistory {
    private String AccountAddress ;
    private String IpAddress ;
    private String Location ;
    private String Device ;
    private String LastOnline;

    public ModelLoginHistory(String accountAddress, String ipAddress, String location, String device, String lastOnline) {
        AccountAddress = accountAddress;
        IpAddress = ipAddress;
        Location = location;
        Device = device;
        LastOnline = lastOnline;
    }

    public String getAccountAddress() {
        return AccountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        AccountAddress = accountAddress;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public String getLastOnline() {
        return LastOnline;
    }

    public void setLastOnline(String lastOnline) {
        LastOnline = lastOnline;
    }
}
