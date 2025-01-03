package com.potent.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.potent.R;
import com.potent.common.Constants;
import com.potent.common.beans.SpeedData;
import com.potent.common.beans.UserBeans;
import com.potent.db.dao.UserDao;
import com.potent.util.SPUtils;
import com.potent.viewmodel.MainViewModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainViewModel mainViewModel;

    private Context context;
    private SharedPreferences instance;

    private String mParam1;
    private int mParam2;

    private SpeedData speedData;

    private String unit;

    public MainUserAdapter(Context context, MainViewModel mainViewModel, String mParam1, int mParam2) {
        super();
        this.context = context;
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
        this.mainViewModel = mainViewModel;
        instance = SPUtils.getInstance(context, Constants.SPNAME, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.main_speed, null);
            return new SpeedViewHolder(view, parent.getWidth(), parent.getHeight());
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_user1, null);
            return new UserViewHolder(view, parent.getWidth(), parent.getHeight());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SpeedViewHolder) {
            SpeedViewHolder speedViewHolder = (SpeedViewHolder) holder;
            String Model = instance.getString(Constants.Model, "Single Model");
            if (Model.equals("Battle Model")) {
                speedViewHolder.itemView.findViewById(R.id.layout_user1).setVisibility(View.VISIBLE);
                UserDao userDao = new UserDao(context);
                UserBeans userBeans = userDao.queryByID(mParam2);
                if (userBeans == null) {
                    return;
                }
                speedViewHolder.userName.setText(userBeans.getUserName());
                speedViewHolder.userName1.setText(userBeans.getUserName1());
                if (speedData != null) {
                    speedViewHolder.userHead.setSelected(!speedData.isSecend());
                    speedViewHolder.userName.setTextColor(speedData.isSecend() ? Color.GRAY : Color.WHITE);
                    speedViewHolder.userHead1.setSelected(speedData.isSecend());
                    speedViewHolder.userName1.setTextColor(speedData.isSecend() ? Color.WHITE : Color.GRAY);
                }
            } else {
                UserDao userDao = new UserDao(context);
                UserBeans userBeansList = userDao.queryByID(mParam2);
                if (userBeansList == null) {
                    return;
                }
                speedViewHolder.userHead.setSelected(true);
                speedViewHolder.userName.setText(userBeansList.getUserName());
            }

            speedViewHolder.textViewkph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (instance.getString(Constants.SpeedUnits, "Km/h").equals("Km/h")) {
                        SPUtils.setSharedString(instance, Constants.SpeedUnits, "Mp/h");
                    } else {
                        SPUtils.setSharedString(instance, Constants.SpeedUnits, "Km/h");
                    }
                    mainViewModel.initUser();
                }
            });
            if (speedData != null) {

                boolean isKPH = instance.getString(Constants.SpeedUnits, "Km/h").equals("Km/h");
                speedViewHolder.speed_text.setText(speedData.getSpeed(isKPH) + "");
            }

            if (unit != null && unit != "") {
                speedViewHolder.textViewkph.setText(unit);
            }
        }


        if (holder instanceof UserViewHolder) {
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            String Model = instance.getString(Constants.Model, "Single Model");
            userViewHolder.model.setText(Model);
            if (Model.equals("Battle Model")) {
                userViewHolder.itemView.findViewById(R.id.layout_user1).setVisibility(View.VISIBLE);
                userViewHolder.itemView.findViewById(R.id.spilt_view).setVisibility(View.VISIBLE);
                UserDao userDao = new UserDao(context);
                UserBeans userBeans = userDao.queryByID(mParam2);
                userViewHolder.username.setText(userBeans.getUserName());
                userViewHolder.adapter = new SpeedAdapter(context, userBeans.getID(), false);
                userViewHolder.list_speed.setAdapter(userViewHolder.adapter);
                userViewHolder.adapter.notifyDataSetChanged();
                userViewHolder.username1.setText(userBeans.getUserName1());
                userViewHolder.adapter1 = new SpeedAdapter(context, userBeans.getID(), true);
                userViewHolder.list_speed2.setAdapter(userViewHolder.adapter1);
                userViewHolder.adapter1.notifyDataSetChanged();
            } else {
                userViewHolder.itemView.findViewById(R.id.img1).setBackgroundResource(R.drawable.header);
                userViewHolder.username.setTextColor(Color.WHITE);
                UserDao userDao = new UserDao(context);
                UserBeans userBeansList = userDao.queryByID(mParam2);
                userViewHolder.username.setText(userBeansList.getUserName());
                userViewHolder.adapter = new SpeedAdapter(context, userBeansList.getID(), false);
                userViewHolder.list_speed.setAdapter(userViewHolder.adapter);
                userViewHolder.adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setSpeed(SpeedData speed) {
        this.speedData = speed;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public class SpeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user)
        ImageView userHead;
        @BindView(R.id.iv_user1)
        ImageView userHead1;
        @BindView(R.id.tv_user)
        TextView userName;
        @BindView(R.id.tv_user1)
        TextView userName1;

        @BindView(R.id.speed_text)
        TextView speed_text;
        @BindView(R.id.textViewkph)
        TextView textViewkph;

        public SpeedViewHolder(@NonNull View itemView, int width, int height) {
            super(itemView);
            itemView.setMinimumWidth(width);
            itemView.setMinimumHeight(height);
            ButterKnife.bind(this, itemView);
        }
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.username1)
        TextView username1;
        @BindView(R.id.model)
        TextView model;
        @BindView(R.id.list_speed)
        ListView list_speed;
        @BindView(R.id.list_speed2)
        ListView list_speed2;

        private SpeedAdapter adapter;
        private SpeedAdapter adapter1;

        public UserViewHolder(@NonNull View view, int width, int height) {
            super(view);
            itemView.setMinimumWidth(getDeviceDisplayWidth(context));
            itemView.setMinimumHeight(height);
            ButterKnife.bind(this, view);
        }
    }

    public int getDeviceDisplayWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
}
