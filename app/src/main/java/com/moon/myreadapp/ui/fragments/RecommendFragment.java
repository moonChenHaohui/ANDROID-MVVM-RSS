package com.moon.myreadapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.myreadapp.R;
import com.moon.myreadapp.mvvm.viewmodels.AddFeedViewModel;

/**
 * Created by moon on 15/12/17.
 */
public class RecommendFragment extends Fragment {

    private static String VM = "VIEW_MODEL";
    private AddFeedViewModel addFeedViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView && addFeedViewModel != null) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            recyclerView.setAdapter(addFeedViewModel.getSystemRecAdapter());
        }
        return view;
    }


}
