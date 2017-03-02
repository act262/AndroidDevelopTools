package io.micro.adt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Helper
 *
 * @author act262@gmail.com
 */
public class DbHelper extends SQLiteOpenHelper {

    public static DbHelper getInstance(Context context) {
        return new DbHelper(context, "adt.db", null, 1);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table hosts(_id integer primary key autoincrement," +
                "title text not null,content text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
