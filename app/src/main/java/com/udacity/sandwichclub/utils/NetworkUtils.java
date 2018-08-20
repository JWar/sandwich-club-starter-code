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
import java.util.Iterator;
import java.util.Scanner;
import static com.udacity.sandwichclub.MainActivity.LOG_TAG;

/**
 * Created by JonGaming on 16/02/2018.
 * Handles network stuff...
 */
public class NetworkUtils {

    private static final String URL_STRING = "http://10.0.2.2:8080/jrclient_1.0.2/storetxn.aj";

    //This kicks off the Networking process. It takes the JSONObject parameter and calls
    //the other methods in this class, which make sure the JSONObject is bundled up
    //in the correct format and POSTed properly.
    //It returns the results for whatever called the method originally. In our case,
    //the NetworkAsyncTask in MainActivity.
    public static synchronized String sendContactDetails(JSONObject aJSONObjectToSend) {
        try {
            String serverResponseReceived = postJSONObject(aJSONObjectToSend);
            return serverResponseReceived;
        } catch (Exception e) {
            Log.i(LOG_TAG,"Error in NetworkUtils.sendContactDetails: "+e.getLocalizedMessage());
            return null;
        }
    }
    private static String postJSONObject(JSONObject aJSONObjectToSend) throws Exception {
        try {
            //Dont think I need to say what this is... Dont worry too much about what buildUrl is doing.
            //Android likes to complicate things... for good reasons mind you.
            URL url = buildUrl();

            //This is the object that handle the network connections.
            //You use these objects methods to get/post data
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //This is the posting bit!
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            //This is the string we are posting. It is the JSONObject in a particular
            //string format, which aligns with what the servlet expects.
            String requestToSend = getQuery(aJSONObjectToSend);
            wr.writeBytes(requestToSend);

            //House keeping. Making sure it is finished properly
            wr.flush();
            //House keeping. Making sure it is finished properly
            wr.close();

            //This is the response we have received from the servlet after the post.
            InputStream in = connection.getInputStream();

            //Dont worry about this. It just makes sure the response is in the correct format.
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            //The hasNext() and next() methods are similar to the iterator methods in
            //the getQuery method. Basically it asks if there is something to get, then
            //gets it! Anything 'got' is put into the response string.
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            //House keeping. Making sure it is finished properly
            scanner.close();
            //Lets the Log know what we have got.
            Log.i(LOG_TAG, "HttpUrl response: " + response);
            //This return is what ends up in the NetworkAsyncTask.onPostExecute in MainActivity
            //via sendContactDetails() above!
            return response;
        } catch (Exception e) {
            Log.i(LOG_TAG, "Error in NetworkUtils.postJSONObject: "+e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * This converts a JSON objects keys/values into a servlet friendly string
     * for the servlet to parse properly.
     * The format goes key1=value1&key2=value2&...
     */
    private static String getQuery(JSONObject aJSONObject) throws Exception {
        //This is the string we will return, we will build it up using the key/value in the JSONObject
        String result = "";
        //Ensures the & sign isnt put in in the wrong place.
        boolean first = true;
        //This is part of the JSONObject functionality and allows you to
        //loop over the JSONObject and treat it as a list.
        //This is the .keys() method which returns the iterator
        Iterator<String> iterator = aJSONObject.keys();
        while (iterator.hasNext()) {//Check to see if the iterator has a next element in its list.
            //Get the key from the next element in the iterator.
            String key = iterator.next();
            //Get the value using the key from the JSONObject
            String value = aJSONObject.getString(key);
            //This if/else ensures the & doesnt appear in the wrong place (i.e. key=value&)
            if (first) {
                first = false;
            } else {
                result += "&";
            }
            //This is where the string is built up using the key and value with an '=' in between
            result += key;
            result += "=";
            result += value;
        }
        //After the loop has finished return the string!
        return result;
    }
    /**
     * URLs arent just simple strings. Android needs to do a couple of things to them to
     * make sure theyre in the correct format. This is what this method does. Dont worry too much about
     * it.
     */
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
            Log.i(LOG_TAG, "Error in BuildURL: " + e.getLocalizedMessage());
            return null;
        }
    }
    //Not used yet, but Im sure you can guess what this does?
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
}
