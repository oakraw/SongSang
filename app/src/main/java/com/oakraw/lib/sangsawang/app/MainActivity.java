package com.oakraw.lib.sangsawang.app;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oakraw.lib.sangsawang.mclib.USBAccessoryManager;
import com.oakraw.lib.sangsawang.mclib.USBAccessoryManagerMessage;
import com.viewpagerindicator.PageIndicator;

import java.util.List;

public class MainActivity extends FragmentActivity  {

    private String status = "ยังไม่ได้ต่อกล่องส่องแสง";
    public static Context context;
    public static List<ResolveInfo> apps;
    final String LOGTAG = "oakTag";

    public static MainActivity mainActivity = null;

    private USBAccessoryManager accessoryManager;
    private final static int USBAccessoryWhat = 0;
    private boolean deviceAttached = false;

    public static final int UPDATE_LED_SETTING		 	= 1;
    public static final int APP_CONNECT					= (int)0xFE;
    public static final int APP_DISCONNECT				= (int)0xFF;
    public static final int LED_1_ON					= 0x02;
    public static final int LED_2_ON					= 0x04;

    private TextView statusText;
    public static CallReceiver yourBR = null;
    private boolean isSongSang = false;
    private ImageView iconSongSang;
    private Handler handlerBlink = new Handler();
    private int blinkRate;
    private int blinkRateOn;
    private int blinkRateOff;
    public static int blinkRatePhoneOn;
    public static int blinkRatePhoneOff;
    public static int blinkRateNotiOn;
    public static int blinkRateNotiOff;

    String TAG = "tag";
    private String[] secsOn;
    private String[] secsOff;

    private enum ErrorMessageCode {
        ERROR_OPEN_ACCESSORY_FRAMEWORK_MISSING,
        ERROR_FIRMWARE_PROTOCOL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOGTAG,"onCreate");

        context = this;
        mainActivity = this;
        //getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getActionBar().setCustomView(R.layout.custom_titlebar);

        accessoryManager = new USBAccessoryManager(handler, USBAccessoryWhat);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            String intentextra = bundle.getString("phone");
            if(intentextra != null){
                if(intentextra.equals("phone coming")){
                    //Toast.makeText(context,intentextra,Toast.LENGTH_LONG).show();
                    callHandler(true,1);
                }
            }

        }


        yourBR = new CallReceiver();
        yourBR.setMainActivityHandler(this);

        secsOn = getResources().getStringArray(R.array.sec_on_array);
        secsOff = getResources().getStringArray(R.array.sec_off_array);

        final SharedPreferences setting = MainActivity.context.getSharedPreferences("BlinkRate", 0);
        blinkRatePhoneOn = (int)Math.round(Double.parseDouble(secsOn[setting.getInt("PhoneOnLed", 0)])*1000d);
        blinkRatePhoneOff = (int)Math.round(Double.parseDouble(secsOff[setting.getInt("PhoneOffLed", 0)])*1000d);
        blinkRateNotiOn = (int)Math.round(Double.parseDouble(secsOn[setting.getInt("NotiOnLed", 0)])*1000d);
        blinkRateNotiOff = (int)Math.round(Double.parseDouble(secsOff[setting.getInt("NotiOffLed", 0)])*1000d);


        final SharedPreferences settings = MainActivity.context.getSharedPreferences("BroadcastReceiver", 0);
        boolean phoneBtnBool = settings.getBoolean("Phone", false);
        if(phoneBtnBool) {
            IntentFilter callInterceptorIntentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
            registerReceiver(yourBR, callInterceptorIntentFilter);
        }

        statusText = (TextView)findViewById(R.id.status);
        /*final PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);*/

        //Set the pager with an adapter
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new TitleAdapter(getSupportFragmentManager()));

        //Bind the title indicator to the adapter
        PageIndicator titleIndicator = (PageIndicator)findViewById(R.id.indicator);
        titleIndicator.setViewPager(pager);



       /* Button startBtn = (Button) findViewById(R.id.start_btn);
        Button stopBtn = (Button) findViewById(R.id.stop_btn);
        Button checkBtn = (Button) findViewById(R.id.check_btn);

        final Intent intent = new Intent(context,NotificationService.class);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PackageManager pm = context.getPackageManager();
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
                for(ResolveInfo pack : apps){
                    Log.d(LOGTAG, "" + pack.loadLabel(pm));
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,isAccessibilityEnabled()+"",Toast.LENGTH_LONG).show();
            }
        });*/

        iconSongSang = (ImageView)findViewById(R.id.icon);
        iconSongSang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"click",Toast.LENGTH_LONG).show();
                isSongSang = !isSongSang;
                callHandler(isSongSang, 99);


            }
        });

    }



//    @Override
//    protected void onResume() {
//        super.onResume();
//        accessoryManager.enable(this, getIntent());
//    }

    @Override
    protected void onStart() {
        super.onStart();
        statusText.setText(status);
        accessoryManager.enable(this, getIntent());

    }

    @Override
    public void onDestroy() {
        disconnectAccessory();
        super.onDestroy();
    }



    public void callHandler(boolean order,int type){
        isSongSang = order;
        //Toast.makeText(context,"callHandler",Toast.LENGTH_LONG).show();
        if(type == 99){
            handler.sendMessage(Message.obtain(handler, UPDATE_LED_SETTING));
        }
        else if(type == 0 && blinkRatePhoneOff == 0)//BlinkRatePhone 0
        {
            handler.sendMessage(Message.obtain(handler, UPDATE_LED_SETTING));
        }
        else if(type == 1 && blinkRateNotiOff == 0)//BlinkRateNoti 0
        {
            handler.sendMessage(Message.obtain(handler, UPDATE_LED_SETTING));
        }
        else if(type == 0 && blinkRatePhoneOff != 0){
            handler.sendMessage(Message.obtain(handler, 10));
        }
        else if(blinkRatePhoneOn == 0 ){

        }
        else{
            handler.sendMessage(Message.obtain(handler, 11));
        }
    }

    private int firmwareProtocol;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            byte[] commandPacket = new byte[2];

            switch(msg.what)
            {
                case UPDATE_LED_SETTING:
                    Log.d("UPDATEEEE",String.valueOf(accessoryManager.isConnected()));
                    //Toast.makeText(context,String.valueOf(accessoryManager.isConnected()),Toast.LENGTH_LONG).show();
                    if(accessoryManager.isConnected() == false) {
                        return;
                    }
                    //Toast.makeText(context,"UPDATE_LED_SETTING",Toast.LENGTH_LONG).show();
                    byte[] commPacket = new byte[2];
                    commPacket[0] = UPDATE_LED_SETTING;
                    commPacket[1] = 0;
                    accessoryManager.write(commPacket);
                    if(isSongSang) {
                        commPacket[1] = LED_1_ON;
                        accessoryManager.write(commPacket);
                        iconSongSang.setImageResource(R.drawable.sang);
                    }else{
                        handlerBlink.removeCallbacks(blinkLED);
                        iconSongSang.setImageResource(R.drawable.song);
                        stopBlink = true;
                    }

                    break;

                case 10://for phone
                    if(accessoryManager.isConnected() == false) {
                        return;
                    }
                    blinkRateOn = blinkRatePhoneOn;
                    blinkRateOff = blinkRatePhoneOff;

                    //Toast.makeText(context,"UPDATE_LED_SETTING",Toast.LENGTH_LONG).show();
                    byte[] comm10Packet = new byte[2];
                    comm10Packet[0] = UPDATE_LED_SETTING;
                    comm10Packet[1] = 0;
                    accessoryManager.write(comm10Packet);
                    if(isSongSang) {
                        //commPacket[1] = LED_1_ON;
                        //accessoryManager.write(commPacket);
                        if(stopBlink) {
                            isBlink = true;
                            stopBlink = false;
                            iconSongSang.setImageResource(R.drawable.sang);
                            handlerBlink.post(blinkLED);
                        }
                    }else{
                        handlerBlink.removeCallbacks(blinkLED);
                        iconSongSang.setImageResource(R.drawable.song);
                        stopBlink = true;
                    }
                    break;

                case 11://for noti
                    if(accessoryManager.isConnected() == false) {
                        return;
                    }
                    blinkRateOn = blinkRateNotiOn;
                    blinkRateOff = blinkRateNotiOff;

                    //Toast.makeText(context,"UPDATE_LED_SETTING",Toast.LENGTH_LONG).show();
                    byte[] comm11Packet = new byte[2];
                    comm11Packet[0] = UPDATE_LED_SETTING;
                    comm11Packet[1] = 0;
                    accessoryManager.write(comm11Packet);
                    if(isSongSang) {
                        //commPacket[1] = LED_1_ON;
                        //accessoryManager.write(commPacket);
                        if(stopBlink) {
                            isBlink = true;
                            stopBlink = false;
                            iconSongSang.setImageResource(R.drawable.sang);
                            handlerBlink.post(blinkLED);
                        }
                    }else{
                        handlerBlink.removeCallbacks(blinkLED);
                        iconSongSang.setImageResource(R.drawable.song);
                        stopBlink = true;
                    }
                    break;

                case USBAccessoryWhat:
                    switch(((USBAccessoryManagerMessage)msg.obj).type) {
                        case CONNECTED:
                            break;
                        case READY:
                            //setTitle("Basic Accessory Demo: Device connected.");
                            status = "ต่อกล่องส่องแสงแล้ว";
                            firstBlink = true;
                            handlerBlink.post(blinkLED);
                            statusText.setText(status);
                            //Log.d(TAG, "BasicAccessoryDemo:Handler:READY");

                            //LEDButtonEnable(true);

                            String version = ((USBAccessoryManagerMessage)msg.obj).accessory.getVersion();
                            firmwareProtocol = getFirmwareProtocol(version);

                            switch(firmwareProtocol){
                                case 1:
                                    deviceAttached = true;
                                    break;
                                case 2:
                                    deviceAttached = true;
                                    commandPacket[0] = (byte) APP_CONNECT;
                                    commandPacket[1] = 0;
                                    Log.d(TAG,"sending connect message.");
                                    accessoryManager.write(commandPacket);
                                    Log.d(TAG,"connect message sent.");
                                    break;
                                default:
                                    showErrorPage(ErrorMessageCode.ERROR_FIRMWARE_PROTOCOL);
                                    break;
                            }
                            break;
                        case DISCONNECTED:
                            Toast.makeText(context,"Disconnected",Toast.LENGTH_LONG).show();
                            disconnectAccessory();
                            break;
                    }

                    break;
                default:
                    break;
            }	//switch
        } //handleMessage
    }; //handler

    private boolean isBlink = true;
    public boolean stopBlink = false;
    public boolean firstBlink = false;

    private Runnable blinkLED  = new Runnable (){

        @Override
        public void run() {

            byte[] commPacket = new byte[2];
            commPacket[0] = UPDATE_LED_SETTING;
            if(!isBlink) {
                commPacket[1] = 0;
                blinkRate = blinkRateOff;
            }
            else{
                commPacket[1] = LED_1_ON;
                blinkRate = blinkRateOn;

            }
            accessoryManager.write(commPacket);
            isBlink = !isBlink;

            if(firstBlink){
                firstBlink = false;
                stopBlink = true;
                handler.postDelayed(blinkLED, 500);
            }
            else {
                if (!stopBlink)
                    handler.postDelayed(blinkLED, blinkRate);
                else {
                    commPacket[1] = 0;
                    accessoryManager.write(commPacket);
                }
            }


        }
    };

    public void disconnectAccessory() {
        status = "ยังไม่ได้ต่อกล่องส่องแสง";
        statusText.setText(status);
        switch(firmwareProtocol) {
            case 2:
                byte[] commandPacket = new byte[2];
                commandPacket[0] = (byte) APP_DISCONNECT;
                commandPacket[1] = 0;
                accessoryManager.write(commandPacket);
                break;
        }

        try {
            while(accessoryManager.isClosed() == false) {
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        accessoryManager.disable(this);

    }

    private int getFirmwareProtocol(String version) {

        String major = "0";

        int positionOfDot;

        positionOfDot = version.indexOf('.');
        if(positionOfDot != -1) {
            major = version.substring(0, positionOfDot);
        }

        return new Integer(major).intValue();
    }

    private void showErrorPage(ErrorMessageCode error){

        //TextView errorMessage = (TextView)findViewById(R.id.error_message);

        switch(error){
            case ERROR_OPEN_ACCESSORY_FRAMEWORK_MISSING:
                //errorMessage.setText(getResources().getText(R.string.error_missing_open_accessory_framework));
                break;
            case ERROR_FIRMWARE_PROTOCOL:
                // errorMessage.setText(getResources().getText(R.string.error_firmware_protocol));
                break;
            default:
                //  errorMessage.setText(getResources().getText(R.string.error_default));
                break;
        }
    }
}
