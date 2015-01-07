package com.oakraw.lib.sangsawang.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;

/**
 * Created by oakraw90 on 6/15/2014.
 */
public class TitleAdapter extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[] { "Home", "Setting"};

    public TitleAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("oak", position+"");
        if(position == 0)
            return new HomeFragment();
        else
            return new AppSetting();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length];
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }


}
