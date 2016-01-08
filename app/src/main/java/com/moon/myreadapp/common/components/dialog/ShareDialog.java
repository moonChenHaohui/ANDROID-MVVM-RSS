package com.moon.myreadapp.common.components.dialog;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
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
import com.moon.myreadapp.databinding.FragmentShareBinding;
import com.moon.myreadapp.mvvm.models.ShareItem;

/**
 * Created by moon on 15/12/24.
 */
public class ShareDialog extends BaseButtomDialog implements View.OnClickListener {

    private FragmentShareBinding binding;

    public ShareDialog(Activity context) {
        super(context);
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
                ToastHelper.showToast("click position:" + position);
//                switch (position) {
//                    case 0:
//                        shareToWeChatSession();
//                        break;
//                    case 1:
//                        shareToWeChatTimeline();
//                        break;
//                    case 2:
//                        shareToWeChatFavorite();
//                        break;
//                    case 3:
//                        shareToWeibo();
//                        break;
//                    default:
//                        share("", null);
//                }
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
