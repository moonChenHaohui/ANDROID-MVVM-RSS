package com.moon.myreadapp.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.moon.myreadapp.R;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.ui.fragments.ImageDetailFragment;

import java.util.ArrayList;

public class ImageBrowserActivity extends FragmentActivity {


    private TextView indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<String> urls = getIntent().getExtras().getStringArrayList(Constants.IMAGES_LIST);
        int position = getIntent().getExtras().getInt(Constants.IMAGES_NOW_POSITION);
        if (urls == null || urls.isEmpty()){
            finish();
        }
        if (position < 0 || position >= urls.size()){
            position = 0;
        }
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        viewPager.setAdapter(mAdapter);


        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, viewPager.getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, viewPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });


        viewPager.setCurrentItem(position);
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }
    }


    @Override
    public void finish() {
        super.finish();

    }
}
