package ng.com.starthub.myfarm.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gideon on 2/16/2015.
 */
public class LogDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "poultryLog.db";

    private static final int DATABASE_VERSION = 2;

    public LogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        PoultryLogger.onCreate(sqLiteDatabase);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        PoultryLogger.onUpgrade(sqLiteDatabase, oldVersion, newVersion);

    }
}
