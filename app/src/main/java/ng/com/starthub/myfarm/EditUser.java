package ng.com.starthub.myfarm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BROWNJEE on 17-Apr-15.
 */
public class EditUser extends Activity {

   /* EditText txtName;
    EditText txtPrice;
    EditText txtDesc;
    EditText txtCreatedAt;
    Button btnSave;
    Button btnDelete;*/

    String uid;
    // user JSONArray
    JSONArray user = null;
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // JSON parser class
    JSONParser jsonParser = new JSONParser();



    // url to get all products list
    private static String url_all_user = "http://nerdminds.com/farm/get_all_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";
    private static final String TAG_UID = "uid";
    private static final String TAG_SYMPTOMS = "desc_symptoms";
    private static final String TAG_SOLUTION = "control";
    private static final String TAG_DESCRIPTION = "description";

    ArrayList<HashMap<String, String>> diseaseList;
    String[] details = new String[2];
    TextView symptoms, soln;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailview);

        // save button
     //   btnSave = (Button) findViewById(R.id.btnSave);
        //btnDelete = (Button) findViewById(R.id.btnDelete);
        symptoms = (TextView) findViewById(R.id.des);
        soln = (TextView) findViewById(R.id.dsoln);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        uid = i.getStringExtra(TAG_UID);
        Log.v("id ", uid);

        // Getting complete product details in background thread
        new GetUserDetails().execute();



    }

/**
 * Background Async Task to Get complete product details
 * */
class GetUserDetails extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(EditUser.this);
        pDialog.setMessage("Loading. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
    /**
     * Getting product details in background thread
     * */
    protected String doInBackground(String... params) {
        // Building Parameters
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_user, "GET", params1);

        // Check your log cat for JSON reponse
        Log.d("All User: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                user = json.getJSONArray(TAG_USER);
                String id = null;
                String desc = null;
                String control = null;
                // looping through All Products
                for (int i = 0; i < user.length(); i++) {
                    JSONObject c = user.getJSONObject(i);

                    String userId = c.getString("uid");

                    if (uid.equals(userId)){
                        id = c.getString(TAG_UID);
                        desc = c.getString(TAG_SYMPTOMS);
                        control = c.getString(TAG_SOLUTION);
                        details[0] = desc;
                        details [1] = control;
                        break;
                    }
                    else {
                        Log.d("uid: = ", uid + " UserId: " + userId);

                    }


                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();


                    // adding each child node to HashMap key => value
                    //map.put(TAG_DESCRIPTION, desc);
                    //map.put(TAG_SOLUTION, control);




                    //txtDesc.setText(product.getString(TAG_DESCRIPTION));*/
                }
                //  diseaseList.add(map);

            }

            else {
                // no products found
                // Launch Add New product Activity
                //Intent i = new Intent(getApplicationContext(),
                // NewProductActivity.class);
                // Closing all previous activities
                // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();


        }

        return null;
    }
    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once got all details
        // display product data in EditText

        symptoms.setText(details[0]);
        soln.setText(details[1]);
        pDialog.dismiss();
    }
}

}
