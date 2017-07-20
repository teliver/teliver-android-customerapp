package com.telivercustomer;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.teliver.sdk.core.TLog;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.UserBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Model> listTrackingId;

    private AdapterTrackingId adapterTrackingId;

    private String userName = "selvakumar";

    private Application application;

    private TextView txtNoData;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TLog.setVisible(true);
        Teliver.identifyUser(new UserBuilder(userName).setUserType(UserBuilder.USER_TYPE.OPERATOR).registerPush().build());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        application = (Application) getApplication();
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listTrackingId = new ArrayList<>();
        refreshData();
        adapterTrackingId = new AdapterTrackingId(this);
        adapterTrackingId.setData(listTrackingId);
        recyclerView.setAdapter(adapterTrackingId);
        adapterTrackingId.notifyDataSetChanged();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("tripId"));
        if (listTrackingId.isEmpty())
            txtNoData.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("msg");
            String trackingId = intent.getStringExtra(Application.TRACKING_ID);
            Database database = new Database(MainActivity.this);
            SQLiteDatabase db = database.getWritableDatabase();

            if (message.equalsIgnoreCase("trip_stopped")) {
                String deleteQuery = "DELETE FROM " + database.TABLENAME + " WHERE " + database.TRACKING_ID + " = "
                        + "'" + trackingId + "'";
                db.execSQL(deleteQuery);
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(database.TRACKING_ID, trackingId);
                contentValues.put(database.MESSAGE, message);
                long values = db.insert(database.TABLENAME, null, contentValues);
                Log.d("TELIVER::", "NUMBER OF VALUES INSERTED ==  " + values);
            }
            refreshData();
            adapterTrackingId.notifyDataSetChanged();
        }
    };

    private void refreshData() {
        if (listTrackingId != null)
            listTrackingId.clear();
        Database database = new Database(this);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        String query = "SELECT * FROM " + database.TABLENAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            for (int j = 0; j < cursor.getCount(); j++) {
                String trackingId = cursor.getString(cursor.getColumnIndex(database.TRACKING_ID));
                listTrackingId.add(new Model(trackingId));
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (!listTrackingId.isEmpty())
            txtNoData.setVisibility(View.GONE);
        else txtNoData.setVisibility(View.VISIBLE);
        Log.d("TELIVER::", "THE SIZE OF ARRAYLIST ==  " + listTrackingId.size());
    }


    @Override
    protected void onResume() {
        refreshData();
        adapterTrackingId.setData(listTrackingId);
        adapterTrackingId.notifyDataSetChanged();
        if (listTrackingId.isEmpty())
            txtNoData.setVisibility(View.VISIBLE);
        super.onResume();
    }
}