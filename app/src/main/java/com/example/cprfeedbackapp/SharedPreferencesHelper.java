package com.example.cprfeedbackapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class SharedPreferencesHelper {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencesHelper(Context context)
    {
        //Opening SharedPreferences file
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(this.context.getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
    }

    // Gets the event names that the user set in the Settings activity
    public ArrayList<String> getEventSettingNames()
    {
        ArrayList<String> eventNames = new ArrayList<>();

        int size = this.sharedPreferences.getInt(this.context.getString(R.string.EventNameStatus_size), 0);

        for(int i=0;i<size;i++)
        {
            eventNames.add(this.sharedPreferences.getString(this.context.getString(R.string.EventNameStatus_) + i, null));
        }

        return eventNames;
    }

    //Saves the event names and max count
    public void saveEventSettings(ArrayList<String> names, int max)
    {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();

        editor.putInt(this.context.getString(R.string.EventNameStatus_size), names.size());

        for(int i=0;i<names.size();i++)
        {
            editor.remove(this.context.getString(R.string.EventNameStatus_) + i);
            editor.putString(this.context.getString(R.string.EventNameStatus_) + i, names.get(i));
        }

        editor.putInt(this.context.getString(R.string.SharedPreferences_MaxCount), max);
        editor.apply();

    }
}
