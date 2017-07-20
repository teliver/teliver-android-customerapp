package com.telivercustomer;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.core.TrackingListener;
import com.teliver.sdk.models.MarkerOption;
import com.teliver.sdk.models.TLocation;
import com.teliver.sdk.models.TrackingBuilder;

public class ActivityTracking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        TrackingBuilder builder = new TrackingBuilder(new MarkerOption(getIntent().getStringExtra(Application.TRACKING_ID)))
                .withListener(new TrackingListener() {
                    @Override
                    public void onTrackingStarted(String trackingId) {
                        Log.d("TELIVER::", "onTrackingStarted: " + trackingId);
                    }

                    @Override
                    public void onLocationUpdate(String trackingId, TLocation location) {
                        Log.d("TELIVER::", "onLocationUpdate: " + location.getLatitude() + location.getLongitude());
                    }

                    @Override
                    public void onTrackingEnded(String trackingId) {
                        Log.d("TELIVER::", "onTrackingEnded: " + trackingId);
                    }

                    @Override
                    public void onTrackingError(String reason) {
                        Log.d("TELIVER::", "onTrackingError: " + reason);
                    }
                });
        Teliver.startTracking(builder.build());
    }
}