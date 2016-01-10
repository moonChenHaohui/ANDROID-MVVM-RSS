package com.moon.myreadapp.mvvm.viewmodels;

import android.app.Activity;
import android.databinding.Bindable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.action.RouterAction;
import com.moon.appframework.core.XApplication;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.R;
import com.moon.myreadapp.common.components.dialog.ReadSetDialog;
import com.moon.myreadapp.common.components.dialog.ShareDialog;
import com.moon.myreadapp.common.components.toast.TastyToast;
import com.moon.myreadapp.common.components.toast.ToastHelper;
import com.moon.myreadapp.common.event.UpdateFeedEvent;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.ui.ArticleWebActivity;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.ScreenUtils;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by moon on 15/11/14.
 */
public class ArticleViewModel extends BaseViewModel {

    private Activity mView;

    private Article article;

    private long articleId;

    private int position;


    public ArticleViewModel(Activity view, long id, int pos) {
        this.mView = view;
        articleId = id;
        position = pos;
        article = DBHelper.Query.getArticle(articleId);
        initViews();
        initEvents();

    }

    @Bindable
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        notifyPropertyChanged(BR.article);
    }

    /**
     * 阅读原文
     */
    public void onClickWebArticle(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARTICLE_TITLE, article.getTitle());
        bundle.putString(Constants.ARTICLE_URL, article.getLink());
        //XLog.d("hahahah" + article.getLink() + "," + article.getUri()+ ",");
        XDispatcher.from((Activity) mView).dispatch(new RouterAction(ArticleWebActivity.class, bundle, true));
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {
    }

    @Override
    public void clear() {

    }

    public void onClickButtomBar(View view) {
        if (!(view instanceof IconTextView)) {
            return;
        }
        IconTextView itv = (IconTextView) view;
        switch (itv.getId()) {
            case R.id.back:
                //返回按钮
                if (itv.getTag() == null) {
                    itv.setTag(true);
                }
                boolean isOpen = (boolean) itv.getTag();

                //获取在屏幕中位置,来确定切换的位置
                int[] position = new int[2];
                view.getLocationOnScreen(position);
                //屏幕宽度- (该控件位置+控件宽度) = 移动的距离    --控件右边贴边需要移动的距离
                int hideSize = ScreenUtils.getDisplayMetrics().widthPixels - (position[0] + view.getWidth());
                //float alpha = 0.75f;
                com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
                set.playTogether(
                        //位置平移
                        ObjectAnimator.ofFloat(view.getParent(), "translationX", isOpen ? hideSize : 0).setDuration(500),
                        //透明度
                        //ObjectAnimator.ofFloat(view.getParent(), "alpha", (isOpen ? 1 : alpha), (isOpen ? alpha : 1)),
                        //图标旋转
                        ObjectAnimator.ofFloat(view, "rotation", 0 + (isOpen ? 0 : 180), 180 + (isOpen ? 0 : 180))
                );
                set.setInterpolator(new DecelerateInterpolator());
                set.start();
                itv.setTag(!isOpen);
                break;
            case R.id.favor:
                //收藏

                boolean isFavor = article.getStatus() == Article.Status.NORMAL.status;

                boolean beFavor = !isFavor;
                //动画
                int colorDown = Globals.getApplication().getResources().getColor(R.color.unfavor);
                int colorPrimary = Globals.getApplication().getResources().getColor(R.color.favor);

                com.nineoldandroids.animation.ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "textColor",
                        beFavor ? colorPrimary : colorDown,
                        beFavor ? colorDown : colorPrimary);

                colorAnim.setDuration(1000);

                colorAnim.setEvaluator(new ArgbEvaluator());


                com.nineoldandroids.animation.AnimatorSet favorSet = new com.nineoldandroids.animation.AnimatorSet();
                favorSet.playTogether(
                        colorAnim,
                        ObjectAnimator.ofFloat(view, "scaleX", 1, 2f, 1).setDuration(1000),
                        ObjectAnimator.ofFloat(view, "scaleY", 1, 2f, 1).setDuration(1000)
                );
                favorSet.setInterpolator(new AnticipateOvershootInterpolator());
                favorSet.start();




                if (isFavor) {
                    article.setStatus(Article.Status.FAVOR.status);
                    ToastHelper.showToast(BuiltConfig.getString(R.string.action_favor) + BuiltConfig.getString(R.string.success));
                } else {
                    article.setStatus(Article.Status.NORMAL.status);
                    ToastHelper.showToast(BuiltConfig.getString(R.string.action_favor_back) + BuiltConfig.getString(R.string.success));
                }
                updateArticle(article);
                break;
            case R.id.font_set:
                new ReadSetDialog(mView).showWithView(view);
                break;
            case R.id.share:
                //分享
                new ShareDialog(mView).showWithView(view);
                break;
        }
    }

    private void updateArticle(Article article){
        DBHelper.UpDate.saveArticle(article);
        setArticle(article);
        if (position >= 0){
            UpdateFeedEvent event = new UpdateFeedEvent(null,UpdateFeedEvent.TYPE.STATUS);
            event.setUpdatePosition(position);
            event.setArticle(article);
            XApplication.getInstance().bus.post(event);
        }
    }

}
