package com.moon.myreadapp.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.moon.myreadapp.R;
import com.moon.myreadapp.ui.fragments.ImageDetailFragment;

import java.util.ArrayList;

public class ImageBrowserActivity extends FragmentActivity {


    private TextView indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://e.hiphotos.baidu.com/image/h%3D300/sign=8ffef0cf8101a18befeb144fae2e0761/8644ebf81a4c510fddd394f96659252dd42aa539.jpg");
        urls.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=9eae7c3766d9f2d33f1122e999ed8a53/3bf33a87e950352a230666de5743fbf2b3118b85.jpg");
        urls.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=cba296ffd3c8a786a12a4c085708c9c7/5bafa40f4bfbfbed45d9007a7af0f736afc31f36.jpg");
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
        if (savedInstanceState != null) {
            //pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        viewPager.setCurrentItem(1);
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
