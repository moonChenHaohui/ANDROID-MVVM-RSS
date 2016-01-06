package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;

import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.toast.SimpleToastHelper;
import com.moon.myreadapp.common.components.toast.TastyToast;
import com.moon.myreadapp.mvvm.models.dao.User;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;
import com.rey.material.app.Dialog;

import org.json.JSONArray;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by moon on 16/1/6.
 */
public class LoginViewModel extends BaseViewModel {


    private Activity mView;
    private User user;


    public LoginViewModel(Activity mView) {
        this.mView = mView;
        user = DBHelper.Query.getUser();

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
        return user;
    }

    /**
     * 找回密码
     * @param v
     */
    public void findPassword (View v){

    }

    /**
     * 注册
     * @param v
     */
    public void register (View v){
        DialogFractory.createDialog(mView, DialogFractory.Type.Register).show();
    }

    public void queryUser (String account,String password){
        if(user == null){
            user = new User();
            user.setAccount(account);
            user.setPassword(account);
        }
        final Dialog dialog = DialogFractory.createDialog(mView, DialogFractory.Type.Loading);
        dialog.show();
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("account", user.getAccount());
        query.addWhereEqualTo("password", user.getPassword());
        query.findObjects(mView, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list.size() > 0) {
                    User user = list.get(0);
                    SimpleToastHelper.showToast(R.string.login_success);
                    mView.finish();
                } else {
                    dialog.dismiss();
                    SimpleToastHelper.showToast(R.string.error_incorrect_password);
                }
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                SimpleToastHelper.showToast(R.string.error_incorrect_password);
            }
        });
    }
}
