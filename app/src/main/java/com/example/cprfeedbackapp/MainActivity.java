package com.example.cprfeedbackapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.cprfeedbackapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        Bluetooth bt = new Bluetooth(this, this);
        bt.checkBluetoothEnabled();
    }


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





