package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    //Declaring UI Widgets
    protected TextView forceTextView, depthTexView, frequencyTextView, nameTextView, connectionStatusTextView;
    protected TextView forceComment, depthComment, frequencyComment, recordingStatusTextView;
    protected Button buttonConnect, buttonRecordData, buttonSaveData;

    //Declaring Bluetooth Variables
    private String deviceName = null;
    private String deviceAddress;
    private String arduinoMsg = "0";
    public static Handler handler;

    //Declaring Data Variables
    protected Boolean boolRecordData = false;
    protected Boolean boolCancel = false;
    protected Boolean boolStopRecording = false;
    protected int nbRecordedData = 0;
    protected int dataSampleSize = 500; //1465 for 150 sec session since 0.1 per point
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
                        if(boolRecordData == true && nbRecordedData <= dataSampleSize )
                        {

                            //Add value to list
                            listRecordedData.add(arduinoMsg);
                            if(nbRecordedData == dataSampleSize)
                            {
                                //Set back to false
                                boolRecordData = false;
                                buttonSaveData.setEnabled(true);
                                recordingStatusTextView.setText("Recording Complete!");
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
        nameTextView = findViewById(R.id.textViewDeviceName);
        forceTextView = findViewById(R.id.textViewForceData);
        depthTexView = findViewById(R.id.textViewDepthData);
        frequencyTextView = findViewById(R.id.textViewFrequencyData);
        forceComment = findViewById(R.id.textViewForceComment);
        depthComment = findViewById(R.id.textViewDepthComment);
        frequencyComment = findViewById(R.id.textViewFrequencyComment);
        recordingStatusTextView = findViewById(R.id.textViewRecordingStatus);

        buttonConnect = findViewById(R.id.buttonConnect);
        buttonRecordData = findViewById(R.id.buttonRecordData);
        buttonSaveData = findViewById(R.id.buttonSendData);
        connectionStatusTextView = findViewById(R.id.textViewConnectionStatus);


        buttonRecordData.setEnabled(false);
        buttonSaveData.setEnabled(false);
        deviceName = getIntent().getStringExtra("deviceName");
        buttonRecordData.setText("Record Session");
        nameTextView.setText(deviceName);

        //Data TextView
        depthTexView.setText("0");
        forceTextView.setText("0");
        frequencyTextView.setText("0");

        //Comment TextView
        depthComment.setText("Feedback");
        forceComment.setText("Feedback");
        frequencyComment.setText("Feedback");
        recordingStatusTextView.setText(" ");

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
                if(boolCancel == false) {
                    boolRecordData = true;
                    buttonRecordData.setText("Cancel");
                    recordingStatusTextView.setText("Recording...");
                    msg("Recording Data");
                    boolCancel = true;
                }
                else
                {
                    //Stop Recording
                    boolRecordData = false;
                    //Set button back to record behaviour
                    boolCancel = false;
                    buttonRecordData.setText("Record Session");

                    buttonRecordData.setEnabled(false);
                    buttonSaveData.setEnabled(true);
                    recordingStatusTextView.setText("Recording Cancelled");
                    msg("Stopped Recording!");
                }
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
                recordingStatusTextView.setText("Recording Saved");

                msg("Saved Data");

            }
        });
    }

    protected int amountDataPoint = 0;
    private int previousData;
    private Integer frequencyCalculator(int aData)
    {
        int returnedVal = -1;
        if(aData == 0 && previousData != 0) {
            returnedVal = amountDataPoint;
            amountDataPoint = 0;
        }
        else
            amountDataPoint++;

        previousData = aData;
        return returnedVal;
    }


    private double timePerDataPoint = 0.1;
    private int lowerFrequency = 2;
    private int higherFrequency = 2;

    private void frequencyFeedback(int aData){
        double frequency = frequencyCalculator(aData);
        if(frequency != -1)
        {
            frequency = frequency * timePerDataPoint;

            if(frequency < lowerFrequency)
            {
                frequencyComment.setText("Too Slow");
            }
            if(frequency > higherFrequency)
            {
                frequencyComment.setText("Too Fast");
            }
            else
            {
                frequencyComment.setText("Good!");
            }
        }
    }

    private int maxForce = 0;
    private int lowerForce= 2;
    private int higherForce = 2;
    private void forceFeedback(int aData)
    {
        int frequency = frequencyCalculator(aData);
        if (aData > maxForce)
            maxForce = aData;
        if(frequency != -1)
        {
            if(maxForce < lowerForce)
            {
                forceComment.setText("Too Weak");
            }
            if(maxForce > higherForce)
            {
                forceComment.setText("Too Strong");
            }
            else
            {
                forceComment.setText("Good!");
            }
            maxForce = 0;
        }

    }
    protected int maxDepth = 0;
    private int lowerDepth= 2;
    private int higherDepth = 2;
    private void depthFeedback(int aData)
    {
        int frequency = frequencyCalculator(aData);
        if (aData > maxDepth)
            maxDepth = aData;
        if(frequency != -1)
        {
            if(maxDepth < lowerDepth)
            {
                depthComment.setText("Too Shallow");
            }
            if(maxDepth > higherDepth)
            {
                depthComment.setText("Too Deep");
            }
            else
            {
                depthComment.setText("Good");
            }
            maxDepth = 0;
        }
    }

    //For toasts
    private void msg(String str) {
        Toast.makeText(DataActivity.this, str, Toast.LENGTH_LONG).show();
    }


}