package com.moon.myreadapp.common.components.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.facebook.drawee.view.DraweeView;
import com.joanzapata.iconify.widget.IconTextView;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.databinding.FragmentShareBinding;
import com.moon.myreadapp.mvvm.models.ShareItem;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by moon on 15/12/24.
 */
public class ShareDialog extends BaseButtomDialog implements View.OnClickListener {

    private FragmentShareBinding binding;
    private Article article;

    private final static String WEIBO_PACKAGENAME = "com.sina.weibo";

    public ShareDialog(Activity context,Article a) {
        super(context);
        article = a;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {
            dismiss();
            return;
        }
    }

    @Override
    void setContentView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_share, null, true);
        setContentView(binding.getRoot());
    }

    @Override
    void init() {
        final ShareAdapter adapter = new ShareAdapter(context);
        String[] array = context.getResources().getStringArray(R.array.share_dialog_text);
        adapter.add(new ShareItem.Builder(context)
                .content(array[0])
                .icon(R.drawable.ic_wx_logo)
                .build());
        adapter.add(new ShareItem.Builder(context)
                .content(array[1])
                .icon(R.drawable.ic_wx_moments)
                .build());
        adapter.add(new ShareItem.Builder(context)
                .content(array[2])
                .icon(R.drawable.ic_wx_collect)
                .build());
        adapter.add(new ShareItem.Builder(context)
                .content(array[3])
                .icon(R.drawable.ic_sina_logo)
                .build());
        adapter.add(new ShareItem.Builder(context)
                .content(array[4])
                .icon(R.drawable.ic_share_more)
                .build());
        binding.gridview.setAdapter(adapter);
        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (article == null){
                    ToastHelper.showToast(R.string.share_fail);
                    dismiss();
                }
                switch (position) {
                    case 0:
                        shareToWeChatSession();
                        break;
                    case 1:
                        shareToWeChatTimeline();
                        break;
                    case 2:
                        shareToWeChatFavorite();
                        break;
                    case 3:
                        shareToWeibo();
                        break;
                    default:
                        share("", null);
                }
            }
        });
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = binding.shareContent.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        binding.cancel.setOnClickListener(this);

    }

    private void shareToWeibo() {
        if (isInstallApplication(WEIBO_PACKAGENAME)) {
            share(WEIBO_PACKAGENAME, null);
        } else {
            ToastHelper.showToast(R.string.share_not_install);
        }
    }

    private boolean isInstallApplication(String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void share(String packages, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        } else {
            intent.setType("text/plain");
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_title));
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_content,
                article.getTitle(),
                article.getLink(),
                context.getString(R.string.app_name),
                Constants.APP_URL
        ));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!TextUtils.isEmpty(packages))
            intent.setPackage(packages);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_title)));
    }

    private void shareToWeChatTimeline(){
        shareToWeChat(SendMessageToWX.Req.WXSceneTimeline);
    }

    private void shareToWeChatSession(){
        shareToWeChat(SendMessageToWX.Req.WXSceneSession);
    }

    private void shareToWeChatFavorite(){
        shareToWeChat(SendMessageToWX.Req.WXSceneFavorite);
    }


    private void shareToWeChat(int scene) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.APP_WX_ID, true);
        if (!api.isWXAppInstalled()) {
            ToastHelper.showToast(R.string.share_not_install);
        }
        api.registerApp(Constants.APP_WX_ID);
        WXWebpageObject object = new WXWebpageObject();
        object.webpageUrl = article.getLink();
        WXMediaMessage msg = new WXMediaMessage(object);
        msg.mediaObject = object;
        msg.thumbData = Bitmap2Bytes(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_rss_logo));
        msg.title = context.getString(R.string.share_content_title,context.getString(R.string.app_name),article.getTitle());
        msg.description = article.getCreator();
        SendMessageToWX.Req request = new SendMessageToWX.Req();
        request.message = msg;
        request.scene = scene;
        api.sendReq(request);
        api.unregisterApp();
    }

    private byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    class ShareAdapter extends ArrayAdapter<ShareItem> {


        public ShareAdapter(Context context) {
            super(context, R.layout.gv_share_item, R.id.name);
        }

        @Override
        public View getView(final int index, View convertView, ViewGroup parent) {
            final View view = super.getView(index, convertView, parent);
            final ShareItem item = getItem(index);
            DraweeView ic = (DraweeView) view.findViewById(R.id.image);
            if (item.getIcon() != 0)
                ic.setImageResource(item.getIcon());
            else
                ic.setVisibility(View.GONE);
            IconTextView tv = (IconTextView) view.findViewById(R.id.name);
            tv.setText(item.getContent());
            return view;
        }
    }

    @Override
    void onDimiss() {

    }
}
