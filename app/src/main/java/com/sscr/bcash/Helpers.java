package com.sscr.bcash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Helpers {
    public static void getIpAddress(Context context){

        Callback IpAddressCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("IpAddressRequest", "Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String parameters = jsonResponse.optString("Parameters");

                        Session.setIpAddress(context, parameters);
                        Session.setLocation(context, "Unknown");
                    } else {
                        Log.e("IpAddressRequest", "Unsuccessful response: " + response.code());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("IpAddressRequest", "JSON Parsing Error");
                }
            }
        };

        OkHttpRequestHelper requestHelper = new OkHttpRequestHelper();

        RequestCustomHeaders customHeaders = new RequestCustomHeaders();
        customHeaders.addHeader("Content-Type", "application/json");

        RequestBody requestBody = new FormBody.Builder().build();

        requestHelper.makeRequest(context, Defaults.HelperEndPoint, customHeaders, requestBody, IpAddressCallback);
    }

    public static String getDevice(){

        String device = Build.DEVICE;  // Device model
        String manufacturer = Build.MANUFACTURER;  // Device manufacturer
        String brand = Build.BRAND;  // Device brand
        String model = Build.MODEL;  // Device model
        String product = Build.PRODUCT;  // Device product

        return ("| "+device +" | "+ manufacturer +" | "+ brand +" | "+ model +" | "+ product+" |");
    }

    public static void responseIntentController (Activity activity, String intentResponse) {
        switch (intentResponse) {
            case "":
                Session.setIntent(activity,"MobileLogin");
                Intent Blank = new Intent(activity, LoginActivity.class);
                activity.startActivity(Blank);
                activity.finish();
                break;

            case "Login":
                Session.setIntent(activity,"MobileLogin");
                Intent Login = new Intent(activity, LoginActivity.class);
                activity.startActivity(Login);
                activity.finish();
                break;

            case "OTPValidation":
                Session.setIntent(activity,"OTPValidation");
                Intent OTPValidation = new Intent(activity, OTPValidationActivity.class);
                activity.startActivity(OTPValidation);
                activity.finish();
                break;

            case "PINValidation":
                Session.setIntent(activity,"PINValidation");
                Intent PINValidation = new Intent(activity, PINValidationActivity.class);
                activity.startActivity(PINValidation);
                activity.finish();
                break;

            case "PINCreation":
                Session.setIntent(activity,"PINCreation");
                Intent PINCreation = new Intent(activity, PINCreationActivity.class);
                activity.startActivity(PINCreation);
                activity.finish();
                break;

            case "5":
                Intent User = new Intent(activity, HomeActivity.class);
                activity.startActivity(User);
                activity.finish();
                break;

            case "6":
                Intent Guest = new Intent(activity, HomeActivity.class);
                activity.startActivity(Guest);
                activity.finish();
                break;

            case "7":
                Intent Guardian = new Intent(activity, HomeActivity.class);
                activity.startActivity(Guardian);
                activity.finish();
                break;

            case "null":

                break;

            default:
                Session.setIntent(activity,"MobileLogin");
                Intent Default = new Intent(activity, LoginActivity.class);
                activity.startActivity(Default);
                activity.finish();
                break;
        }
    }

    public static void responseMessageController (Context context, String messageResponse) {
        if(messageResponse.length() > 0) {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, messageResponse, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e("Helpers", "Context is not an Activity");
            }
        }
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static String getHiddenBalance(String input) {
        StringBuilder modifiedString = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isDigit(currentChar)) {
                modifiedString.append('*');
            } else {
                modifiedString.append(currentChar);
            }
        }
        return "₱ "+modifiedString;
    }

    public static String getBalance(String input) {
        try {
            double amount = Double.parseDouble(input);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return "₱ " + decimalFormat.format(amount);
        } catch (NumberFormatException e) {
            return "Invalid Balance";
        }
    }

    public static String getHiddenPersonalId(String input) {
        StringBuilder modifiedString = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isDigit(currentChar)) {
                modifiedString.append('*');
            } else {
                modifiedString.append(currentChar);
            }
        }
        return modifiedString.toString();
    }

    public static String convertTimestamp(String inputTimestamp) {
        String desiredFormat = "MMM dd, yyyy h:mma";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(desiredFormat, Locale.US);

        try {
            Date date = inputFormat.parse(inputTimestamp);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
