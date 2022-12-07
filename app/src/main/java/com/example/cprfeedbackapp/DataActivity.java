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
import com.example.cprfeedbackapp.database.dao.AverageDepthForceDao;
import com.example.cprfeedbackapp.database.entity.AverageDepthForce;
import com.example.cprfeedbackapp.database.entity.WaveformForce;

import java.sql.Array;
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

    // Time for progress bar
    protected CountDownTimer countDownTimer;

    // Datetime for Database
    protected String formattedDate;

    // Time List for Plots
    ArrayList<Double> timeSpent = new ArrayList<>();

    //Database
    private AppDatabase appDatabase;

    //Declaring Threads
    public ConnectedThread connectedThread;
    public CreateConnectThread createConnectThread;

    protected SharedPreferencesHelper sharedPreferencesHelper;

    //Declaring Constants
    private final static int MESSAGE_READ = 0; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status

    //Declaring cpr values
    protected String forceData = "0";
    protected String accData = "0";
    protected String prevForceData = "0";
    protected String prevAccData = "0";

    private double timePerDataPoint = 0.1;

    //Frequency
    private double lowerFrequency = 1.5;     //90 compression per minute
    private double higherFrequency = 2.33333;       //140 compression per minute
    //Force
    private double maxForce = 0;
    private double tempForce = 0;
    private double lowerForce= 9;
    private double higherForce = 2000;
    //Depth
    protected double depth = 0;
    private int lowerDepth= 3; //in cm
    private int higherDepth = 7;
    private double timeCPR = 0;
    private double frequency = 0;

    //Boolean for feedback
    protected Boolean goodDepth =false;
    protected Boolean goodForce = false;
    protected Boolean goodFrequency = false;

    protected int nbCPR = 0;
    protected int nbGoodCPR = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        //Sharedpreferences declaration
        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        //Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appDatabase = AppDatabase.getInstance(this);

        //Setting app database
        LocalDateTime dateTimeNow = LocalDateTime.now();
        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd @ HH:mm:ss");
        formattedDate = dateTimeNow.format(formatTime);

        //Setup handler and ui
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

        //Cancels connection on pause if connected
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

                        //If first char is f its force data
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

                                    //save nb of cpr and good ones
                                    sharedPreferencesHelper.saveCprNb(nbCPR);
                                    sharedPreferencesHelper.saveGoodCprNb(nbGoodCPR);

                                    //reset value
                                    nbCPR = 0;
                                    nbGoodCPR = 0;
                                }
                                else
                                    nbRecordedData++;
                            }
                        }

                        //if first char is a its acceleration data
                        if(arduinoMsg.charAt(0) == 'a') {
                            accData = arduinoMsg.substring(1);
                            prevAccData = accData;
                        }

                        //Function used to manage feedback
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
        connectionStatusTextView = findViewById(R.id.textViewConnectionStatus);

        //set buttons
        buttonConnect = findViewById(R.id.buttonConnect);
        buttonRecordData = findViewById(R.id.buttonRecordData);
        buttonSaveData = findViewById(R.id.buttonSendData);

        //Setup progress bar
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //Setup bool
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


                    //Progress bar
                    int count = 0;
                    int timer = sharedPreferencesHelper.getCurrentRecordTime() * 1000;
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(count);
                    countDownTimer = new CountDownTimer(timer, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //Counts till set amount time
                            long finishedSeconds = timer - millisUntilFinished;
                            int total = (int) (((float)finishedSeconds / (float)timer) * 100.0);
                            progressBar.setProgress(total);
                        }

                        @Override
                        public void onFinish() {
                            progressBar.setProgress(100);
                        }
                    };
                    countDownTimer.start();


                    //Enables cancel
                    boolCancel = true;

                    //set to next state
                    boolOpenGraph = false;
                    buttonSaveData.setEnabled(false);


                }
                else
                {
                    //Stop Recording
                    boolRecordData = false;
                    //Set button back to record behaviour
                    boolCancel = false;
                    buttonRecordData.setText("Record Session");

                    //Disable progress bar
                    progressBar.setProgress(0);
                    countDownTimer.cancel();
                    progressBar.setVisibility(View.GONE);


                    //Save cpr number if canceled
                    sharedPreferencesHelper.saveCprNb(nbCPR);
                    sharedPreferencesHelper.saveGoodCprNb(nbGoodCPR);

                    //Reset
                    nbCPR = 0;
                    nbGoodCPR = 0;

                    //Reset regular behaviour
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

                // Saves all values of the force in the database
                for (String value : listRecordedData) {
                    appDatabase.waveformForceDao().insertWaveformForce(new WaveformForce(0, Double.parseDouble(value), formattedDate));
                }


                //appDatabase
//
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
                recordingStatusTextView.setText(" ");

                //Cancels progress bar
                progressBar.setProgress(0);
                countDownTimer.cancel();
                progressBar.setVisibility(View.GONE);
                connectionStatusTextView.setText("Connection Status: Not Connected");


                //Bring to live data graph activity
                Intent send = new Intent(DataActivity.this, LiveDataGraph.class);
                startActivity(send);

            }
        });
    }

    protected int amountDataPoint = 0;
    private int previousData;
    //Counts amount of data points until data point is 0 and previous isn't
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


    //Wait for frequency calculator to indicate end of compression and give feedback
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
                goodFrequency = true;
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
                goodForce = true;
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
                goodDepth = true;
            }

            // Saves average depth and force
            appDatabase.averageDepthForceDao().insertAverageDepthForce(new AverageDepthForce(0, maxForce, depth, formattedDate));

            if(boolRecordData) {
                nbCPR++;
                if(goodDepth && goodForce && goodFrequency) {
                    nbGoodCPR++;
                    goodDepth = false;
                    goodForce = false;
                    goodFrequency = false;
                }
            }

            maxForce = 0;
            depth = 0;
            accRecordedData.clear();
        }
    }


    //For toasts
    private void msg(String str){
        Toast.makeText(DataActivity.this, str, Toast.LENGTH_LONG).show();
    }


}