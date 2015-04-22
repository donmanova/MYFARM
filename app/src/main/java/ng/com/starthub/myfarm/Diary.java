package ng.com.starthub.myfarm;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import ng.com.starthub.myfarm.data.PoultryLogProvider;
import ng.com.starthub.myfarm.data.PoultryLogger;


public class Diary extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Button sav =(Button)findViewById(R.id.save);
        final EditText ldate = (EditText)findViewById(R.id.ldate);
        final EditText log = (EditText)findViewById(R.id.log);
        Calendar c=Calendar.getInstance();

        String mYear= String.valueOf(c.get(Calendar.YEAR));
        String mMonth= String.valueOf(c.get(Calendar.MONTH)+1);
        String mDay= String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String tDay= mDay + "/" + mMonth + "/"+mYear;

        ldate.setText(tDay);
        ldate.setEnabled(false);


        sav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();
                values.put(PoultryLogger.COLUMN_DATE, ldate.getText().toString() );
                values.put(PoultryLogger.COLUMN_LOG, log.getText().toString() );




                // Creating an instance of LocationInsertTask

                getContentResolver().insert(PoultryLogProvider.CONTENT_URI, values);

                Toast.makeText(Diary.this, "LOG SAVED", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diary, menu);
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
}
