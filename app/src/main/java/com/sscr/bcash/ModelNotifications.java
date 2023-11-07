package com.sscr.bcash;

public class ModelNotifications {
    String date;
    String title;
    String contents;
    public ModelNotifications(String date, String title, String contents) {
        this.date = date;
        this.title = title;
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContent(String content) {
        this.contents = contents;
    }
}
