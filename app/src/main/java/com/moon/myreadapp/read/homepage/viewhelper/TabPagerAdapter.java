package com.moon.myreadapp.read.homepage.viewhelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.moon.myreadapp.read.homepage.fragment.HomeFragment;
import com.moon.myreadapp.read.me.fragment.MeFragment;

import java.util.ArrayList;

/**
 * Created by moon on 15/10/1.
 */
    public class TabPagerAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> listFragments = new ArrayList<>();

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
        listFragments.add(new HomeFragment());
        listFragments.add(new MeFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
