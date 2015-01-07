package com.oakraw.lib.sangsawang.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.cengalabs.flatui.views.FlatSeekBar;

/**
 * Created by oakraw90 on 6/15/2014.
 */
public class AppSetting extends Fragment {
    private TextView valuePhone;
    private TextView valueNoti;
    private Spinner phoneOnLed;
    private Spinner phoneOffLed;
    private String[] secsOn;
    private String[] secsOff;
    private Spinner notiOnLed;
    private Spinner notiOffLed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.app_setting_fragment, container, false);

        /*ListView appList = (ListView) rootView.findViewById(R.id.listView);
        AppAdapter appAdapter = new AppAdapter(MainActivity.context);
        appList.setAdapter(appAdapter);*/
        phoneOnLed = (Spinner) rootView.findViewById(R.id.phoneOnLed);
        phoneOffLed = (Spinner) rootView.findViewById(R.id.phoneOffLed);
        notiOnLed = (Spinner) rootView.findViewById(R.id.notiOnLed);
        notiOffLed = (Spinner) rootView.findViewById(R.id.notiOffLed);
        ArrayAdapter<CharSequence> adapterOn = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.sec_on_array, R.layout.spinner_item);
        adapterOn.setDropDownViewResource(R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapterOff = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.sec_off_array, R.layout.spinner_item);
        adapterOff.setDropDownViewResource(R.layout.spinner_item);
        phoneOnLed.setAdapter(adapterOn);
        phoneOffLed.setAdapter(adapterOff);
        notiOnLed.setAdapter(adapterOn);
        notiOffLed.setAdapter(adapterOff);

//        FlatSeekBar seekBarPhone = (FlatSeekBar)rootView.findViewById(R.id.seekBarPhone);
//        valuePhone = (TextView) rootView.findViewById(R.id.valuePhone);
//        FlatSeekBar seekBarNoti = (FlatSeekBar)rootView.findViewById(R.id.seekBarNoti);
//        valueNoti = (TextView) rootView.findViewById(R.id.valueNoti);

        secsOn = getResources().getStringArray(R.array.sec_on_array);
        secsOff = getResources().getStringArray(R.array.sec_off_array);


        final SharedPreferences settings = MainActivity.context.getSharedPreferences("BlinkRate", 0);
        int phoneBlinkOn = settings.getInt("PhoneOnLed", 0);
        int phoneBlinkOff = settings.getInt("PhoneOffLed", 0);
        phoneOnLed.setSelection(phoneBlinkOn);
        phoneOffLed.setSelection(phoneBlinkOff);
        int notiBlinkOn = settings.getInt("NotiOnLed", 0);
        int notiBlinkOff = settings.getInt("NotiOffLed", 0);
        notiOnLed.setSelection(notiBlinkOn);
        notiOffLed.setSelection(notiBlinkOff);

        phoneOnLed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("PhoneOnLed", position);
                editor.commit();
                MainActivity.blinkRatePhoneOn = (int)Math.round(Double.parseDouble(secsOn[position])*1000d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        phoneOffLed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("PhoneOffLed", position);
                editor.commit();
                MainActivity.blinkRatePhoneOff = (int)Math.round(Double.parseDouble(secsOff[position])*1000d);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        notiOnLed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("NotiOnLed", position);
                editor.commit();
                MainActivity.blinkRateNotiOn = (int)Math.round(Double.parseDouble(secsOn[position])*1000d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        notiOffLed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("NotiOffLed", position);
                editor.commit();
                MainActivity.blinkRateNotiOff = (int)Math.round(Double.parseDouble(secsOff[position])*1000d);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*updatePhoneText(phoneBlinkRate);
        seekBarPhone.setProgress(phoneBlinkRate);
        seekBarPhone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePhoneText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity.blinkRatePhone = seekBar.getProgress()*100;
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("PhoneBlinkRate", seekBar.getProgress());
                editor.commit();
            }
        });

        updateNotiText(notiBlinkRate);
        seekBarNoti.setProgress(notiBlinkRate);
        seekBarNoti.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateNotiText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity.blinkRateNoti = seekBar.getProgress()*100;
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("NotiBlinkRate", seekBar.getProgress());
                editor.commit();
            }
        });*/
        return rootView;
    }

    private void updatePhoneText(int progress){
        float p = progress/10f;
        if((p*10)%10 == 0) {
            int q = Math.round(p);
            valuePhone.setText(String.valueOf(q) + " วินาที");
        }
        else
            valuePhone.setText(String.valueOf(p) + " วินาที");
    }

    private void updateNotiText(int progress){
        float p = progress/10f;
        if((p*10)%10 == 0) {
            int q = Math.round(p);
            valueNoti.setText(String.valueOf(q) + " วินาที");
        }
        else
            valueNoti.setText(String.valueOf(p) + " วินาที");
    }
}
