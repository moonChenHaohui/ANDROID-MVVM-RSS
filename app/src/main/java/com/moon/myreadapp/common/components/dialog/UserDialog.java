package com.moon.myreadapp.common.components.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.UpdateUserEvent;
import com.moon.myreadapp.databinding.FragmentRegisterBinding;
import com.moon.myreadapp.databinding.FragmentUserBinding;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.mvvm.models.dao.User;
import com.moon.myreadapp.ui.LoginActivity;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;
import com.moon.myreadapp.util.Globals;
import com.rey.material.app.Dialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by moon on 16/1/14.
 * 用户信息
 */
public class UserDialog extends Dialog {

    private FragmentUserBinding binding;
    private User user;
    private Dialog dialog;

    public UserDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.user_title);
        layoutParams(-1, -2);
        setCanceledOnTouchOutside(true);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_user, null, false);
        setContentView(binding.getRoot());

        negativeAction(R.string.user_quit_user);
        negativeActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (dialog != null && dialog.isShowing()){
                        return;
                    }
                    dialog = DialogFractory.createDialog(getContext(), DialogFractory.Type.YesNo).positiveActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user = null;
                            DBHelper.Delete.deleteUser();
                            dialog.dismiss();
                            dismiss();
                        }
                    }).title(R.string.user_quit_user_check);
                    dialog.show();
                } else {
                    dismiss();
                }

            }
        }).positiveAction(R.string.user_exit);
        positiveActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        initUser();
    }
    
    private void initUser(){
        showProgress(true);
        user = DBHelper.Query.getUser();
        if (user == null){
            dismiss();
            return;
        }
        binding.setUser(user);

        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("account", user.getAccount());
        query.addWhereEqualTo("password", user.getPassword());
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                showProgress(false);
                if (list == null || list.size() == 0){
                    user = null;
                    ToastHelper.showToast(R.string.request_error);
                    dismiss();
                    return;
                }
                user = list.get(0);
                DBHelper.UpDate.saveUser(user);
                binding.setUser(user);
            }

            @Override
            public void onError(int i, String s) {
                ToastHelper.showToast(R.string.request_error);
                showProgress(false);
            }
        });
    }


    private void showProgress(boolean isShow) {
        binding.progress.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }


    @Override
    public void dismiss() {
        //通知用户更新
        XApplication.getInstance().bus.post(new UpdateUserEvent(user));
        super.dismiss();
    }
}
