package com.example.cprfeedbackapp;

import android.Manifest;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Set;

public class DeviceRecyclerViewAdapter extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>
{

    private List<BluetoothDevice> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView deviceNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceNameTextView = itemView.findViewById(R.id.btDeviceName);

        }

        public TextView getDeviceNameTextView() {
            return deviceNameTextView;
        }
    }

    public DeviceRecyclerViewAdapter(Set<BluetoothDevice> localDataSet) {
        this.localDataSet.addAll(localDataSet);
    }

    @NonNull
    @Override
    public DeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_device_recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceRecyclerViewAdapter.ViewHolder holder, int position) {

        if (ActivityCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            holder.getDeviceNameTextView().setText(localDataSet.get(position).getName());
        }
    }

    @Override
    public int getItemCount ()
    {
        return localDataSet.size();
    }
}
