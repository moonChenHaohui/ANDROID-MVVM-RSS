package com.moon.myreadapp.common.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.util.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 15/11/9.
 * base recycler adapter with data binding
 * add head \foot list;
 */
public abstract class BaseRecyclerAdapter<E, T> extends RecyclerView.Adapter<BaseRecyclerAdapter.BindingHolder<T>> {


    private ArrayList<View> mHeadViews;
    private ArrayList<View> mFooterViews;
    final static int TYPE_HEAD = 1 << 10;
    final static int TYPE_FOOT = 1 << 11;

    private int headerPosition = 0;
    private int footerPosition = 0;
    protected List<E> mData;

    protected LayoutInflater mInflater;

    public BaseRecyclerAdapter(List<E> data) {
        mInflater = LayoutInflater.from(Globals.getApplication());
        mData = data;
    }


    public void addFooter(View view) {
        if (null == mFooterViews) {
            mFooterViews = new ArrayList<>();
        }
        mFooterViews.add(view);
    }


    public void addHeader(View view) {
        if (null == mHeadViews) {
            mHeadViews = new ArrayList<>();
        }
        mHeadViews.add(view);
    }

    private int getHeaderSize() {
        return null == mHeadViews ? 0 : mHeadViews.size();
    }

    private int getFooterSize() {
        return null == mFooterViews ? 0 : mFooterViews.size();
    }

    /**
     * 判断是否是头部
     *
     * @param pos
     * @return
     */
    private boolean isHeader(int pos) {
        if (null == mHeadViews) return false;
        return pos >= 0 && pos < getHeaderSize();
    }

    /**
     * 判断是否是底部
     *
     * @param position
     * @return
     */
    public boolean isFooter(int position) {
        if (null == mFooterViews) return false;
        return position < getItemCount() && position >= getItemCount() - getFooterSize();
    }


    /**
     * 根据位置配置不同的view类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEAD;
        } else if (isFooter(position)) {
            return TYPE_FOOT;
        } else {
            int truePos = position - getHeaderSize();
            if (truePos >= 0 && truePos < mData.size()) {
                //todo 这里应当对 data 的type进行判断
                return getItemCoreViewType(truePos);
            }
        }
        return RecyclerView.INVALID_TYPE;
    }

    protected abstract int getItemCoreViewType (int truePos);

    @Override
    public int getItemCount() {
        return getHeaderSize() + getFooterSize() + mData.size();
    }


    public E getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {

        int truePos = position - getHeaderSize();
        if (mData != null && truePos >= 0 && truePos < mData.size()) {
            return mData.get(truePos).getClass().hashCode();
        }
        return -1;
    }

    public List<E> getmData() {
        return mData;
    }

    public void setmData(List<E> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void add(E e) {
        //this.mData.add(e);
        add(e, mData.size());
    }
    public void add(E e,int pos) {
        this.mData.add(pos, e);
        notifyItemInserted(getHeaderSize() + pos);
    }

    public void addAll(List<E> list) {
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.mData.remove(getHeaderSize() + position);
        notifyItemRemoved(getHeaderSize() + position);
    }



    @Override
    public void onBindViewHolder(BindingHolder<T> holder, int position) {
        int truePos = position - getHeaderSize();
        if (mData != null && truePos >= 0 && truePos < mData.size()) {
            onBindCoreViewHolder(holder, truePos);
        }
    }

    /**
     * you should realize this method.
     * @param holder
     * @param truePos
     */
    protected abstract void onBindCoreViewHolder(BindingHolder<T> holder, int truePos);


    @Override
    public BindingHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new OtherViewHolder(mHeadViews.get(headerPosition++));
        } else if (viewType == TYPE_FOOT) {
            return new OtherViewHolder(mFooterViews.get(footerPosition++));
        } else {
            return onCreateCoreViewHolder(parent, viewType);
        }
    }

    /**
     * you should realize this method.
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract BindingHolder<T> onCreateCoreViewHolder (ViewGroup parent, int viewType);


    public static class BindingHolder<T> extends RecyclerView.ViewHolder {

        private T binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = (T)itemView.getTag();
        }

        public T getBinding() {
            return binding;
        }

        public void setBinding(T binding) {
            this.binding = binding;
        }
    }

    private class OtherViewHolder<T>  extends BindingHolder<T> {

        public OtherViewHolder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

}
