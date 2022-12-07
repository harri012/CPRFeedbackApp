package com.example.cprfeedbackapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceRecyclerViewAdapter extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>
{

    protected List<DeviceInfoModel> localDataSet;
    protected Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView deviceNameTextView;
        private TextView deviceAddressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceNameTextView = itemView.findViewById(R.id.btDeviceName);
            deviceAddressTextView = itemView.findViewById(R.id.btDeviceAddress);

        }

        public TextView getDeviceNameTextView() {
            return deviceNameTextView;
        }
        public TextView getDeviceAddressTextView() {
            return deviceAddressTextView;
        }
    }

    public DeviceRecyclerViewAdapter(List<DeviceInfoModel> localDataSet, Context context) {
        this.localDataSet = localDataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public DeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_device_recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceRecyclerViewAdapter.ViewHolder holder, int position) {

        // Get the deviceInfoModel object that corresponds to the item that was clicked
        DeviceInfoModel deviceInfoModel = (DeviceInfoModel) localDataSet.get(position);

        holder.deviceNameTextView.setText(deviceInfoModel.getDeviceName());
        holder.deviceAddressTextView.setText(deviceInfoModel.getDeviceHardwareAddress());


        // When a device is selected
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gets position of the selected device
                int position = holder.getLayoutPosition();

                // Call dataActivity
                Intent intent = new Intent(context, DataActivity.class);
                intent.putExtra("deviceName", localDataSet.get(position).getDeviceName());
                intent.putExtra("deviceAddress",localDataSet.get(position).getDeviceHardwareAddress());

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount ()
    {
        return localDataSet.size();
    }


}
