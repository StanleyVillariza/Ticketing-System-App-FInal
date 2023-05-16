package com.example.ticketingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class EditViolation extends AppCompatActivity {
    private static Button btnQuery, cancel;
    private static EditText fn, dob, ln, tov;
    private static TextView tv_civ, defburger;
    private static String cItemcode = "";
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "UpdateViolation.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    public static String String_isempty = "";
    public static final String FN = "fn";
    public static final String DOB = "dob";
    public static final String LN = "ln";
    public static final String TOV = "tov";
    public static String ID = "ID";
    private String flnme, dateofbirth, licensenumber, typeofviolation, aydi;
    public static String FullName = "";
    public static String Dateofbirth = "";
    public static String Licensenumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_violation);
        fn = (EditText) findViewById(R.id.drivername);
        dob = (EditText) findViewById(R.id.dob);
        ln = (EditText) findViewById(R.id.licensenum);
        tov = (EditText) findViewById(R.id.tov);
        cancel = (Button) findViewById(R.id.cancel);
        btnQuery = (Button) findViewById(R.id.submit);
        Intent i = getIntent();
        flnme = i.getStringExtra(FN);
        dateofbirth = i.getStringExtra(DOB);
        licensenumber = i.getStringExtra(LN);
        typeofviolation = i.getStringExtra(TOV);
        aydi = i.getStringExtra(ID);
        fn.setText(flnme);
        dob.setText(dateofbirth);
        ln.setText(licensenumber);
        tov.setText(typeofviolation);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullName = fn.getText().toString();
                Dateofbirth = dob.getText().toString();
                Licensenumber = ln.getText().toString();
                typeofviolation = tov.getText().toString();
                new uploadDataToURL().execute();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditViolation.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(EditViolation.this);

        public uploadDataToURL() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = aydi;
                cv.put("ViolationID", cPostSQL);

                cPostSQL = " '" + FullName + "' ";
                cv.put("Fullname", cPostSQL);

                cPostSQL = " '" + Dateofbirth + "' ";
                cv.put("Dateofbirth", cPostSQL);

                cPostSQL = " '" + Licensenumber + "' ";
                cv.put("Licensenumber", cPostSQL);

                cPostSQL = " '" + typeofviolation + "' ";
                cv.put("Typeofviolation", cPostSQL);


                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(EditViolation.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(EditViolation.this, s, Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
