package ng.com.starthub.myfarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ContactVet extends ActionBarActivity {
    final SmsManager smsManager = SmsManager.getDefault();
    EditText msg,clng,clat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_vet);
         clat= (EditText)findViewById(R.id.clat);
         clng= (EditText)findViewById(R.id.clng);
        clat.setEnabled(false);
        clng.setEnabled(false);
         msg= (EditText)findViewById(R.id.msg);
        Button send = (Button)findViewById(R.id.send);

        FarmLocation f = new FarmLocation(this);
        double latitude = f.getLatitude();
        double longitude = f.getLongitude();
        clng.setText(String.valueOf(longitude));
        clat.setText(String.valueOf(latitude));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLocation();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_vet, menu);
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
                        PreferenceManager.getDefaultSharedPreferences(ContactVet.this);
                final String emergencyCon = sharedPrefs.getString(
                        getString(R.string.pref_emergency_key),
                        getString(R.string.pref_emergency_default));

                if (emergencyCon == "") {
                    Toast.makeText(ContactVet.this, "ENTER A VALID EMERGENCY CONTACT", Toast.LENGTH_LONG).show();
                } else {
                    final String url = "http://maps.google.com/maps?q=";
                    final String smsText = "#VET DOCTOR REQUEST- INFESTATION: " + msg.getText()
                            + " AT " + url + clat.getText() + "," + clng.getText() + "VIA PoulCast";
                    smsManager.sendTextMessage(emergencyCon, null, smsText, null, null);
                    Toast.makeText(ContactVet.this, "MESSAGE SENT", Toast.LENGTH_SHORT).show();


                }
            }
        });

        smsAlert.show();

    }
}
