package com.example.cprfeedbackapp;

import static com.example.cprfeedbackapp.DataActivity.handler;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

interface MessageConstants {
    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_TOAST = 2;
}


public class ConnectedThread implements MessageConstants {

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    protected final Handler handler;

    private static final String TAG = "MY_APP_DEBUG_TAG";

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.handler = handler;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
            Log.e("Bt Service Manager", "Created input stream");

        } catch (IOException e) {
            Log.e("Bt Service Manager", "Error occurred when creating input stream");
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e("Bt Service Manager", "Error occurred when creating output stream");
        }

        mmInStream = tmpIn;

        if (mmInStream == null)
            Log.e("Bt Service Manager", "Null input Stream");

        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] mmBuffer = new byte[1024];
        int numBytes = 0; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {

                mmBuffer[numBytes] = (byte) mmInStream.read();
                String readMessage;
                if (mmBuffer[numBytes] == '\n') {
                    readMessage = new String(mmBuffer, 0, numBytes);
                    if (handler == null)
                        Log.e("Bt Service Manager", "no handler");
                    handler.obtainMessage(MESSAGE_READ, readMessage).sendToTarget();
                    numBytes = 0;
                } else {
                    numBytes++;
                }
            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    /*// Call this from the main activity to send data to the remote device.
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
    }*/

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
