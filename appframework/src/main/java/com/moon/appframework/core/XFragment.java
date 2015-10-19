package com.moon.appframework.core;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.appframework.common.log.XLog;

import butterknife.ButterKnife;


/**
 * @author kidcrazequ
 *
 */
public abstract class XFragment extends Fragment {


	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		int layoutRes = getLayoutRes();
		if (layoutRes == 0) {
			throw new IllegalArgumentException(
					"getLayoutRes() returned 0, which is not allowed. "
							+ "If you don't want to use getLayoutRes() but implement your own view for this "
							+ "fragment manually, then you have to override onCreateView();");
		} else {
			View view = inflater.inflate(layoutRes, container, false);
			return view;
		}
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this,view);
		initView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    public void onStart() {
        super.onStart();
		XApplication.getInstance().bus.register(this);
    }
	
	@Override
	public void onResume(){
		super.onResume();

	}
	
	@Override
	public void onPause(){
		super.onPause();
	}

    @Override
    public void onStop() {
		XApplication.getInstance().bus.unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

	@Override
	public void onDestroy(){
		super.onDestroy();
		ButterKnife.unbind(this);
	}

	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	protected int getLayoutRes() {
		return 0;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	public abstract void initView();
}
