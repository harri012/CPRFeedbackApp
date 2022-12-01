package com.example.cprfeedbackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.cprfeedbackapp.database.AppDatabase;
import com.example.cprfeedbackapp.database.dao.CprSessionDao;
import com.example.cprfeedbackapp.database.entity.CprSessionDatapoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    protected Boolean boolOpenGraph = false;
    protected Boolean boolConnected = false;
    protected int nbRecordedData = 0;
    protected int dataSampleSize = 500; //1465 for 150 sec session since 0.1 per point
    protected ArrayList<String> listRecordedData = new ArrayList<>();
    protected ArrayList<Double> accRecordedData = new ArrayList<>();

    protected String forceData = "0";
    protected String accData = "0";
    protected String prevForceData = "0";
    protected String prevAccData = "0";

    // Time for progress bar
    protected CountDownTimer countDownTimer;

    //Database
    private AppDatabase appDatabase;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appDatabase = AppDatabase.getInstance(this);

        setupHandler();
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if(boolConnected)
            createConnectThread.cancel();
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
                                boolConnected = true;
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
                        dataSampleSize = sharedPreferencesHelper.getCurrentRecordTime() * 10;

                        forceData = prevForceData;
                        accData = prevAccData;
                        if(arduinoMsg.charAt(0) == 'f') {
                            forceData = arduinoMsg.substring(1);
                            prevForceData = forceData;

                            //If true record data until it reached dataSampleSize
                            if(boolRecordData == true && nbRecordedData <= dataSampleSize )
                            {
                                //Add value to list
                                listRecordedData.add(Integer.toString(Integer.parseInt(forceData) *10/1024));
                                if(nbRecordedData == dataSampleSize)
                                {
                                    //Set button back to record behaviour
                                    boolCancel = false;
                                    buttonRecordData.setText("Record Session");

                                    buttonRecordData.setEnabled(false);
                                    buttonSaveData.setEnabled(true);

                                    //Set back to false
                                    boolRecordData = false;
                                    buttonSaveData.setEnabled(true);
                                    recordingStatusTextView.setText("Recording Complete!");
                                    msg("Finished Recording Data");
                                }
                                else
                                    nbRecordedData++;
                            }
                        }
                        if(arduinoMsg.charAt(0) == 'a') {
                            accData = arduinoMsg.substring(1);
                            prevAccData = accData;
                        }

                        frequencyFeedback(Integer.parseInt(forceData), Double.parseDouble(accData));



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

        ProgressBar progressBar = findViewById(R.id.progressBar);

        buttonRecordData.setEnabled(false);
        buttonSaveData.setEnabled(false);
        deviceName = getIntent().getStringExtra("deviceName");
        buttonRecordData.setText("Record Session");
        buttonSaveData.setText("View Session");
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
                    createConnectThread = new CreateConnectThread(btAdapter, deviceAddress, getApplicationContext());
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

                    int count = 0;
                    progressBar.setProgress(count);
                    countDownTimer = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            progressBar.setProgress(count);
                        }

                        @Override
                        public void onFinish() {
                            progressBar.setProgress(100);
                        }
                    };
                    countDownTimer.start();


                    boolCancel = true;

                    //set to next state
                    boolOpenGraph = false;
                    buttonSaveData.setText("Save Session");
                    buttonSaveData.setEnabled(false);
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

                LocalDateTime dateTimeNow = LocalDateTime.now();
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd @ HH:mm:ss");
                String formattedDate = dateTimeNow.format(formatTime);

                for(int i=0; i< listRecordedData.size(); i++)
                {
                    appDatabase.cprSessionDao().insertSession(new CprSessionDatapoint(0, Double.parseDouble(listRecordedData.get(i)), 2.00, formattedDate));
                }

                //appDatabase

                //buttonRecordData.setEnabled(true);
                buttonConnect.setEnabled(true);
                nbRecordedData = 0;
                listRecordedData.clear();
                recordingStatusTextView.setText("Recording Saved");
                msg("Saved Data");

                //Set button back to record behaviour
                boolCancel = false;
                buttonRecordData.setText("Record Session");
                buttonSaveData.setEnabled(false);


                Intent send = new Intent(DataActivity.this, LiveDataGraph.class);
                startActivity(send);

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
    private double lowerFrequency = 1.5;     //90 compression per minute
    private double higherFrequency = 2.33333;       //140 compression per minute

    private double maxForce = 0;
    private double tempForce = 0;
    private double lowerForce= 9;
    private double higherForce = 2000;

    protected double depth = 0;
    private int lowerDepth= 3; //in cm
    private int higherDepth = 7;
    private double timeCPR = 0;
    private double frequency = 0;


    private void frequencyFeedback(int forceData, double accData){
        double nbDataPoint = frequencyCalculator(forceData);

        tempForce = (forceData*10)/1024;
        if (tempForce > maxForce)
            maxForce = tempForce;

        accData = (accData - 9.8)*100;
        accRecordedData.add(accData);

        if(nbDataPoint != -1)
        {
            timeCPR = nbDataPoint *timePerDataPoint;
            //for frequency
            frequency = 2/(nbDataPoint * timePerDataPoint);

            if(frequency < lowerFrequency)
            {
                frequencyComment.setText("Too Slow");
                frequencyComment.setTextColor(Color.parseColor("#FFFF0000"));
            }
            else if(frequency > higherFrequency)
            {
                frequencyComment.setText("Too Fast");
                frequencyComment.setTextColor(Color.parseColor("#FFFF0000"));
            }
            else
            {
                frequencyComment.setText("Good!");
                frequencyComment.setTextColor(Color.parseColor("#00FF00"));
            }
            frequencyTextView.setText(String.format("%.2f",frequency) + " hz");

            //for force
            if(maxForce < lowerForce)
            {
                forceComment.setText("Too Weak");
                forceComment.setTextColor(Color.parseColor("#FFFF0000"));
            }
            else if(maxForce > higherForce)
            {
                forceComment.setText("Too Strong");
                forceComment.setTextColor(Color.parseColor("#FFFF0000"));
            }
            else
            {
                forceComment.setText("Good!");
                forceComment.setTextColor(Color.parseColor("#00FF00"));
            }
            forceTextView.setText(String.format("%.2f", maxForce) + " N");

            maxForce = 0;

            //Python Integration Test
            if (! Python.isStarted()) {
                Python.start(new AndroidPlatform(this));
            }

            Python py = Python.getInstance();

            PyObject pyobj = py.getModule("script");
            PyObject obj = pyobj.callAttr("displacementLive", accRecordedData.toArray(), timeCPR);
            depth = obj.toFloat() / 10;
            if(depth > 100)
                depth = 0;

            //for depth
            depthTexView.setText(String.format("%.2f",depth) + " cm");
            if(depth < lowerDepth)
            {
                depthComment.setText("Too Shallow");
                depthComment.setTextColor(Color.parseColor("#FFFF0000"));
            }
            else if(depth > higherDepth)
            {
                depthComment.setText("Too Deep");
                depthComment.setTextColor(Color.parseColor("#FFFF0000"));

            }
            else
            {
                depthComment.setText("Good");
                depthComment.setTextColor(Color.parseColor("#00FF00"));
            }

            depth = 0;
            accRecordedData.clear();
        }
    }




    //For toasts
    private void msg(String str){
        Toast.makeText(DataActivity.this, str, Toast.LENGTH_LONG).show();
    }


}