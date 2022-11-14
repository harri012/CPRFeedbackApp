package com.example.cprfeedbackapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.UUID;

public class CreateConnectThread extends Thread {

    private static final String TAG = "MY_APP_DEBUG_TAG";
    protected Context context;
    protected BluetoothSocket mmSocket;
    protected UUID uuid;


    public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address, Context context) {

        this.context = context;

        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        BluetoothSocket tmp = null;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            uuid = bluetoothDevice.getUuids()[0].getUuid();
        }

        try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
            tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter.cancelDiscovery();
        }

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
            Log.e("Status", "Device connected");
//            handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
                Log.e("Status", "Cannot connect to device");
//                handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        connectedThread.run();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

}
