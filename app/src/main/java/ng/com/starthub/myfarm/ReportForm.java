package ng.com.starthub.myfarm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ReportForm extends ActionBarActivity {
    FarmLocation farmLocation;
    TextView lat, lng,disease;
    final SmsManager smsManager = SmsManager.getDefault();
    EditText pname,phone,email,town,disease_name,desc, control;
    private static String url_create_user = "http://nerdminds.com/farm/create_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private ProgressDialog pDialog;
    Boolean isButtonClicked = false;
    JSONObject json;
    JSONParser myJsonParser;


    String uname;
    String uphone;
    String uemail;
    String utown;
    String ulat;
    String ulng;
    String udisease;
    String udesc;
    String ucontrol;


    InputStream is=null;
    String result=null;
    String line=null;
    int code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);
        lat = (TextView) findViewById(R.id.lat);
        lng = (TextView) findViewById(R.id.lng);
       pname= (EditText)findViewById(R.id.name);
        phone= (EditText)findViewById(R.id.phone);
        email= (EditText)findViewById(R.id.email);
        town= (EditText)findViewById(R.id.location);
        disease_name= (EditText)findViewById(R.id.disease);
        desc= (EditText)findViewById(R.id.desc);
        control= (EditText)findViewById(R.id.control);
        //pname = (TextView) findViewById(R.id.pest);
        disease = (TextView) findViewById(R.id.disease);
        lng = (TextView) findViewById(R.id.lng);
        lat.setEnabled(false);
        lng.setEnabled(false);
        Button canc = (Button)findViewById(R.id.cancel);
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(ReportForm.this, MainActivity.class);
                startActivity(intent);
            }
        });



        farmLocation = new FarmLocation(ReportForm.this);

        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // sendLocation();
                uname = pname.getText().toString();
                uphone = phone.getText().toString();
                uemail = email.getText().toString();
                utown = town.getText().toString();
                ulat = lat.getText().toString();
                ulng = lng.getText().toString();
                udisease = disease_name.getText().toString();
                udesc = desc.getText().toString();
                ucontrol = control.getText().toString();

                    CreateNewUser newUser = new CreateNewUser();
                    newUser.execute();

            }
        });


        // farmLocation.getLocation();


       locationFetcher();

    }



    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            pDialog = new ProgressDialog(ReportForm.this);
            pDialog.setMessage("Submitting ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        /**
         * Creating user
         * */
        protected String doInBackground(String... args) {






            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            // nameValuePairs.add(new BasicNameValuePair("id",id));
            // nameValuePairs.add(new BasicNameValuePair("name",name));

            // Building Parameters
            // List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", uname));
            params.add(new BasicNameValuePair("phone", uphone));
            params.add(new BasicNameValuePair("email", uemail));
            params.add(new BasicNameValuePair("town", ucontrol));
            params.add(new BasicNameValuePair("lat", ulat));
            params.add(new BasicNameValuePair("lng", ulng));
            params.add(new BasicNameValuePair("disease_name", udisease));
            params.add(new BasicNameValuePair("desc_symptoms", udesc));
            params.add(new BasicNameValuePair("control", ucontrol));

            try
            {

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://nerdminds.com/farm/create_user.php");
                httppost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            }
            catch(Exception e)
            {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try
            {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            }
            catch(Exception e)
            {
                Log.e("Fail 2", e.toString());
            }

            try
            {
                JSONObject json_data = new JSONObject(result);
                code=(json_data.getInt("code"));

                if(code==1)
                {

                    Toast.makeText(getBaseContext(), "Inserted Successfully",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Sorry, Try Again",
                            Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception e)
            {
                Log.e("Fail 3", e.toString());
            }



            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getBaseContext(), "Inserted Successfully",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), AllUserActivity.class);
            startActivity(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void locationFetcher() {

        if (farmLocation.canGetLocation()) {
            //farmLocation.getLocation();
            double latitude = farmLocation.getLatitude();
            double longitude = farmLocation.getLongitude();
            lng.setText(String.valueOf(longitude));
            lat.setText(String.valueOf(latitude));


        } else {

            Toast.makeText(this, "LOCATION NOT ACQUIRED,TURN ON A PROVIDER", Toast.LENGTH_SHORT).show();


        }

    }

    private void sendLocation() {

        final AlertDialog.Builder smsAlert = new AlertDialog.Builder(this);
        smsAlert.setTitle("SMS CONFIRMATION");
        smsAlert.setMessage("SMS DRAWS SERVICE CHARGES; DO YOU WANT TO CONTINUE?");
        smsAlert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        smsAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(ReportForm.this);
                final String emergencyCon = sharedPrefs.getString(
                        getString(R.string.pref_emergency_key),
                        getString(R.string.pref_emergency_default));

                if (emergencyCon == "") {
                    Toast.makeText(ReportForm.this, "ENTER A VALID EMERGENCY CONTACT", Toast.LENGTH_LONG).show();
                } else {
                    final String url = "http://maps.google.com/maps?q=";
                    final String smsText = "INFESTATION: " + disease.getText()
                            + " AT " + url + lat.getText() + "," + lng.getText() + "#Eagleye";
                    smsManager.sendTextMessage(emergencyCon, null, smsText, null, null);
                    Toast.makeText(ReportForm.this, "MESSAGE SENT", Toast.LENGTH_SHORT).show();


                }
            }
        });

        smsAlert.show();

    }

    public void insert()
    {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        // nameValuePairs.add(new BasicNameValuePair("id",id));
       // nameValuePairs.add(new BasicNameValuePair("name",name));

        // Building Parameters
       // List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", uname));
        params.add(new BasicNameValuePair("phone", uphone));
        params.add(new BasicNameValuePair("email", uemail));
        params.add(new BasicNameValuePair("town", ucontrol));
        params.add(new BasicNameValuePair("lat", ulat));
        params.add(new BasicNameValuePair("lng", ulng));
        params.add(new BasicNameValuePair("disease_name", udisease));
        params.add(new BasicNameValuePair("desc_symptoms", udesc));
        params.add(new BasicNameValuePair("control", ucontrol));

        try
        {

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://nerdminds.com/farm/create_user.php");
            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success " + result);
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONObject json_data = new JSONObject(result);
            code=(json_data.getInt("success"));

            if(code==1)
            {
                Toast.makeText(getBaseContext(), "Inserted Successfully",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Sorry, Try Again",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());
        }
    }
}



