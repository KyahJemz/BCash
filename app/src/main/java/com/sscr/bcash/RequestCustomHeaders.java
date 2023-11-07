package com.sscr.bcash;

import java.util.HashMap;

public class RequestCustomHeaders {
    private HashMap<String, String> headers;

    public RequestCustomHeaders() {
        headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }
}

