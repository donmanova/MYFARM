package ng.com.starthub.myfarm.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Gideon on 2/17/2015.
 */
public class PoultryLogProvider extends ContentProvider {
    LogDbHelper database;


    private static final int LOG = 10;
    private static final int LOG_ID = 20;

    private static HashMap<String, String> projectionMap;




    private static final String AUTHORITY = "ng.com.starthub.myfarm";

    private static final String BASE_PATH = "logger";      //changed from location
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);


    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/logger";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/logger";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, LOG);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", LOG_ID);

        projectionMap = new HashMap<String, String>();
        projectionMap.put(PoultryLogger.COLUMN_ID, PoultryLogger.COLUMN_ID);
        projectionMap.put(PoultryLogger.COLUMN_DATE, PoultryLogger.COLUMN_DATE);
        projectionMap.put(PoultryLogger.COLUMN_LOG, PoultryLogger.COLUMN_LOG);



    }



    @Override
    public boolean onCreate() {
        database =new LogDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
       // checkColumns(projection);

        // Set the table
        queryBuilder.setTables(PoultryLogger.TABLE_LOG);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case LOG:
            queryBuilder.setProjectionMap(projectionMap);
                break;
       /*     case LOCATION_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(LocationLogger.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case LOCATION_DATE:

                break;
            case LOCATION_TIME:
                break;    */
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
       // return null;
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)){
            case LOG:
                return CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI "
                        + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri locationUri;
        int uriType = sURIMatcher.match(uri);
        if (uriType != LOG) { throw new
                IllegalArgumentException("Unknown URI " + uri); }
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id ;

      id = sqlDB.insert(PoultryLogger.TABLE_LOG, null, values);
        if (id > 0)
            locationUri =
                    ContentUris.withAppendedId(CONTENT_URI,
                            id);
            else
                throw new SQLException("Failed to insert row into " + uri);



       // throw new SQLException("Failed to insert row into " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return locationUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case LOG:
                rowsDeleted = sqlDB.delete(PoultryLogger.TABLE_LOG, selection,
                        selectionArgs);
                break;
           case LOG_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PoultryLogger.TABLE_LOG,
                            PoultryLogger.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(PoultryLogger.TABLE_LOG,
                            PoultryLogger.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated ;
        switch (uriType) {
            case LOG:
                rowsUpdated = sqlDB.update(PoultryLogger.TABLE_LOG,
                        values,
                        selection,
                        selectionArgs);
                break;
           /* case LOCATION_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(LocationLogger.TABLE_LOG,
                            values,
                            LocationLogger.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(LocationLogger.TABLE_LOG,
                            values,
                            LocationLogger.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;      */
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;



    }

    private void checkColumns(String[] projection) {
        String[] available = {PoultryLogger.COLUMN_ID, PoultryLogger.COLUMN_DATE,
                PoultryLogger.COLUMN_LOG
                };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
