package com.sscr.bcash;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpRequestPoolingHelper {
    private OkHttpClient client;

    public OkHttpRequestPoolingHelper() {
        ConnectionPool connectionPool = new ConnectionPool(5, 30, TimeUnit.SECONDS);

        client = new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .build();
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
