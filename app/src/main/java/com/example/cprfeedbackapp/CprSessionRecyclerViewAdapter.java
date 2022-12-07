package com.example.cprfeedbackapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cprfeedbackapp.database.AppDatabase;
import com.example.cprfeedbackapp.database.entity.AverageDepthForce;
import com.example.cprfeedbackapp.database.entity.WaveformForce;

import java.util.ArrayList;
import java.util.List;

public class CprSessionRecyclerViewAdapter extends RecyclerView.Adapter<CprSessionRecyclerViewAdapter.ViewHolder> {

    private List<String> localDataSet;
    private List<AverageDepthForce> sessionAverageDatapoints = new ArrayList<>();
    private List<WaveformForce> sessionWaveformForce = new ArrayList<>();

    protected AppDatabase db;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView datetimeTextView;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            datetimeTextView = itemView.findViewById(R.id.datetimeTextView);
        }

        public TextView getDatetimeTextView() {return datetimeTextView;};
    }

    public CprSessionRecyclerViewAdapter(List<String> localDataSet) {
        this.localDataSet = localDataSet;
    }

    @NonNull
    @Override
    public CprSessionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpr_session_recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CprSessionRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.getDatetimeTextView().setText(localDataSet.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the position of the recycler view item that was clicked
                int pos = holder.getLayoutPosition();

                db = AppDatabase.getInstance(view.getContext());

                // Returns a list of CprSessionDatapoint of the session specified by the datetime
                sessionAverageDatapoints = db.averageDepthForceDao().getAverageDepthForceDatapoints(localDataSet.get(pos));
                sessionWaveformForce = db.waveformForceDao().getWaveformForceDatapoints(localDataSet.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    // Returns the list of AverageDepthForce objects
    public List<AverageDepthForce> getSessionAverageDatapoints() {
        return sessionAverageDatapoints;
    }

    // Returns the list of WaveformForce objects
    public List<WaveformForce> getSessionWaveformForce() {
        return sessionWaveformForce;
    }





}
