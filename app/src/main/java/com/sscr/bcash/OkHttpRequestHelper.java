package com.sscr.bcash;

import android.content.Context;

import okhttp3.*;

public class OkHttpRequestHelper {
    private OkHttpClient client;

    public OkHttpRequestHelper() {
        client = new OkHttpClient();
    }

    public void makeRequest(Context context, String url, RequestCustomHeaders customHeaders, RequestBody requestBody, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(Defaults.API + url);

        if (customHeaders != null) {
            for (String header : customHeaders.getHeaders().keySet()) {
                requestBuilder.addHeader(header, customHeaders.getHeaders().get(header));
            }
        }

        requestBuilder.post(requestBody);

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(callback);
    }

}
