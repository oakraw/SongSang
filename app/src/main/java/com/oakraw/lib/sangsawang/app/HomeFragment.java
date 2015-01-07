package com.oakraw.lib.sangsawang.app;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cengalabs.flatui.FlatUI;

import java.util.List;

/**
 * Created by oakraw90 on 6/26/2014.
 */
public class HomeFragment extends Fragment {

    String TAG = "oakTag";
    private ToggleButton notiButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FlatUI.initDefaultValues(getActivity());
        FlatUI.setDefaultTheme(FlatUI.SEA);

        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        ToggleButton phoneButton = (ToggleButton) rootView.findViewById(R.id.phoneToggleButton);
        notiButton = (ToggleButton) rootView.findViewById(R.id.notiToggleButton);




        final SharedPreferences settings = MainActivity.context.getSharedPreferences("BroadcastReceiver", 0);

        boolean phoneBtnBool = settings.getBoolean("Phone", false);
        phoneButton.setChecked(phoneBtnBool);
        notiButton.setChecked(isAccessibilitySettingsOn(MainActivity.context));

        phoneButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    enableBroadcastReceiver();
                }
                else{
                    disableBroadcastReceiver();
                }
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Phone",isChecked);
                editor.commit();
            }
        });


        notiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiButton.setChecked(!notiButton.isChecked());
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        notiButton.setChecked(isAccessibilitySettingsOn(MainActivity.context));
    }

    public void enableBroadcastReceiver(){
//        ComponentName receiver = new ComponentName(MainActivity.context, CallReceiver.class);
//        PackageManager pm = MainActivity.context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
        //Toast.makeText(MainActivity.context, "Enabled broadcast receiver", Toast.LENGTH_SHORT).show();
        IntentFilter callInterceptorIntentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
        getActivity().registerReceiver(MainActivity.yourBR,  callInterceptorIntentFilter);
    }

    public void disableBroadcastReceiver(){
//        ComponentName receiver = new ComponentName(MainActivity.context, CallReceiver.class);
//        PackageManager pm = MainActivity.context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
       // Toast.makeText(MainActivity.context, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();

        getActivity().unregisterReceiver(MainActivity.yourBR);
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "com.oakraw.lib.sangsawang.app/com.oakraw.lib.sangsawang.NotificationService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILIY IS ENABLED*** -----------------");
            return true;
            /*String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    Log.v(TAG, "-------------- > accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }*/
        } else {
            Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }

}
