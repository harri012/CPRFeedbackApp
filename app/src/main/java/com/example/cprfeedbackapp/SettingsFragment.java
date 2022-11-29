package com.example.cprfeedbackapp;

import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */



public class SettingsFragment extends Fragment {

    protected Switch darkModeSwitch;
    protected TextView recordTimeTextView;
    protected ImageButton increaseRecordTimeButton;
    protected ImageButton decreaseRecordTimeButton;

    protected SharedPreferencesHelper sharedPreferencesHelper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

        sharedPreferencesHelper = new SharedPreferencesHelper(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        recordTimeTextView = fragmentView.findViewById(R.id.recordTimeTextView);
        recordTimeTextView.setText(Integer.toString(sharedPreferencesHelper.getCurrentRecordTime()));

        // Link the switch to the variable
        darkModeSwitch = fragmentView.findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(sharedPreferencesHelper.getDarkModeState());

        // Getting the darkMode state from shared preferences
        boolean isNightModeOn = sharedPreferencesHelper.getDarkModeState();

        // OnClickListener for the switch
        darkModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNightModeOn) {

                    // Sets the app's theme to light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    // Saving the current theme to the shared preferences
                    sharedPreferencesHelper.saveDarkModeState(false);
                }

                else {

                    // Setting the app's theme to dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    // Saving the current theme to the shared preferences
                    sharedPreferencesHelper.saveDarkModeState(true);
                }
            }
        });

        // Buttons need to be styled
        // Font size and color as well
        // ...
        increaseRecordTimeButton = fragmentView.findViewById(R.id.increaseRecordTimeButton);
        increaseRecordTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTimeValue = Integer.parseInt(recordTimeTextView.getText().toString());

                //check if it will the maximum allowed time of recording
                int newRecordTime = currentTimeValue +15;

                if(newRecordTime < 150)
                    //Increase the value of record time
                    recordTimeTextView.setText(String.valueOf(currentTimeValue + 15));

                else {
                    //Set to a max value
                    recordTimeTextView.setText(Integer.toString(150));
                    Toast.makeText(SettingsFragment.this.getContext(), "Maximum Record Time Reached", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decreaseRecordTimeButton = fragmentView.findViewById(R.id.decreaserecordTimeButton);
        decreaseRecordTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTimeValue = Integer.parseInt(recordTimeTextView.getText().toString());

                // Checks if the decrease will result in a negative number
                int newRecordTime = currentTimeValue - 15;
                if(newRecordTime > 0)
                    recordTimeTextView.setText(String.valueOf(currentTimeValue - 15));

                else{
                    recordTimeTextView.setText(Integer.toString(0));
                    Toast.makeText(SettingsFragment.this.getContext(), "Time must be a positive value!", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return fragmentView;
    }
}