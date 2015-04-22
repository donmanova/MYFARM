package ng.com.starthub.myfarm;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import ng.com.starthub.myfarm.data.PoultryLogProvider;
import ng.com.starthub.myfarm.data.PoultryLogger;


public class ViewLog extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log);

       ListView lv = (ListView)findViewById(android.R.id.list);
        //registerForContextMenu(lv);




        String[] projections = new String[] { PoultryLogger.COLUMN_ID,
                PoultryLogger.COLUMN_DATE, PoultryLogger.COLUMN_LOG };
        Cursor c = getContentResolver().query(PoultryLogProvider.CONTENT_URI,
                projections, null, null, null);
        startManagingCursor(c);

        String[] columns =  new String[] {
                PoultryLogger.COLUMN_DATE, PoultryLogger.COLUMN_LOG,  };


        int[] to = new int[] {R.id.ddate,
                 R.id.dlog };

        final SimpleCursorAdapter cAdapter = new SimpleCursorAdapter(this,
                R.layout.favorite_list, c, columns, to);

        this.setListAdapter(cAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_log, menu);
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
