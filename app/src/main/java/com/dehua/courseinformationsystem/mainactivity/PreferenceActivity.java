package com.dehua.courseinformationsystem.mainactivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.Service.NotificationService;
import com.dehua.courseinformationsystem.utils.PollingUtils;

/**
 * Created by dehua on 16/4/28 028.
 */
public class PreferenceActivity extends AppCompatPreferenceActivity{
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TimePicker time;
    private SwitchPreference pref_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_data_sync);

        Preference pref_time=findPreference("pref_time");
        pref_time.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showTimeDialog();
                return false;
            }
        });

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();

        pref_notification= (SwitchPreference) findPreference("pref_notification");
        pref_notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Log.i("PreferenceActivity","change");
                PollingUtils.stopPollingService(MainActivity.getInstance(), NotificationService.class, NotificationService.ACTION);
                boolean pref_notification_get=sharedPreferences.getBoolean("pref_notification",true);
                if(!pref_notification_get) {
                    editor.putBoolean("pref_notification",true);
                    editor.commit();
                    pref_notification.setChecked(true);
                    PollingUtils.startPollingService(MainActivity.getInstance(), 5, NotificationService.class, NotificationService.ACTION);
                    Log.i("PreferenceActivity","polling service start");
                }else {
                    editor.putBoolean("pref_notification",false);
                    editor.commit();
                    pref_notification.setChecked(false);
                    Log.i("PreferenceActivity","polling service stop");
                }
                return false;
            }
        });
    }

    private void showTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = PreferenceActivity.this.getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.pref_time_dialog, null);
        time= (TimePicker) dialogView.findViewById(R.id.pref_timePicker);
        time.setIs24HourView(true);
        int hour=sharedPreferences.getInt("pref_time_hour",22);
        int min=sharedPreferences.getInt("pref_time_min",0);
        time.setCurrentHour(hour);
        time.setCurrentMinute(min);
        builder.setView(dialogView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int hour=time.getCurrentHour();
                        int min=time.getCurrentMinute();
                        editor.putInt("pref_time_hour",hour);
                        editor.putInt("pref_time_min",min);
                        editor.commit();
                        boolean pref_notification=sharedPreferences.getBoolean("pref_notification",true);
                        if(pref_notification) {
                            PollingUtils.stopPollingService(MainActivity.getInstance(), NotificationService.class, NotificationService.ACTION);
                            PollingUtils.startPollingService(MainActivity.getInstance(), 5, NotificationService.class, NotificationService.ACTION);
                            Log.i("PreferenceActivity","polling service restart");
                        }
                        dialog.cancel();
                   }
                })
                .setTitle("");
        builder.create().show();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
