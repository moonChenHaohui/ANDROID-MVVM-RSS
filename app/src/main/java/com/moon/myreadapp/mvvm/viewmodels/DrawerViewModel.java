package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XApplication;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.adapter.DrawerAdapter;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.SynchronizeStateEvent;
import com.moon.myreadapp.common.event.UpdateFeedListEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.MenuItem;
import com.moon.myreadapp.mvvm.models.SyncState;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.mvvm.models.dao.Feed;
import com.moon.myreadapp.mvvm.models.dao.User;
import com.moon.myreadapp.mvvm.models.dao.UserDao;
import com.moon.myreadapp.ui.AddFeedActivity;
import com.moon.myreadapp.ui.LoginActivity;
import com.moon.myreadapp.ui.SettingActivity;
import com.moon.myreadapp.ui.ViewArticleActivity;
import com.moon.myreadapp.util.BmobHelper;
import com.moon.myreadapp.util.Conver;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.DialogFractory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

/**
 * Created by moon on 15/10/19.
 */
public class DrawerViewModel extends BaseViewModel {


    private User user;

    private DrawerAdapter drawerAdapter;

    private AdapterView.OnItemClickListener drawerItemClickListener;

    private Activity mView;

    private String notice;

    private SyncState syncState;


    private BmobHelper.SyncListener syncListener;


    public DrawerViewModel(Activity view) {
        this.mView = view;
        syncState = SynchronizeStateEvent.getInstance();
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        List<MenuItem> menus = new ArrayList<>();
        String[] fucs = mView.getResources().getStringArray(R.array.functions);
        String[] fuc_icons = mView.getResources().getStringArray(R.array.functions_icon);
        for (int i = 0; i < fucs.length && i < fuc_icons.length; i++) {
            menus.add(new MenuItem.Builder().title(fucs[i]).icon(fuc_icons[i]).build());
        }
        drawerAdapter = new DrawerAdapter(menus);
        drawerItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        XDispatcher.from(mView).dispatch(new RouterAction(AddFeedActivity.class, true));
                        break;
                    case 1:
                    case 2:
                    case 3:
                        int type = -1;
                        if (position == 1) {
                            type = ViewArticleViewModel.Style.VIEW_UNREAD.ordinal();
                        } else if (position == 2) {
                            type = ViewArticleViewModel.Style.VIEW_FAVOR.ordinal();
                        } else if (position == 3) {
                            type = ViewArticleViewModel.Style.VIEW_READ_HISTORY.ordinal();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.VIEW_ARTICLE_TYPE, type);
                        XDispatcher.from(mView).dispatch(new RouterAction(ViewArticleActivity.class, bundle, true));
                        break;
                    case 4:
                        XDispatcher.from(mView).dispatch(new RouterAction(SettingActivity.class, true));
                        break;
                    case 5:
//                        if(syncState.isSpin()){
//                            ToastHelper.showToast(R.string.drawer_sync_in_sync_wait);
//                            break;
//                        }
                        DialogFractory.createDialog(mView, DialogFractory.Type.ThemeChoose).show();
                        break;
                }
            }
        };
    }

    @Override
    public void initEvents() {
        setUser(DBHelper.Query.getUser());
        requestUser();
        syncListener = new BmobHelper.SyncListener() {
            @Override
            public void onDataSyncSuccess() {

            }

            @Override
            public void onDataDownloadSuccess() {

            }

            @Override
            public void onDataSyncFailure(int i, String s) {
                syncState.setNotice(R.string.drawer_sync_in_download_fail);
                syncState.setIsSpin(false);
                notifyPropertyChanged(BR.syncState);
            }

            @Override
            public void onDataDownloadFailure(int i, String s) {
                syncState.setNotice(R.string.drawer_sync_in_download_fail);
                syncState.setIsSpin(false);
                notifyPropertyChanged(BR.syncState);
            }

            @Override
            public void onDatasSyncOver() {
                syncState.setNotice(mView.getString(R.string.drawer_sync_in_sync_success_msg, Conver.ConverToString(new Date())));
                syncState.setIsSpin(false);
                notifyPropertyChanged(BR.syncState);
                //更新feed 列表
                XApplication.getInstance().bus.post(new UpdateFeedListEvent());
            }
        };
    }


    @Override
    public void clear() {
        mView = null;
    }


    @Bindable
    public DrawerAdapter getDrawerAdapter() {
        return drawerAdapter;
    }

    public void setDrawerAdapter(DrawerAdapter drawerAdapter) {
        this.drawerAdapter = drawerAdapter;
        notifyPropertyChanged(BR.drawerAdapter);
    }

    @Bindable
    public AdapterView.OnItemClickListener getDrawerItemClickListener() {
        return drawerItemClickListener;
    }

    public void setDrawerItemClickListener(AdapterView.OnItemClickListener drawerItemClickListener) {
        this.drawerItemClickListener = drawerItemClickListener;
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyChange();
    }


    public void requestUser() {
        if (user == null || StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())) {
            //没有用户缓存
            return;
        }

        //验证用户有效性
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("account", user.getAccount());
        query.addWhereEqualTo("password", user.getPassword());
        query.findObjects(mView, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list == null || list.size() == 0) {
                    DBHelper.Delete.deleteUser();
                    ToastHelper.showToast(R.string.action_cancel);
                } else {
                    setUser(list.get(0));
                }
                DBHelper.UpDate.saveUser(user);
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }


    /**
     * 用户点击窗口
     */
    public void onClickUserAction(View view) {
        //无用户则弹出登录
        if (user == null || StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())) {
            XDispatcher.from(mView).dispatch(new RouterAction(LoginActivity.class, true));
            return;
        } else {
            //打开用户设置
            DialogFractory.createDialog(mView, DialogFractory.Type.UserInfo).show();
        }
    }

    /**
     * 用户点击同步信息
     *
     * @param view
     */
    public void onClickSynchro(View view) {
        if (user == null) {
            return;
        }
        synchroUserInfo();
    }

    @Bindable
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
        notifyPropertyChanged(BR.notice);
    }

    @Bindable
    public SyncState getSyncState() {
        return syncState;
    }

    public void setSyncState(SyncState syncState) {
        this.syncState = syncState;
        notifyPropertyChanged(BR.syncState);
        XLog.d("setSyncState: isspin:" + syncState.isSpin() + ",notice:" + syncState.getNotice());
    }


    public void updateUser(User user) {
        setUser(user);
    }

    /**
     * 同步用户信息
     */
    public void synchroUserInfo() {
        if (getUser() == null) {
            return;
        }
        if (getSyncState() == null) {
            return;
        }
        if (mView == null) {
            return;
        }
        if (getSyncState().isSpin()) {
            return;
        } else {
            getSyncState().setIsSpin(true);
            getSyncState().setNotice(R.string.drawer_sync_in_sync);
            notifyPropertyChanged(BR.syncState);
            BmobHelper.updateUserFeeds(mView, -1, -1, syncListener);
            BmobHelper.updateUserFavors(mView, -1, -1, syncListener);
        }
    }

}
