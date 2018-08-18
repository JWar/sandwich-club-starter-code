package com.udacity.sandwichclub.utils;

import android.net.Uri;
import android.util.Log;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import static com.udacity.sandwichclub.MainActivity.LOG_TAG;

/**
 * Created by JonGaming on 16/02/2018.
 * Handles network stuff...
 */
public class NetworkUtils {

    private static final String URL_STRING = "http://10.0.2.2:8080/jrclient/contact.aj";

    public static synchronized String sendContactDetails(JSONObject aJSONObjectToSend) {
        try {
            String serverResponseReceived = postJSONObject(aJSONObjectToSend);
            return serverResponseReceived;
        } catch (Exception e) {
            Log.i(LOG_TAG,"Error in NetworkUtils.getMoviesOrderBy: "+e.getLocalizedMessage());
            return null;
        }
    }
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            Log.i(LOG_TAG, "HttpUrl response: " + response);
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
    private static String postJSONObject(JSONObject aJSONObjectToSend) throws Exception {
        URL url = buildUrl();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

        String stringToSend = aJSONObjectToSend.toString();
        wr.writeBytes(stringToSend);

        wr.flush();
        wr.close();

        InputStream in = connection.getInputStream();

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        String response = null;
        if (hasInput) {
            response = scanner.next();
        }
        scanner.close();
        Log.i(LOG_TAG, "HttpUrl response: " + response);
        return response;
    }
    private static URL buildUrl() {
        String urlToParse = URL_STRING;
        Uri uri = Uri.parse(urlToParse)
                .buildUpon()
                .build();
        try {
            URL url = new URL(uri.toString());
            Log.i(LOG_TAG, "URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
