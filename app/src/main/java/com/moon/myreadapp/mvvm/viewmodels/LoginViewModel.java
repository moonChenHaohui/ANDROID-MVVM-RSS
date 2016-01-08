package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;

import com.moon.appframework.common.log.XLog;
import com.moon.appframework.core.XApplication;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.UpdateUserEvent;
import com.moon.myreadapp.mvvm.models.dao.User;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;
import com.rey.material.app.Dialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by moon on 16/1/6.
 */
public class LoginViewModel extends BaseViewModel {


    private Activity mView;
    private User muUser;
    private boolean isOnlogin;


    public LoginViewModel(Activity mView) {
        this.mView = mView;
        muUser = DBHelper.Query.getUser();

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void clear() {
        mView = null;
    }


    @Bindable
    public User getUser() {
        return muUser;
    }

    /**
     * 找回密码
     *
     * @param v
     */
    public void findPassword(View v) {

    }

    /**
     * 注册
     *
     * @param v
     */
    public void register(View v) {
        DialogFractory.createDialog(mView, DialogFractory.Type.Register).show();
    }

    public void queryUser(String account, String password) {
        if (isOnlogin) {
            return;
        }
        //使用生成的账号去登录
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);

        final Dialog dialog = DialogFractory.createDialog(mView, DialogFractory.Type.Loading);
        dialog.show();
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("account", user.getAccount());
        query.addWhereEqualTo("password", user.getPassword());
        isOnlogin = true;
        query.findObjects(mView, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                for (User r : list) {
                    XLog.d("acc:" + r.getAccount() + "pwd:" + r.getPassword());
                }
                if (list.size() > 0) {
                    User user = list.get(0);
                    ToastHelper.showToast(R.string.login_success);
                    //更新本地用户数据
                    muUser = user;
                    DBHelper.UpDate.saveUser(user);
                    //通知更新
                    XApplication.getInstance().bus.post(new UpdateUserEvent(user));
                    mView.finish();
                } else {
                    dialog.dismiss();
                    ToastHelper.showToast(R.string.error_incorrect_password);
                }
                isOnlogin = false;
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                ToastHelper.showToast(R.string.system_error);
                isOnlogin = false;
            }
        });
    }
}
