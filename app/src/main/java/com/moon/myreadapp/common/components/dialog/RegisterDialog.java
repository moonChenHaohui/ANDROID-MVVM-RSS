package com.moon.myreadapp.common.components.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.moon.myreadapp.R;
import com.moon.myreadapp.databinding.FragmentRegisterBinding;
import com.moon.myreadapp.mvvm.models.dao.User;
import com.moon.myreadapp.ui.LoginActivity;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.Globals;
import com.rey.material.app.Dialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by moon on 16/1/6.
 * 注册dialog
 */
public class RegisterDialog extends Dialog {

    private FragmentRegisterBinding binding;
    private User user;

    public RegisterDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.register_title);
        layoutParams(-1, -2);
        setCanceledOnTouchOutside(false);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_register, null, false);
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        binding.account.setOnFocusChangeListener(focusListener);
        binding.password.setOnFocusChangeListener(focusListener);
        binding.passwordReq.setOnFocusChangeListener(focusListener);
    }

    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                showErrorInfo(null);
            }
        }
    };

    private void attemptRegister() {
        binding.account.setError(null);
        binding.password.setError(null);
        binding.passwordReq.setError(null);

        String account = binding.account.getText().toString();
        String password = binding.password.getText().toString();
        String passwordreq = binding.passwordReq.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // 密码检测
        if (TextUtils.isEmpty(password) || !LoginActivity.isPasswordValid(password)) {
            binding.password.setError(Globals.getApplication().getString(R.string.error_invalid_password));
            focusView = binding.password;
            cancel = true;
        }
        //密码重复检测
        if (TextUtils.isEmpty(passwordreq) || !LoginActivity.isPasswordValid(passwordreq) || !password.equals(passwordreq)) {
            binding.passwordReq.setError(Globals.getApplication().getString(R.string.error_invalid_password_req));
            focusView = binding.passwordReq;
            cancel = true;
        }

        //账号检测
        if (TextUtils.isEmpty(account)) {
            binding.account.setError(Globals.getApplication().getString(R.string.error_field_required));
            focusView = binding.account;
            cancel = true;
        } else if (!LoginActivity.isAccountValid(account)) {
            binding.account.setError(Globals.getApplication().getString(R.string.error_invalid_email));
            focusView = binding.account;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            register(account, password);
        }
    }

    private void register(String account, String password) {
        //可以提交注册
        user = new User();
        user.setAccount(account);
        user.setPassword(password);

        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("account", user.getAccount());
        query.setLimit(1);

        showProgress(true);
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list.size() == 0) {
                    //没找到
                    //保存
                    user.save(getContext(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            showProgress(false);
                            registerSuccess();

                        }

                        @Override
                        public void onFailure(int i, String s) {
                            showProgress(false);
                            showErrorInfo(Globals.getApplication().getString(R.string.system_error));
                        }
                    });
                } else {
                    showProgress(false);
                    showErrorInfo(Globals.getApplication().getString(R.string.register_account_already_exist));
                }

            }

            @Override
            public void onError(int i, String s) {
                showProgress(false);
                showErrorInfo(Globals.getApplication().getString(R.string.system_error));
            }
        });

    }

    private void showProgress(boolean isShow) {
        binding.progress.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        binding.account.setEnabled(!isShow);
        binding.login.setEnabled(!isShow);
    }

    /**
     * 注册成功
     */
    private void registerSuccess() {
        setTitle(R.string.register_success);
        com.rey.material.widget.Button button = new com.rey.material.widget.Button(getContext());
        button.setText(Globals.getApplication().getString(R.string.register_success_info));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(button);
        //保存用户
        if (user != null) {
            DBHelper.UpDate.saveUser(user);
        }
    }

    /**
     * 在提交时候出现的错误显示
     *
     * @param info
     */
    private void showErrorInfo(String info) {
        if (TextUtils.isEmpty(info)) {
            binding.login.setText(Globals.getApplication().getString(R.string.register_action));
        } else {
            binding.login.setText(info);
        }
    }
}
