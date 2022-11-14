package com.example.cprfeedbackapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cprfeedbackapp.database.AppDatabase;
import com.example.cprfeedbackapp.database.entity.Sensor;
import com.example.cprfeedbackapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    protected AppDatabase db;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        //setting up play and stop sound button
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);
        Button playButton = (Button) this.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
            }
        });

         */

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        switchFragmentView(new HomeFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.Home:
                    switchFragmentView(new HomeFragment());
                    break;

                case R.id.GraphView:
                    switchFragmentView(new CprPerformanceFragment());
                    break;

                case R.id.Monitoring:
                    switchFragmentView(new healthMonitoringFragment());
                    break;

                case R.id.Tutorial:
                    switchFragmentView(new tutorialFragment());
                    break;

                case R.id.Settings:
                    switchFragmentView(new SettingsFragment());
                    break;
            }

            return true;
        });

        //test for database
        /*db = AppDatabase.getInstance(getApplicationContext());
         for(int i=0; i< 10; i++)
           db.sensorDAO().insertAll(new Sensor(0, "Sensor "+i, "Room "+i ));
          */
    }
    //end of create



    @Override
    protected void onResume() {
        super.onResume();


    }


    private void switchFragmentView (Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }



}





