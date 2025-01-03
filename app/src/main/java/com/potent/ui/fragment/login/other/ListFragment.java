package com.potent.ui.fragment.login.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.potent.R;
import com.potent.common.Constants;
import com.potent.common.PotentApplication;
import com.potent.common.beans.DateData;
import com.potent.common.beans.SpeedBeans;
import com.potent.common.beans.SpeedData;
import com.potent.common.beans.UserBeans;
import com.potent.db.dao.DateDataDao;
import com.potent.db.dao.UserDao;
import com.potent.ui.ActivityList;
import com.potent.ui.adapter.ListAdapter;
import com.potent.ui.view.CustomDialog;
import com.potent.util.SPUtils;

import java.util.Collection;
import java.util.List;

import static com.potent.ui.ActivityMain.INCOMING_MSG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    @BindView(R.id.list_speed)
    ListView list_speed;
    @BindView(R.id.action_clear)
    TextView action_clear;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.arrow_left)
    ImageView arrow_left;
    @BindView(R.id.arrow_right)
    ImageView arrow_right;

    ListAdapter adapter;
    DateDataDao dateDataDao;
    private UserDao userDao;
    private SharedPreferences instance;
    private String SpeedUnits;
    private int userID;
    UserBeans userBeans;
    private boolean isSecond;
    private String Model;
    boolean isKPH;

    public ListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(boolean isSecond) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putBoolean("isSecond", isSecond);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter mFilter01 = new IntentFilter(INCOMING_MSG);
        PotentApplication.getContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                userDao = new UserDao(getContext());
                userBeans = userDao.queryByID(userID);
                loadData();
            }
        }, mFilter01);
        Bundle args = getArguments();
        if (args != null) {
            isSecond = args.getBoolean("isSecond");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        instance = SPUtils.getInstance(getContext(),
                Constants.SPNAME, Context.MODE_PRIVATE);
        SpeedUnits = instance.getString(Constants.SpeedUnits, "Km/h");
        isKPH = SpeedUnits.equals("Km/h");
        Model = instance.getString(Constants.Model, "Single Model");
        userID = instance.getInt(Constants.USERID, 0);
        userDao = new UserDao(getContext());
        userBeans = userDao.queryByID(userID);
        userName.setText(isSecond ? userBeans.getUserName1() : userBeans.getUserName());
        if ("Battle Model".equals(Model)) {
            if (isSecond) {
                arrow_left.setVisibility(View.VISIBLE);
            } else {
                arrow_right.setVisibility(View.VISIBLE);
            }

        }

//        userName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!Model.equals("Single Model")) {
//                    userName.setText(showSecend ? userBeans.getUserName1() : userBeans.getUserName());
//                    showSecend = !showSecend;
//                    loadData();
//                }
//            }
//        });

        adapter = new ListAdapter(getContext(), userBeans);
        list_speed.setAdapter(adapter);
        dateDataDao = new DateDataDao(getContext());
        loadData();
        action_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.Builder customBuilder = new
                        CustomDialog.Builder(getContext());
                customBuilder.setMessage("Sure delete all data?")
                        .setPositiveButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        List<DateData> dataList = userBeans.getDatesList();
                                        if (null != dataList && dataList.size() > 0) {
                                            for (DateData dateData : dataList) {
                                                dateDataDao.deleteByID(dateData.getID());
                                            }
                                        }
                                        loadData();
                                        dialog.dismiss();
                                    }
                                });
                CustomDialog dialog = customBuilder.create();
                dialog.show();
            }
        });
    }

    private void loadData() {
        if (adapter == null) {
            return;
        }
        adapter.clearData();

        List<DateData> dataList = userBeans.getDatesList();
        if (null != dataList && dataList.size() > 0) {
            for (DateData dateData : dataList) {
                Collection<SpeedData> speedDatas = dateData.getOptionGroupsListDesc();
                for (SpeedData speedData : speedDatas) {
                    if (speedData.isSecend() == isSecond) {
                        adapter.addSpeed(new SpeedBeans(speedData.getID(), speedData.getDateTime(), speedData.getSpeed(isKPH) + SpeedUnits, speedData.isSecend()));
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}