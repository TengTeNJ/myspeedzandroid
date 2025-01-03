package com.potent.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.potent.R;
import com.potent.common.Constants;
import com.potent.common.beans.DateData;
import com.potent.common.beans.SpeedData;
import com.potent.common.beans.UserBeans;
import com.potent.common.event.SpeedUpdate;
import com.potent.db.dao.UserDao;
import com.potent.util.SPUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Potent
 * @description: TODO<说明>
 * @date: 2014/12/28 14:23
 */
public class SpeedAdapter extends BaseAdapter {
    private Context mContext;
    private int UserID;
    private boolean secend;
    private UserDao userDao;

    public SpeedAdapter(Context mContext, int UserID, boolean secend) {
        this.mContext = mContext;
        this.UserID = UserID;
        this.secend = secend;
        userDao = new UserDao(mContext);
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(SpeedUpdate update) {
        if (update.getUserID() == UserID) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        UserBeans userBeans = userDao.queryByID(UserID);
        SharedPreferences instance = SPUtils.getInstance(mContext, Constants.SPNAME, Context.MODE_PRIVATE);
        boolean isKPH = instance.getString(Constants.SpeedUnits, "Km/h").equals("Km/h");
        listSpeed.clear();
        int i = 0;
        for (DateData dateData : userBeans.getDatesList()) {
            for (SpeedData speedData : dateData.getOptionGroupsListDesc()) {
                if (speedData.isSecend() == secend) {
                    listSpeed.add(speedData.getSpeed(isKPH) + "");
                    i = i + 1;

                }
            }
        }
        super.notifyDataSetChanged();
    }

    private ArrayList<String> listSpeed = new ArrayList<String>();

    public ArrayList<String> getListSpeed() {
        return listSpeed;
    }

    public void setListSpeed(ArrayList<String> listSpeed) {
        this.listSpeed = listSpeed;
    }

    @Override
    public int getCount() {
        return null == listSpeed ? 0 : listSpeed.size();
    }

    @Override
    public String getItem(int position) {
        return listSpeed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate
                    (R.layout.item_list_speed, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String speed = listSpeed.get(position);
        holder.item_speed.setText(speed);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item_speed)
        TextView item_speed;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
