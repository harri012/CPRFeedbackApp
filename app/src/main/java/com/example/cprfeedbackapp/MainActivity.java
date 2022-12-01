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

        setupUI(savedInstanceState);

        //Checks for bluetooth status
        BluetoothServiceManager bt = new BluetoothServiceManager(this, this);
        bt.checkBluetoothEnabled();
    }


    private void switchFragmentView (Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }


    private void setupUI(Bundle savedInstanceState)
    {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

                case R.id.Tutorial:
                    switchFragmentView(new TutorialFragment());
                    break;

                case R.id.Settings:
                    switchFragmentView(new SettingsFragment());
                    break;
            }

            return true;
        });

      if(savedInstanceState==null)
          //change to default as home
          binding.bottomNavigation.setSelectedItemId(R.id.Home);
    }
}





