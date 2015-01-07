package com.oakraw.lib.sangsawang.app;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class NotificationService extends AccessibilityService {
    final String TAG = "oakTag";
    private boolean isInit;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"onAccessibilityEvent");

        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            //Do something, eg getting packagename
            final String packagename = String.valueOf(event.getPackageName());
            if(!packagename.equals(getApplication().getPackageName()) && !packagename.equals("com.android.phone") && !packagename.equals("com.android.setting")&& !packagename.equals("com.nero.android.htc.sync")){
                Log.d(TAG,"ส่องแสง "+packagename);
                Toast.makeText(getApplicationContext(),"ส่องแสง Notification form "+packagename, Toast.LENGTH_SHORT).show();
                if(MainActivity.mainActivity != null)
                    MainActivity.mainActivity.callHandler(true,1);
            }
        }
    }

    @Override
    protected void onServiceConnected() {
        Log.d(TAG,"onServiceConnected");
        if (isInit) {
            return;
        }
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        setServiceInfo(info);
        isInit = true;
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG,"onInterrupt");
        isInit = false;
    }
}