package com.moon.myreadapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.core.XActivity;
import com.moon.myreadapp.read.homepage.fragment.HomeFragment;
import com.moon.myreadapp.read.me.fragment.MeFragment;
import com.moon.myreadapp.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends BaseActivity {

    private LinearLayout tabLayout;
    private FrameLayout container;
    private View.OnClickListener navLinstener;
    private List<Fragment> mTabs = new ArrayList<>();
    private int mPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    private void initViews() {
        initContainer();
        initTabBar();
        gotoTab(0);
    }

    private void initContainer() {
        container = getView(R.id.container);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        mTabs.add(homeFragment);
        MeFragment meFragment = new MeFragment();
        mTabs.add(meFragment);
        ft.add(R.id.container, homeFragment);
        ft.hide(homeFragment);
        ft.add(R.id.container, meFragment);
        ft.hide(meFragment);
        ft.commit();
    }

    private void initTabBar() {
        tabLayout = getView(R.id.navbar);
        navLinstener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof IconTextView) {
                    //改变显示的fragment
                    gotoTab((int) v.getTag());
                }
            }
        };
        LinearLayout.LayoutParams lp;
        for (int i = 0; i < 2; i++) {
            IconTextView it = new IconTextView(tabLayout.getContext());
            lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            it.setText("{fa-commenting}");
            it.setTag(i);
            it.setGravity(Gravity.CENTER);
            it.setTextSize(getResources().getDimension(R.dimen.activity_tab_bar_item_text));
            it.setLayoutParams(lp);
            it.setOnClickListener(navLinstener);
            tabLayout.addView(it);
        }
    }


    private void gotoTab(int index) {
        if (index == mPosition) {
            return;
        }
        //改变选中tab颜色
        ((IconTextView) tabLayout.getChildAt(index)).setTextColor(getResources().getColor(R.color.tab_bar_item_acvite));
        if (mPosition >= 0) {
            ((IconTextView) tabLayout.getChildAt(mPosition)).setTextColor(getResources().getColor(R.color.tab_bar_item_normal));
        }
        //改变显示的fragment
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        Fragment nf = mTabs.get(index);
        if (mPosition >= 0) {
            Fragment old = mTabs.get(mPosition);
            ft.hide(old);
        }
        ft.show(nf);
        mPosition = index;
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_more) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabs.clear();
    }
}
