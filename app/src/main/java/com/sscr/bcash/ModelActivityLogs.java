package com.sscr.bcash;

public class ModelActivityLogs {
    private String AccountAddress;
    private String TargetAccountAddress;
    private String Action;
    private String Task;
    private String Timestamp;

    public ModelActivityLogs(String accountAddress, String targetAccountAddress, String action, String task, String timestamp) {
        AccountAddress = accountAddress;
        TargetAccountAddress = targetAccountAddress;
        Action = action;
        Task = task;
        Timestamp = timestamp;
    }

    public String getAccountAddress() {
        return AccountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        AccountAddress = accountAddress;
    }

    public String getTargetAccountAddress() {
        return TargetAccountAddress;
    }

    public void setTargetAccountAddress(String targetAccountAddress) {
        TargetAccountAddress = targetAccountAddress;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getTask() {
        return Task;
    }

    public void setTask(String task) {
        Task = task;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
