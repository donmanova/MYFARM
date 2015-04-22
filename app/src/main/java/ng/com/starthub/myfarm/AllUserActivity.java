package ng.com.starthub.myfarm;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
public class AllUserActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> userList;

    // url to get all products list
    private static String url_all_user = "http://nerdminds.com/farm/get_all_user.php";

    //JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";
    private static final String TAG_UID = "uid";
    private static final String TAG_NAME = "disease_name";

    // user JSONArray
    JSONArray user = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_user);

        // Hashmap for ListView
        userList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllUser().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String uid = ((TextView)view.findViewById(R.id.uid)).getText()
                        .toString();
               // String disease_name = ((TextView)findViewById(R.id.des)).getText()
                    //    .toString();
                //String control = ((TextView)findViewById(R.id.dsoln)).getText()
                       // .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditUser.class);
                // sending pid to next activity
               in.putExtra(TAG_UID, uid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });

    }
    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllUserActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_user, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All User: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    user = json.getJSONArray(TAG_USER);

                    // looping through All Products
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject c = user.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_UID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_UID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        userList.add(map);
                    }
                } else {
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllUserActivity.this, userList,
                            R.layout.list_item, new String[] { TAG_UID,
                            TAG_NAME},
                            new int[] { R.id.uid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}