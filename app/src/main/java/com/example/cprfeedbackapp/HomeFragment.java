package com.example.cprfeedbackapp;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        //setting up play and stop sound button
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);

        //play button
        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.beep);
        Button playButton = (Button) rootView.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mp.setLooping(true);
                mp.start();
            }
        });

        //stop button
        Button stopButton = (Button) rootView.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.release();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

/*
Mediaplayer sound;
    public void playSound (View v){
        if (sound == null){
            sound = MediaPlayer.create(getActivity(), R.raw.beep);
            sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stop();
                }
            });
        }
        sound.setLooping(true);
        sound.start();
    }

    public  void stopSound (View v){
        stop();
    }

    private void stop() {
        if(sound != null) {
            sound.release();
            sound = null;
            Toast.makeText(getActivity(), "Sound Stop Playing", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        stop();
    }

 */

}



