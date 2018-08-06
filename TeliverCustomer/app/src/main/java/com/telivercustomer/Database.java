package com.telivercustomer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    private final static int DATABASEVERSION = 1;

    private final static String DATABASENAME = "teliver.db";

    public String TABLENAME = "tracking_details",MESSAGE= "message",TRACKING_ID = "tracking_id";

    public Database(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLENAME + "( " + MESSAGE + " TEXT,"  + TRACKING_ID + " TEXT PRIMARY KEY" + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + TABLENAME);
        onCreate(db);
    }
}

