package com.telivercustomer;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.core.TrackingListener;
import com.teliver.sdk.models.MarkerOption;
import com.teliver.sdk.models.TLocation;
import com.teliver.sdk.models.TrackingBuilder;

import java.util.ArrayList;


public class AdapterTrackingId extends RecyclerView.Adapter<AdapterTrackingId.MyViewHolder> {

    private Activity context;

    private ArrayList<Model> listTrackingId;

    public AdapterTrackingId(Activity context) {
        this.context =context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpater_tracking_id,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Model model = listTrackingId.get(position);
        holder.txtTrackingId.setText(model.getTrackingId());

        holder.crdTrackingId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackingBuilder builder = new TrackingBuilder(new MarkerOption(holder.txtTrackingId.getText().toString().trim()))
                        .withListener(new TrackingListener() {
                            @Override
                            public void onTrackingStarted(String trackingId) {
                            }

                            @Override
                            public void onLocationUpdate(String trackingId, TLocation location) {
                            }

                            @Override
                            public void onTrackingEnded(String trackingId) {
                            }

                            @Override
                            public void onTrackingError(String reason) {
                            }
                        });
                Teliver.startTracking(builder.build());
            }

        });
    }

    @Override
    public int getItemCount() {
        return listTrackingId.size();
    }

    public void setData(ArrayList<Model> listTrackingId) {
        this.listTrackingId = listTrackingId;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTrackingId;

        private CardView crdTrackingId;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTrackingId = (TextView) itemView.findViewById(R.id.txtTrackingId);
            crdTrackingId = (CardView) itemView.findViewById(R.id.crdTrackingId);
        }
    }
}
