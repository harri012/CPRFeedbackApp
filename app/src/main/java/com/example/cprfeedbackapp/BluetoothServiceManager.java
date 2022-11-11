package com.example.cprfeedbackapp;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BluetoothServiceManager {

    //Declaring data members for bluetooth connection
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice btDevice;
    private BluetoothSocket btSocket;
    private BluetoothServerSocket btServerSocket;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    private Context context;
    private Activity activity;
    private Handler handler;
    protected HashMap<String, String> previouslyConnectedDevicesInfo;
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private OutputStream outputStream;
    private InputStream inputStream;
    private String MY_UUID = "5cd11816-90c9-4883-9a7a-9cb5a1116568";

    // Create a Broadcast receiver for ACTION.FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDeviceList.add(device);

                Log.d(TAG, bluetoothDeviceList.toString());
            }
        }
    };

    public BluetoothServiceManager(Context aContext, Activity anActivity) {
        this.context = aContext;
        this.btManager = context.getSystemService(BluetoothManager.class);
        this.btAdapter = btManager.getAdapter();
        this.activity = anActivity;
    }

    //Checks if bluetooth is enabled
    //Returns true if bluetooth is now enabled else returns false
    public boolean checkBluetoothEnabled() {
        //Returns null if it doesn't support Bluetooth
        if (btAdapter == null) {
            msg("Device Does Not Support Bluetooth!");
            return false;
        } else {
            //Prompts with dialog to activate bluetooth
            if (!btAdapter.isEnabled()) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, RequestCode.BLUETOOTH_PERMISSION);
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    activity.startActivityForResult(enableBtIntent, RequestCode.IMPORT);
                    if (!btAdapter.isEnabled())
                        return false;
                    else
                        return true;
                }
            }
        }
        return true;
    }


    // Searches for nearby Bluetooth devices
    // Linked to a scan button
    public void discoverDevices() {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED)
            btAdapter.startDiscovery();

        // Register for broadcast when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(receiver, filter);
    }


    // Unregister the ACTION_FOUND receiver
    public void unregisterDevice() {
        context.unregisterReceiver(receiver);
    }


    //Checks devices that are already paired
    public void queryPairedDevice() {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED)

            // Gets all the previously connected Bluetooth devices
            // and add them to the list of bluetooth devices
            for (BluetoothDevice device : btAdapter.getBondedDevices())
                bluetoothDeviceList.add(device);
    }

    //Creates connection
    public void connectBluetoothDevice() {

        // Creates a new thread
        AcceptThread thread = new AcceptThread();
        thread.run();
    }

    // Checks if the android application is connected to the right hardware
    public boolean isThisTheDevice(BluetoothDevice device) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
            return device.getName().startsWith("");

        return false;
    }


    //Input Stream of Bluetooth Data
    public String getInputData(){

        ConnectedThread connectedThread = new ConnectedThread(btSocket);
        connectedThread.run();
        return "";
    }



    //Request Code Class
    private class RequestCode{
        static final int BLUETOOTH_PERMISSION = 100;
        static final int IMPORT = 101;
    }

    //For toasts
    private void msg(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    // Returns the list of discovered devices
    public List<BluetoothDevice> getPairedDevices() {

        if (!bluetoothDeviceList.isEmpty()) {

            List<BluetoothDevice> deviceList = new ArrayList<>();

            for (BluetoothDevice device : bluetoothDeviceList)
                deviceList.add(device);

            return deviceList;
        }

        return new ArrayList<>();
    }

    // Helper class for Bluetooth connection
    // Will run on a separate thread
    private class AcceptThread extends Thread {

        // Get a BluetoothServerSocket object
        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try{
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                    tmp = btAdapter.listenUsingRfcommWithServiceRecord("CPR Feedback", UUID.fromString(MY_UUID));
            }

            catch (IOException e) {
                msg("Socket's listen() method failed.");
            }

            btServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;

            while (true)
            {
                // Start listening for connection requests
                // Returns when either a connection has been accepted or an exception has occurred
                try {
                    socket = btServerSocket.accept();
                }

                catch(IOException e) {
                    msg("Socket's accept() method failed.");
                }

                // If the android application is connected to the correct device
                if (socket.isConnected() && isThisTheDevice(socket.getRemoteDevice())) {

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                        Log.d(TAG, socket.getRemoteDevice().getName());

                    //TODO: Perform work associated with the connection in a separate thread



                    cancel();
                    break;
                }
            }
        }

        // Cancels all attempts to a connection except the one that is currently connected
        public void cancel() {
            try {
                btServerSocket.close();
            }

            catch(IOException e) {
                msg("could not close the connect socket.");
            }
        }
    }


    // Defines several constants used when transmitting messages between the service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(MessageConstants.MESSAGE_READ, numBytes, -1, mmBuffer);
                    readMsg.sendToTarget();
                    final String string = new String(mmBuffer,"UTF-8");
                    Log.i("Bt Service Manager",string);

                }
                catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            }
            catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg = handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast", "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}




