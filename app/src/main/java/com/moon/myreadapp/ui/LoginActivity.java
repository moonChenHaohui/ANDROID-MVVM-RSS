package com.moon.myreadapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;
import com.moon.myreadapp.databinding.ActivityLoginBinding;
import com.moon.myreadapp.mvvm.viewmodels.LoginViewModel;
import com.moon.myreadapp.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected Toolbar getToolBar() {
        return null;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_login;
    }

    @Override
    public void setContentViewAndBindVm(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutView());
        viewModel = new LoginViewModel(this);
        binding.setModel(viewModel);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding.login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        //填充本地的用户
        if (viewModel.getUser() != null) {
            XLog.d("VM:" + viewModel.getUser().getAccount());
            binding.account.setText(viewModel.getUser().getAccount());
        }
    }

    private void attemptLogin() {
        binding.account.setError(null);
        binding.password.setError(null);

        String account = binding.account.getText().toString();
        String password = binding.password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 密码检测
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            binding.password.setError(getString(R.string.error_invalid_password));
            focusView = binding.password;
            cancel = true;
        }

        //账号检测
        if (TextUtils.isEmpty(account)) {
            binding.account.setError(getString(R.string.error_field_required));
            focusView = binding.account;
            cancel = true;
        } else if (!isAccountValid(account)) {
            binding.account.setError(getString(R.string.error_invalid_email));
            focusView = binding.account;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            viewModel.queryUser(account, password);
        }


    }

    public static boolean isAccountValid(String account) {
        return account.contains("@");
    }


    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    @Override
    protected void onDestroy() {
        if (viewModel != null) {
            viewModel.clear();
            viewModel = null;
        }
        super.onDestroy();
    }

}

