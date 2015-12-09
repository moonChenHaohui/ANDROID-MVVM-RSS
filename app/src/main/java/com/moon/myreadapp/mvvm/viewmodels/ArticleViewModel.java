package com.moon.myreadapp.mvvm.viewmodels;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.databinding.Bindable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.joanzapata.iconify.widget.IconTextView;
import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.log.XLog;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XDispatcher;
import com.moon.myreadapp.BR;
import com.moon.myreadapp.R;
import com.moon.myreadapp.constants.Constants;
import com.moon.myreadapp.mvvm.models.dao.Article;
import com.moon.myreadapp.ui.ArticleWebActivity;
import com.moon.myreadapp.ui.LoginActivity;
import com.moon.myreadapp.ui.base.IViews.IView;
import com.moon.myreadapp.util.DBHelper;
import com.moon.myreadapp.util.Globals;
import com.moon.myreadapp.util.ScreenUtils;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;

import de.halfbit.tinybus.Subscribe;

/**
 * Created by moon on 15/11/14.
 */
public class ArticleViewModel extends BaseViewModel {

    private IView mView;

    private Article article;

    private long articleId;


    public ArticleViewModel(IView view, long id) {
        this.mView = view;
        articleId = id;
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

    /**
     * 用户点击登陆
     */
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
                float alpha = 0.75f;
                com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
                set.playTogether(
                        //位置平移
                        ObjectAnimator.ofFloat(view.getParent(), "translationX", isOpen ? hideSize : 0).setDuration(500),
                        //透明度
                        ObjectAnimator.ofFloat(view.getParent(), "alpha", (isOpen ? 1 : alpha), (isOpen ? alpha : 1)),
                        //图标旋转
                        ObjectAnimator.ofFloat(view, "rotation", 0 + (isOpen ? 0 : 180), 180 + (isOpen ? 0 : 180))
                );
                set.setInterpolator(new DecelerateInterpolator());
                set.start();
                itv.setTag(!isOpen);
                break;
            case R.id.favor:
                //收藏
                if (itv.getTag() == null) {
                    itv.setTag(false);
                }
                boolean isFavor = (boolean) itv.getTag();
                itv.setTag(!isFavor);

                TypedValue typedValue = new TypedValue();
                view.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
                int colorPrimary = typedValue.data;
                int colorDown = Globals.getApplication().getResources().getColor(R.color.txt_active);

                com.nineoldandroids.animation.ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "textColor",
                        isFavor ? colorPrimary : colorDown,
                        isFavor ? colorDown : colorPrimary);
                colorAnim.setDuration(500);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
                break;
            case R.id.font_set:
                break;
            case R.id.share:
                //分享
                break;
        }
    }


}
