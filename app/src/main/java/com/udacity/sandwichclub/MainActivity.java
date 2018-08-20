package com.udacity.sandwichclub;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.udacity.sandwichclub.utils.NetworkUtils;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "starter";

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText numberEditText;

    private NetworkAsyncTask networkAsyncTask;

    /**
     * onCreate is what is called when the App is run and MainActivity is started.
     * I have done a few things in it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Dont worry about super for now.
        super.onCreate(savedInstanceState);
        //Tells the Activity what view/screen design it should use
        setContentView(R.layout.activity_main);

        //This is the bit where you connect the views to Activity.
        //Its so we can get the text entered in the form fields,
        //and put them into the JSONObject (this is in onSave of course!)
        nameEditText = findViewById(R.id.main_name_edit_text);
        emailEditText = findViewById(R.id.main_email_edit_text);
        numberEditText = findViewById(R.id.main_number_edit_text);
    }

    //This is what is called when the Save button is pressed.
    public void onSave(View aView) {
        //Try and catch (at the bottom of this method) are just to pick up errors that may occur.
        //Dont worry for now.
        try {
            //Get the text written in the forms then convert it to string.
            String nameString = nameEditText.getText().toString();
            String emailString = emailEditText.getText().toString();
            String numberString = numberEditText.getText().toString();

            //Turn the text into a JSONObject with the correct keys
            JSONObject contactAsJSONObject = new JSONObject();
            contactAsJSONObject.put("name", nameString);
            contactAsJSONObject.put("email", emailString);
            contactAsJSONObject.put("number", numberString);

            //Instantiate a new NetworkAsyncTask object. It does the Asynchronous work,
            //and makes the network calls to the servlet.
            networkAsyncTask = new NetworkAsyncTask();

            //Execute is what tells the NetworkAsyncTask to do the network work. Does this
            //using the JSONObject we created above, which we want POSTing to the servlet.
            networkAsyncTask.execute(contactAsJSONObject);
        } catch (Exception e) {
            Log.i("starter","Error in MainActivity.onSave: "+e.getLocalizedMessage());
        }
    }
    //This is my custom AsyncTask.
    public class NetworkAsyncTask extends AsyncTask<JSONObject,Void,String> {
        @Override
        protected String doInBackground(JSONObject... aJSONObjects) {
            //This is now a background thread.
            //Get the JSONObject from the parameters in doInBackground.
            //Notice the parameter in doInBackground looks a little different than
            //the other parameters.
            //From this can you guess why we need to use [0]? What does this remind you of?
            JSONObject objectToSend = aJSONObjects[0];

            //This is then the bit where the networking is called.
            //Control + Click on .sendContactDetails to take you to that method
            String response = NetworkUtils.sendContactDetails(objectToSend);

            //Response received, now pass it to onPostExecute below.
            //We are still on the background thread now. Up until the return is
            //finished.
            return response;
        }

        @Override
        protected void onPostExecute(String aResponse) {
            //This is where the response ends up. It is now up to you what you do
            //with it.
            //Note you are now on the Main Thread again!
            if (aResponse!=null) {//This is just to check there were no errors, vital to handle them properly
                //A Toast is a little message that pops up at the bottom of your screen.
                //Im sure youll have seen one before in Android apps.
                Toast.makeText(MainActivity.this,
                        "Response received:" +
                                aResponse, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,
                        "Problem with network response", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //This is what is called when the Cancel button is pressed
    public void onCancel(View aView) {
        nameEditText.setText(null);
        emailEditText.setText(null);
        numberEditText.setText(null);
    }
}
