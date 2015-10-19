package com.moon.appframework.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


/**
 * Created by kidcrazequ on 15/3/4.
 */
public class XFragmentManager {

    private static FragmentManager mFragmentManager = XActivity.getInstance().getSupportFragmentManager();
    private static XFragment currentFragment;
    private static XFragment prevFragment;
    private static Activity mActivity = XActivity.getInstance();

    private static int enterId = android.R.animator.fade_in;
    private static int exitId = android.R.animator.fade_out;

    private static int layoutId;

    private static int stackCount = 0;
    private static final String TAG = "XFragmentManager";


    private XFragmentManager(){
    }

    public static void setLayoutId(@IdRes int resId){
        layoutId = resId;
    }

    /**
     * 添加指定的fragment
     * @param fragmentClass
     * @param args 
     */
    public static void add(Class<? extends XFragment> fragmentClass, Bundle args){
        add(fragmentClass, args, false);
    }

    /**
     * 添加指定的fragment
     * @param fragmentClass
     * @param args
     * @param isMultiple 是否支持多实例
     */
    public static void add(Class<? extends XFragment> fragmentClass, Bundle args, boolean isMultiple){
        add(fragmentClass, args, isMultiple, true);
    }

    /**
     * 添加指定的fragment
     * @param fragmentClass
     * @param args
     * @param isMultiple 是否支持多实例
     * @param isShowAnimation 是否展示切换动画
     */
    public static void add(Class<? extends XFragment> fragmentClass, Bundle args, boolean isMultiple, boolean isShowAnimation){
        if(mFragmentManager == null){
            throw new NullPointerException("XFragmentManager not init");
        }

        FragmentTransaction mTransation = mFragmentManager.beginTransaction();

        // 单实例
        if(!isMultiple){
            currentFragment = (XFragment) mFragmentManager.findFragmentByTag(fragmentClass.getName());
            if(currentFragment != null){
            	
            	if(prevFragment != null){
                	mTransation.hide(prevFragment);
                }
            	
            	if(isShowAnimation){
                    mTransation.setCustomAnimations(enterId, exitId);
                }
            	
                mTransation.show(currentFragment);
                mTransation.commitAllowingStateLoss();
                mFragmentManager.executePendingTransactions();
                prevFragment = currentFragment;
                return;
                
            }
        }
        
        // 多实例
        if(prevFragment != null){
        	mTransation.hide(prevFragment);
        }
        
        currentFragment = (XFragment) Fragment.instantiate(mActivity, fragmentClass.getName(), args);
        if(isShowAnimation){
            mTransation.setCustomAnimations(enterId, exitId);
        }

        mTransation.add(layoutId, currentFragment, fragmentClass.getName());
        mTransation.addToBackStack(fragmentClass.getName());
        mTransation.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
        
        prevFragment = getStackTopFragment();
        
        stackCount++;
    }

    /**
     * 删除指定的Fragment
     * @param fragmentClass fragment.class
     */
    public static void remove(Class<? extends XFragment> fragmentClass){
        if(mFragmentManager == null){
            throw new NullPointerException("XFragmentManager not init");
        }
          
        XFragment fragment = (XFragment) mFragmentManager.findFragmentByTag(fragmentClass.getName());
        
        if(stackCount <= 0){
        	return;
        }

        // 当栈里面的数量为1时,pop最后一个Fragment,否则remove删除当前Fragment
        if(stackCount == 1){ 
        	XFragment statckTopFragment = getStackTopFragment();
        	if(fragment != statckTopFragment){
        		return;
        	}
        	clear();
        }else{
        	remove(fragment);
        }
        
        stackCount--;
    }
    
    private static void remove(XFragment fragment){
    	FragmentTransaction mTransation = mFragmentManager.beginTransaction();
    	mTransation.remove(fragment);
    	mTransation.commitAllowingStateLoss();
    }

    /**
     * 返回操作
     * 
     */
    public static void back(){
        if(mFragmentManager == null){
            throw new NullPointerException("XFragmentManager not init");
        }

        if(stackCount > 1){
        	mFragmentManager.popBackStackImmediate();
        }else if(stackCount == 1){
        	clear();
        }else{
        	mActivity.finish();
        	return;
        }
        
        stackCount--;
    }

    /**
     * 获取返回栈里面BackStackEntry的实例个数
     * @return
     */
    public static int getBackStackEntryCount(){
        if(mFragmentManager == null){
            throw new NullPointerException("XFragmentManager not init");
        }

        return mFragmentManager.getBackStackEntryCount();
    }
    
    /**
     * 获取返回栈的Fragment个数
     * @return
     */
    public static int count(){
    	return stackCount;
    }

    /**
     * 清空回退栈
     */
    public static void clear(){
        if(mFragmentManager == null){
            throw new NullPointerException("XFragmentManager not init");
        }

        if(currentFragment != null){
            currentFragment = null;
        }

        mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mFragmentManager.executePendingTransactions();
    }

    /*
     * 获取指定fragment实例
     */
    public static XFragment get(Class<? extends XFragment> fragmentClass){
        if(mFragmentManager == null){
            throw new NullPointerException("XFragmentManager not init");
        }

        return (XFragment) mFragmentManager.findFragmentByTag(fragmentClass.getName());
    }

    /**
     * 设置fragment切换的动画
     * @param enterId  进入动画id
     * @param exitId 退出动画id
     */
    public static void setCustomAnimations(int enterId, int exitId){
        XFragmentManager.enterId = enterId;
        XFragmentManager.exitId = exitId;
    }
    
    /**
     * 拿到当前返回栈，栈顶的fragment
     * @return
     */
    private static XFragment getStackTopFragment(){
    	XFragment topFragment = null;
        int idx = mFragmentManager.getBackStackEntryCount();
        if (idx > 0) {
            FragmentManager.BackStackEntry entry = mFragmentManager.getBackStackEntryAt(idx-1);
            topFragment = (XFragment) mFragmentManager.findFragmentByTag(entry.getName());
        }
        
        return topFragment;
    }

}
