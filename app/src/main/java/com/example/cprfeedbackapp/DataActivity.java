package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    //Declaring UI Widgets
    protected TextView forceTextView, depthTexView, nameTextView, connectionStatusTextView;
    protected Button buttonConnect, buttonRecordData, buttonSaveData;

    //Declaring Bluetooth Variables
    private String deviceName = null;
    private String deviceAddress;
    private String arduinoMsg = "0";
    public static Handler handler;

    //Declaring Data Variables
    protected Boolean boolRecordData = false;
    protected int nbRecordedData = 0;
    protected int dataSampleSize = 120;
    protected ArrayList<String> listRecordedData = new ArrayList<>();

    //Declaring Threads
    public ConnectedThread connectedThread;
    public CreateConnectThread createConnectThread;

    protected SharedPreferencesHelper sharedPreferencesHelper;

    //Declaring Constants
    private final static int MESSAGE_READ = 0; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        setupHandler();
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Missing ConnectedThread Code here.
    }



    public void setupHandler()
    {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                //If connected Properly
                                connectionStatusTextView.setText("Connection Status: Connected to " + deviceName);
                                buttonRecordData.setEnabled(true);
                                buttonConnect.setEnabled(false);
                                break;
                            case -1:
                                //Cant connect
                                connectionStatusTextView.setText("Connection Status: Device fails to connect");
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        // Read message from Arduino
                        arduinoMsg = msg.obj.toString();
                        forceTextView.setText(arduinoMsg);

                        //If true record data until it reached dataSampleSize
                        if(boolRecordData == true && nbRecordedData <= dataSampleSize)
                        {

                            //Add value to list
                            listRecordedData.add(arduinoMsg);
                            if(nbRecordedData == dataSampleSize)
                            {
                                //Set back to false
                                boolRecordData = false;
                                buttonSaveData.setEnabled(true);
                                msg("Finished Recording Data");
                            }
                            else
                                nbRecordedData++;
                        }
                        break;
                }
            }
        };
    }

    public void setup() {
        //Setting up UI widgets
        forceTextView = findViewById(R.id.forceDataTextView);
        depthTexView = findViewById(R.id.depthDataTextView);
        nameTextView = findViewById(R.id.textViewDeviceName);
        buttonConnect = findViewById(R.id.buttonConnect);
        buttonRecordData = findViewById(R.id.buttonRecordData);
        buttonSaveData = findViewById(R.id.buttonSendData);
        connectionStatusTextView = findViewById(R.id.textViewConnectionStatus);

        buttonRecordData.setEnabled(false);
        buttonSaveData.setEnabled(false);
        deviceName = getIntent().getStringExtra("deviceName");
        nameTextView.setText("Device Name: " + deviceName);
        depthTexView.setText("0");
        forceTextView.setText("0");

        //On Click for save connect button
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionStatusTextView.setText("Connecting...");
                msg("Connecting...");
                // If a bluetooth device has been selected from SelectDeviceActivity
                deviceName = getIntent().getStringExtra("deviceName");

                if (deviceName != null) {
                    // Get the device address to make BT Connection
                    deviceAddress = getIntent().getStringExtra("deviceAddress");

                    // Calls a new thread and creates a bluetooth connection to the selected device
                    BluetoothAdapter btAdapter = new BluetoothServiceManager(getApplicationContext(), DataActivity.this).getBtAdapter();
                    CreateConnectThread createConnectThread = new CreateConnectThread(btAdapter, deviceAddress, getApplicationContext());
                    createConnectThread.start();
                }
            }
        });

        //On Click for save record data button
        buttonRecordData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolRecordData = true;
                buttonRecordData.setEnabled(false);
                msg("Recording Data");
            }
        });

        //On Click for save data button
        buttonSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesHelper.saveEventSettings(listRecordedData, listRecordedData.size());
                buttonSaveData.setEnabled(false);
                buttonRecordData.setEnabled(true);
                nbRecordedData = 0;
                listRecordedData.clear();
                msg("Saved Data");

            }
        });
    }




    //For toasts
    private void msg(String str) {
        Toast.makeText(DataActivity.this, str, Toast.LENGTH_LONG).show();
    }


}