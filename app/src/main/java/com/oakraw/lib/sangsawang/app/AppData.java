package com.oakraw.lib.sangsawang.app;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by oakraw90 on 6/15/2014.
 */
public class AppData {
    private Drawable icon;
    private String name;
    private String package_name;

    public static ArrayList<AppData> dataArrayList = new ArrayList<AppData>();

    public AppData(Drawable icon, String name, String package_name) {
        this.icon = icon;
        this.name = name;
        this.package_name = package_name;
    }

    public static void remove(String package_name){
        for(int i=0;i<dataArrayList.size();i++){
            if(dataArrayList.get(i).package_name == package_name){
                dataArrayList.remove(i);
                break;
            }
        }
    }

    public static String showname(int position){
        return dataArrayList.get(position).name;
    }
}
