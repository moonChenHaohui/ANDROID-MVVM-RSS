package com.moon.myreadapp.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.R;
import com.moon.myreadapp.databinding.FragmentRecommendListBinding;
import com.moon.myreadapp.mvvm.viewmodels.AddFeedViewModel;

/**
 * Created by moon on 15/12/17.
 */
public class RecommendFragment extends Fragment {

    private static String VM = "VIEW_MODEL";
    private AddFeedViewModel addFeedViewModel;
    private FragmentRecommendListBinding binding;


    public RecommendFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public RecommendFragment createWithViewModel(AddFeedViewModel addFeedViewModel) {
        this.addFeedViewModel = addFeedViewModel;
        return this;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (addFeedViewModel != null) {
            addFeedViewModel.loadSystemData(binding.empty);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_recommend_list,container,false);
        if (addFeedViewModel != null) {
            binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext()));
            //binding.list.setAdapter(addFeedViewModel.getSystemRecAdapter());
            binding.setAdapter(addFeedViewModel.getSystemRecAdapter());
            binding.empty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFeedViewModel.loadSystemData(binding.empty);
                }
            });
        }
        return binding.getRoot();
    }


}
