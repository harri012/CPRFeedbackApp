package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class dataActivity extends AppCompatActivity {
    protected TextView forceTextView, depthTexView, nameTextView;
    protected Button buttonConnect;

    private String deviceName = null;
    private String deviceAddress;
    private String arduinoMsg = "0";

    public static Handler handler;

    public ConnectedThread connectedThread;
    public CreateConnectThread createConnectThread;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("Bt Service Manager", "Inside Fragment");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);



        Log.i("Bt Service Manager", "Before handler");
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                nameTextView.setText("Connected to " + deviceName);
    /*                          progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                buttonToggle.setEnabled(true);*/
                                Log.i("Bt Service Manager", "1");

                                break;
                            case -1:
                                nameTextView.setText("Device fails to connect");
                                //progressBar.setVisibility(View.GONE);
                                //buttonConnect.setEnabled(true);
                                Log.i("Bt Service Manager", "-1");

                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        Log.i("Bt Service Manager", arduinoMsg);

                        forceTextView.setText(arduinoMsg);

                        break;
                }
            }
        };


        setup();

        /*Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!interrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                forceTextView.setText(arduinoMsg);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Missing ConnectedThread Code here.
    }

    public void setup() {
        forceTextView = findViewById(R.id.forceDataTextView);
        depthTexView = findViewById(R.id.depthDataTextView);
        nameTextView = findViewById(R.id.textViewDeviceName);
        buttonConnect = findViewById(R.id.buttonConnect);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If a bluetooth device has been selected from SelectDeviceActivity
                deviceName = getIntent().getStringExtra("deviceName");
                if (deviceName != null) {
                    // Get the device address to make BT Connection
                    deviceAddress = getIntent().getStringExtra("deviceAddress");


                    // Calls a new thread and creates a bluetooth connection to the selected device
                    BluetoothAdapter btAdapter = new BluetoothServiceManager(getApplicationContext(), dataActivity.this).getBtAdapter();
                    CreateConnectThread createConnectThread = new CreateConnectThread(btAdapter, deviceAddress, getApplicationContext());
                    createConnectThread.start();

                }
            }
        });
    }

    //For toasts
    private void msg(String str) {
        Toast.makeText(dataActivity.this, str, Toast.LENGTH_LONG).show();
    }
}