package com.moon.appframework.common.util;

import android.os.AsyncTask;

import java.util.Objects;

public abstract class SafeAsyncTask<Object, Progress, Result> extends AsyncTask<Object, Progress, Result>{

	@Override
	protected Result doInBackground(Object... params) {
		return null;
	}


	@Override
	protected void onPostExecute(Result result) {
		if(!isCancelled() && result != null){
			onSafePostExecute(result);
		}
    }
	
	@Override
	protected void onCancelled(Result result) {
        onCancelled();
    }


	/**
	 * 更安全的方式在主线程执行，防止Activity,Fragment销毁时，task进程还在运行，导致空指针错误
	 * @param result
	 */
	protected void onSafePostExecute(Result result){
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#onCancelled()
	 */
	protected void onCancelled() {
	}
	
	public void destory(){
		cancel(true);
	}
	
}
