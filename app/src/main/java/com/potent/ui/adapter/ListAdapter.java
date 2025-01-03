package com.potent.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.potent.R;
import com.potent.common.beans.SpeedBeans;
import com.potent.common.beans.UserBeans;
import com.potent.db.dao.DateDataDao;
import com.potent.db.dao.SpeedDataDao;
import com.potent.db.dao.UserDao;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by 高鹏 on 2014/10/26.
 */
public class ListAdapter extends BaseAdapter{
    private Context mContext;
    private UserBeans userBeans;
    public ListAdapter(Context mContext,UserBeans userBeans) {
        this.mContext = mContext;
        speedDataDao=new SpeedDataDao(mContext);
        this.userBeans=userBeans;
    }

    ArrayList<SpeedBeans> listSpeed=new ArrayList<SpeedBeans>();
    SpeedDataDao speedDataDao;

    @Override
    public int getCount() {
        return listSpeed.size();
    }
    public void clearData(){
        listSpeed.clear();
    }
    @Override
    public SpeedBeans getItem(int position) {
        return listSpeed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addSpeed(SpeedBeans speed){
        listSpeed.add(speed);
    }

    public void setListSpeed(ArrayList<SpeedBeans> listSpeed) {
        this.listSpeed = listSpeed;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView!=null){
            holder=(ViewHolder)convertView.getTag();
        }else{

            convertView= LayoutInflater.from(parent.getContext().getApplicationContext()).inflate
                    (R.layout.item_list,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        final SpeedBeans speed=listSpeed.get(position);
        holder.swipeHolder.item_date.setText(speed.getDate());
        holder.swipeHolder.item_speed.setText(speed.getSpeed());
        return convertView;
    }
    class ViewHolder{
        @BindView(R.id.swipeuser)
        LinearLayout swipeuser;
        swipeHolder swipeHolder;
        ViewHolder(View view){
            ButterKnife.bind(this,view);
            swipeHolder=new swipeHolder(swipeuser);
        }
    }
    class swipeHolder{
        @BindView(R.id.item_date)
        TextView item_date;
        @BindView(R.id.item_speed)
        TextView item_speed;
        swipeHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
