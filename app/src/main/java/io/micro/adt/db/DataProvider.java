package io.micro.adt.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Database ContentProvider
 *
 * @author act262@gmail.com
 */
public class DataProvider extends ContentProvider {

    public static Uri HOSTS = Uri.parse("content://io.micro.adt/hosts");


    private final static int CODE_HOSTS = 1;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI("io.micro.adt", "hosts", CODE_HOSTS);
    }

    private SQLiteOpenHelper dbHelper;
    private ContentResolver contentResolver;

    @Override
    public boolean onCreate() {
        dbHelper = DbHelper.getInstance(getContext());
        contentResolver = getContext().getContentResolver();
        return false;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri u = null;
        if (uriMatcher.match(uri) == CODE_HOSTS) {
            long _id = dbHelper.getWritableDatabase().insert("hosts", "_id", values);
            u = ContentUris.withAppendedId(uri, _id);

            contentResolver.notifyChange(u, null);
        }
        return u;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id = 0;
        if (uriMatcher.match(uri) == CODE_HOSTS) {
            id = dbHelper.getWritableDatabase().delete("hosts", selection, selectionArgs);
            contentResolver.notifyChange(uri, null);
        }
        return id;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        if (uriMatcher.match(uri) == CODE_HOSTS) {
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            cursor = database.query("hosts", projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(contentResolver, uri);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int row = 0;
        if (uriMatcher.match(uri) == CODE_HOSTS) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            row = database.update("hosts", values, selection, selectionArgs);
        }
        return row;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
