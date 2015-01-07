package com.oakraw.lib.sangsawang.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by oakraw90 on 6/15/2014.
 */
public class AppAdapter extends BaseAdapter {

    private Context mContext;
    private ViewHolder holder;
    private boolean[] switchVal = new boolean[MainActivity.apps.size()];

    private class ViewHolder {
        ImageView icon;
        TextView name;
        Switch sw;
    }

    public AppAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return MainActivity.apps.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.app_list, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.sw = (Switch) convertView.findViewById(R.id.switch1);
            //switchVal[position] = holder.sw.isChecked();
            Log.d("oakTag","null "+position + " " +switchVal[position] );
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.sw.setChecked(switchVal[position]);
        Log.d("oakTag",position + " " +switchVal[position] );
        final Drawable iconPic = MainActivity.apps.get(position).loadIcon(mContext.getPackageManager());
        final String app_name = MainActivity.apps.get(position).loadLabel(mContext.getPackageManager()).toString();
        final String package_name = MainActivity.apps.get(position).activityInfo.packageName;

        holder.icon.setImageDrawable(iconPic);
        holder.name.setText(app_name);
        /*holder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   AppData.dataArrayList.add(new AppData(iconPic,app_name,package_name));
                }
                else{
                    AppData.remove(package_name);
                }
                switchVal[position] = isChecked;
                Log.d("oakTag",position + " " +switchVal[position] );
                printIt();
            }
        });*/

        holder.sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printIt();
                boolean check =  ((Switch)v).isChecked();
                if(check){
                    AppData.dataArrayList.add(new AppData(iconPic,app_name,package_name));
                }
                else{
                    AppData.remove(package_name);
                }
                switchVal[position] = check;
               // Log.d("oakTag",position + " " +switchVal[position] );
                printIt();
            }
        });

        return convertView;
    }

    void printIt(){
        for(int i=0;i<MainActivity.apps.size();i++){
            Log.d("oakTag","check >> "+i + " " +switchVal[i] );
        }
    }
}
