package com.moon.myreadapp.common.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.util.Globals;

import java.util.List;

/**
 * Created by moon on 15/11/9.
 * base recycler adapter with data binding
 */
public abstract class BaseRecyclerAdapter<E, T> extends RecyclerView.Adapter<BaseRecyclerAdapter.BindingHolder<T>> {

    protected List<E> mData;

    protected LayoutInflater mInflater;

    public BaseRecyclerAdapter(List<E> data) {
        mInflater = LayoutInflater.from(Globals.getApplication());
        mData = data;
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public E getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getClass().hashCode();
    }

    public List<E> getmData() {
        return mData;
    }

    public void setmData(List<E> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void add(E e) {
        this.mData.add(e);
        notifyDataSetChanged();
    }

    public void addAll(List<E> list) {
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.mData.remove(position);
        notifyDataSetChanged();
    }

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

}
