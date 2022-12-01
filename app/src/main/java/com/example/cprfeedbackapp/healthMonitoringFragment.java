package com.example.cprfeedbackapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link healthMonitoringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class healthMonitoringFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public healthMonitoringFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment healthMonitoringFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static healthMonitoringFragment newInstance(String param1, String param2) {
        healthMonitoringFragment fragment = new healthMonitoringFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_health_monitoring, container, false);

            final MediaPlayer[] mp = {MediaPlayer.create(getActivity(), R.raw.beep)};
            Button playButton = (Button) fragmentView.findViewById(R.id.play_button);

            //Timer for the interval to play the beep at around 120bpm

        final Timer[] timer = {new Timer("MetronomeTimer", true)};
            final TimerTask[] tone = {new TimerTask() {
                @Override
                public void run() {
                    //Play sound
                    mp[0].start();
                }
            }};

            //play button
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mp[0] == null){
                        mp[0] = MediaPlayer.create(getActivity(), R.raw.beep);
                        tone[0] = new TimerTask() {
                            @Override
                            public void run() {
                                mp[0].start();
                            }
                        };
                        /*
                        mp[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                if(mp[0] != null) {
                                    mp[0].release();
                                    mp[0] = null;
                                    tone[0].cancel();
                                    timer[0].cancel();
                                }
                            }
                        });
                        */
                   }

                    //mp[0].setLooping(true);
                    //mp[0].start();
                   timer[0].scheduleAtFixedRate(tone[0], 500, 500); //120 BPM. Executes every 500 ms.
                    playButton.setEnabled(false);
                   // handler.postDelayed(runnable, 500);

                }
            });

            //stop button
            Button stopButton = (Button) fragmentView.findViewById(R.id.stop_button);
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mp[0] != null) {
                        mp[0].stop();
                        mp[0].reset();
                        mp[0].release();
                        mp[0] = null;
                        Toast.makeText(getActivity(), "Sound Stop Playing", Toast.LENGTH_SHORT).show();
                        playButton.setEnabled(true);
                        tone[0].cancel();
                        timer[0].cancel();
                        timer[0] = new Timer("MetronomeTimer", true);

                        //should stop repeat
                      //  handler.removeCallbacks(runnable);
                    }
                }
            });


        return fragmentView;

    }


  //  MediaPlayer[] mp = {MediaPlayer.create(getActivity(), R.raw.beep)};

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //Play sound
           // mp[0].start();

            //should make sound replay at 500ms = 120bpm
            //handler.postDelayed(this, 500);
        }
    };
}

