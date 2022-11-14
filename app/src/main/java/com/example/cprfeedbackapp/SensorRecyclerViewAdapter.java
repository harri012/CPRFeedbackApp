package com.example.cprfeedbackapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cprfeedbackapp.database.entity.Sensor;

import java.util.List;


public class SensorRecyclerViewAdapter extends RecyclerView.Adapter<SensorRecyclerViewAdapter.ViewHolder> {

    private List <Sensor> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView sensorValueTextView;
        private TextView sensorNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sensorValueTextView = itemView.findViewById(R.id.sensorValueTextView);
            sensorNameTextView = itemView.findViewById(R.id.sensorNameTextView);
        }

        //getter functions
       public TextView getSensorValueTextView(){
            return sensorValueTextView;
       }

       public TextView getSensorNameTextView() {
            return sensorNameTextView;
        }
    }

    public SensorRecyclerViewAdapter(List<Sensor> localDataSet) {
        this.localDataSet = localDataSet;
    }

    @NonNull
    @Override
    //displayed the recycle view layout
    public SensorRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_recycleview_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    //Display the actual item view of the recycler view like the text
    public void onBindViewHolder(@NonNull SensorRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.getSensorNameTextView().setText(localDataSet.get(position).sensorName);
        holder.getSensorValueTextView().setText(localDataSet.get(position).sensorValue);
    }

    //length of the list to be displayed
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
