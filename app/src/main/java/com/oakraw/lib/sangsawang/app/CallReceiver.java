package com.oakraw.lib.sangsawang.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.oakraw.lib.sangsawang.mclib.USBControl;

/**
 * Created by oakraw90 on 6/26/2014.
 */
public class CallReceiver extends BroadcastReceiver {

    MainActivity mainActivity = null;

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            // This code will execute when the phone has an incoming call

            // get the phone number
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            //Toast.makeText(context, "ส่องแสง Call from:" +mainActivity, Toast.LENGTH_LONG).show();
            if(mainActivity != null) {
                mainActivity.callHandler(true,0);
            }
            else{
                Intent callingIntent = new Intent(context, MainActivity.class);
                callingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                callingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String value = "phone coming";
                callingIntent.putExtra("phone", value);
                context.startActivity(callingIntent);
            }


        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            // This code will execute when the call is disconnected
           // Toast.makeText(context, "Detected call hangup event", Toast.LENGTH_LONG).show();

        }
    }

    void setMainActivityHandler(MainActivity main){
        mainActivity = main;
    }


}