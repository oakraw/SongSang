package com.oakraw.lib.sangsawang.mclib;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by oakraw90 on 8/19/2014.
 */
public class USBControl {
    private USBAccessoryManager accessoryManager;
    private final static int USBAccessoryWhat = 0;
    private boolean deviceAttached = false;

    public static final int UPDATE_LED_SETTING		 	= 1;
    public static final int APP_CONNECT					= (int)0xFE;
    public static final int APP_DISCONNECT				= (int)0xFF;
    public static final int LED_1_ON					= 0x02;


    String TAG = "tag";
    private enum ErrorMessageCode {
        ERROR_OPEN_ACCESSORY_FRAMEWORK_MISSING,
        ERROR_FIRMWARE_PROTOCOL
    };


    public USBControl() {
        accessoryManager = new USBAccessoryManager(handler, USBAccessoryWhat);
    }

    public void enable(Context context,Intent intent){
        accessoryManager.enable(context, intent);
    }

    private int firmwareProtocol;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] commandPacket = new byte[2];

            switch(msg.what)
            {
                case UPDATE_LED_SETTING:

                    commandPacket[0] = UPDATE_LED_SETTING;
                    commandPacket[1] = LED_1_ON;
                    accessoryManager.write(commandPacket);

                    break;

                case USBAccessoryWhat:
                    switch(((USBAccessoryManagerMessage)msg.obj).type) {
                        case CONNECTED:
                            break;
                        case READY:
                            //setTitle("Basic Accessory Demo: Device connected.");

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
                            disconnectAccessory();
                            break;
                    }

                    break;
                default:
                    break;
            }	//switch
        } //handleMessage
    }; //handler

    public void disconnectAccessory() {

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
