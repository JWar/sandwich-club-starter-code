package com.udacity.sandwichclub;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.udacity.sandwichclub.model.Contact;
import com.udacity.sandwichclub.utils.NetworkUtils;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "starter";

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText numberEditText;

    private NetworkAsyncTask networkAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.main_name_edit_text);
        emailEditText = findViewById(R.id.main_email_edit_text);
        numberEditText = findViewById(R.id.main_number_edit_text);

    }

    public void onSave(View aView) {
        try {
            Toast.makeText(this, "Save pressed sending to server", Toast.LENGTH_SHORT).show();
            String nameString = nameEditText.getText().toString();
            String emailString = emailEditText.getText().toString();
            String numberString = numberEditText.getText().toString();

            JSONObject contactAsJSONObject = new JSONObject();
            contactAsJSONObject.put("name", nameString);
            contactAsJSONObject.put("email", emailString);
            contactAsJSONObject.put("number", numberString);

            networkAsyncTask = new NetworkAsyncTask();

            networkAsyncTask.execute(contactAsJSONObject);
        } catch (Exception e) {
            Log.i("starter","Error in MainActivity.onSave: "+e.getLocalizedMessage());
        }

    }

    public void onCancel(View aView) {
        Toast.makeText(this, "Cancel pressed clearing fields", Toast.LENGTH_SHORT).show();
        nameEditText.setText(null);
        emailEditText.setText(null);
        numberEditText.setText(null);
    }

    public class NetworkAsyncTask extends AsyncTask<JSONObject,Void,String> {
        @Override
        protected String doInBackground(JSONObject... aJSONObjects) {
            JSONObject objectToSend = aJSONObjects[0];
            String response = NetworkUtils.sendContactDetails(objectToSend);
            return response;
        }

        @Override
        protected void onPostExecute(String aResponse) {
            if (aResponse!=null) {
                Toast.makeText(MainActivity.this,
                        "Response received:" +
                                aResponse, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,
                        "Problem with network response", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
