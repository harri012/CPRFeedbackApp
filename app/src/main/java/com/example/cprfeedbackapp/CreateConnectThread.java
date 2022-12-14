package com.example.cprfeedbackapp;

import static com.example.cprfeedbackapp.DataActivity.handler;

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
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    protected Context context;
    protected BluetoothSocket mmSocket;
    protected UUID uuid;
    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address, Context context) {

        this.context = context;

        //get address
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        Log.i("Bt Service Manager", "passing: "+ address);

        BluetoothSocket tmp = null;

        //check if permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            //Not used but doesn't work if removed!!!!!!!
            uuid = bluetoothDevice.getUuids()[0].getUuid();
            Log.i("Bt Service Manager", "Gets UUID: " + uuid);

            try {
                //Get a BluetoothSocket to connect with the given BluetoothDevice.
                tmp = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                //tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
                Log.i("Bt Service Manager", "Socket Failed");
            }
        }
        //Set socket to bluetooth device
        mmSocket = tmp;
    }

    //Run function
    public void run() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            /*bluetoothAdapter.cancelDiscovery();
            Log.i("Bt Service Manager", "Cancelled Discovery");*/
        }

        try {
            // Connect to the remote device through the socket. This call blocks until it succeeds or throws an exception.
            mmSocket.connect();
            //Send connection confirmation
            handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
        }
        catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
                Log.e("Bt Service Manager", "Cannot connect to device");
                handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        ConnectedThread connectedThread = new ConnectedThread(mmSocket, handler);
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
