package com.potent.ui.fragment.login.other;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.potent.R;
import com.potent.common.Constants;
import com.potent.ui.adapter.MainUserAdapter;
import com.potent.util.SPUtils;
import com.potent.viewmodel.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentUser extends Fragment {

    @BindView(R.id.user_recycle)
    RecyclerView recyclerView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MainViewModel mainViewModel;


    private String mParam1;
    private int mParam2;

    MainUserAdapter mainUserAdapter;

    public static FragmentUser newInstance(String param1, int param2) {
        FragmentUser fragment = new FragmentUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentUser() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_user_fragment, container, false);
        ButterKnife.bind(this, view);
        mainUserAdapter = new MainUserAdapter(getActivity(), mainViewModel, mParam1, mParam2);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
        mPagerSnapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mainUserAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel.getLiveSpeed().observe(getActivity(), speed -> {
            if (mParam2 != mainViewModel.getUserID()) {
                return;
            }
            mainUserAdapter.setSpeed(speed);
            mainUserAdapter.notifyDataSetChanged();
        });
        mainViewModel.getLiveSpeedUnits().observe(getActivity(), unit -> {
            mainUserAdapter.setUnit(unit);
            mainUserAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mainUserAdapter.notifyDataSetChanged();
    }
}
