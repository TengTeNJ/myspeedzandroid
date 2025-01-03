package com.potent.viewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.potent.common.Constants;
import com.potent.common.PotentApplication;
import com.potent.common.beans.DateData;
import com.potent.common.beans.SpeedData;
import com.potent.common.beans.TabViewBeans;
import com.potent.common.beans.UserBeans;
import com.potent.common.event.SpeedUpdate;
import com.potent.db.dao.DateDataDao;
import com.potent.db.dao.SpeedDataDao;
import com.potent.db.dao.UserDao;
import com.potent.server.BleDeviceManager;
import com.potent.ui.fragment.login.other.FragmentUser;
import com.potent.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import de.greenrobot.event.EventBus;

public class MainViewModel extends AndroidViewModel {
    public static final String INCOMING_MSG = "INCOMING_MSG";
    public static final String ALERT_MSG = "ALERT_MSG";
    private SharedPreferences instance;
    private UserDao userDao;
    private DateDataDao dateDataDao;
    private SpeedDataDao speedDataDao;
    private int userID;
    private UserBeans userBeans;
    private boolean second = false;
    boolean isKPH;

    private MutableLiveData<ArrayList<TabViewBeans>> liveUserList = new MutableLiveData<>();
    private MutableLiveData<String> liveSpeedUnits = new MutableLiveData<>();
    private MutableLiveData<SpeedData> liveSpeed = new MutableLiveData<>();
    private MutableLiveData<String> liveToast = new MutableLiveData<>();
    private MutableLiveData<Integer> liveSelected = new MutableLiveData<>();

    private NotificationReceiver mNotiReceiver;

    public MainViewModel(@NonNull Application application) {
        super(application);

        instance = SPUtils.getInstance(application,
                Constants.SPNAME, Context.MODE_PRIVATE);
        userDao = new UserDao(application);
        dateDataDao = new DateDataDao(getApplication());
        speedDataDao = new SpeedDataDao(getApplication());
        userID = instance.getInt(Constants.USERID, 0);
        mNotiReceiver = new NotificationReceiver();
    }


    public void initUser() {
        isKPH = instance.getString(Constants.SpeedUnits, "Km/h").equals("Km/h");
        List<UserBeans> userBeansList = userDao.queryForAll();
        if (null == userBeansList || userBeansList.size() == 0) {
            for (int i = 0; i < 2; i++) {
                UserBeans userBeans = new UserBeans();
                userBeans.setUserName("Default" + (i + 1));
                userDao.add(userBeans);
            }

            userBeansList = userDao.queryForAll();
            SPUtils.setSharedInt(instance, Constants.USERID, userBeansList.get(0).getID());

        }
        userID = instance.getInt(Constants.USERID, 0);
        setUserID(userID);
        userBeans = userDao.queryByID(userID);
        ArrayList<TabViewBeans> viewList = new ArrayList<TabViewBeans>();
        String Model = instance.getString(Constants.Model, "Single Model");
        int select = 0;
        int i = 0;
        if (Model.equals("Battle Model")) {
            for (UserBeans userBeans : userBeansList) {
                if (userBeans.isDoubleModel()) {
                    viewList.add(new TabViewBeans("", FragmentUser.newInstance(userBeans.getUserName(), userBeans.getID()), userBeans.getID()));
                    if (userBeans.getID() == this.userBeans.getID()) {
                        select = i;
                    }
                    i++;
                }
            }
        } else {
            for (UserBeans userBeans : userBeansList) {
                if (!userBeans.isDoubleModel()) {
                    viewList.add(new TabViewBeans("", FragmentUser.newInstance(userBeans.getUserName(), userBeans.getID()), userBeans.getID()));
                    if (userBeans.getID() == this.userBeans.getID()) {
                        select = i;
                    }
                    i++;
                }
            }
            second = false;
        }
        liveUserList.postValue(viewList);
        liveSpeedUnits.postValue(instance.getString(Constants.SpeedUnits, "Km/h"));
        liveSelected.postValue(select);
//        textViewkph.setText(i);
        System.gc();
    }

    private void checkRemoveMinData(DateData dateData, SpeedDataDao speedDataDao) {
        ArrayList<SpeedData> speedList = dateData.getOptionGroupsListDesc();
        if (userBeans.isDoubleModel()) {
            ArrayList<SpeedData> secendList = new ArrayList<>();
            for (SpeedData speedData : speedList) {
                if (speedData.isSecend() == second) {
                    secendList.add(speedData);
                }
            }
            if (secendList.size() >= 5) {
                for (SpeedData speedData : secendList) {
                    speedDataDao.deleteByID(speedData.getID());
                }
            }

        } else {
            if (speedList.size() < 10) {
                return;
            }
            int iD = getMinID(speedList);
            speedDataDao.deleteByID(iD);
        }
    }

    private int getMinID(ArrayList<SpeedData> speedList) {
        int minSpeed = Integer.MAX_VALUE;
        int iD = 0;
        for (SpeedData speedData : speedList) {
            if (speedData.getSpeed(isKPH) < minSpeed) {
                minSpeed = speedData.getSpeed(isKPH);
                iD = speedData.getID();
            }

        }
        return iD;
    }

    public void changeSecond() {
        if (!userBeans.isDoubleModel()) {
            return;
        }
        DateData dateData = dateDataDao.queryTop(userID);
        ArrayList<SpeedData> speedList = dateData.getOptionGroupsListDesc();
        ArrayList<SpeedData> secendList = new ArrayList<>();
        for (SpeedData speedData : speedList) {
            if (speedData.isSecend() == second) {
                secendList.add(speedData);
            }
        }
        if (secendList.size() == 5) {
            second = !second;
        }
    }


    private boolean isSecond(DateData dateData) {
        ArrayList<SpeedData> speedList = dateData.getOptionGroupsListDesc();
        if (speedList.size() % 10 >= 5) {
            return true;
        }
        return false;
    }

    public class NotificationReceiver extends BroadcastReceiver {
        private void updateData(int data) {
            userBeans = userDao.queryByID(userID);
            Date dateNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatterSpeed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = formatter.format(dateNow);

            DateData dateData = dateDataDao.queryTop(userID);
            if (null == dateData || !dateData.getDataTime().equals(now)) {
                dateData = new DateData();
                dateData.setDataTime(now);
                dateData.setUserBeans(userBeans);
                dateDataDao.add(dateData);
            }

            changeSecond();
            checkRemoveMinData(dateData, speedDataDao);

            SpeedData speedData = new SpeedData();
            speedData.setDate(dateData);
            speedData.setDateTime(formatterSpeed.format(dateNow));
            speedData.setSpeed(data);
            speedData.setSecend(second);
            speedDataDao.add(speedData);
            EventBus.getDefault().post(new SpeedUpdate(userID));
            liveSpeed.postValue(speedData);
            System.gc();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(INCOMING_MSG)) {
                try {
                    updateData(Integer.parseInt(intent.getExtras().getString("STR").trim()));

                } catch (Exception e) {

                }


            }  if (action.equals(ALERT_MSG)) {
                String displayMsg = intent.getExtras().getString("STR");
                liveToast.postValue(displayMsg);
                if (intent.getExtras().getInt("COUNTER") > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                    builder.setIcon(android.R.drawable.stat_notify_error)
                            .setTitle("Error")
                            .setMessage(displayMsg)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int button) {
                                }
                            })
                            .create();
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
/*			else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                Toast.makeText(BluetoothConn.this, "Bluetooth connection is disconnected!", Toast.LENGTH_LONG).show();

			}*/
            else {
            }
        }
    }

    public int getUserID() {
        return userID;
    }

    public MutableLiveData<ArrayList<TabViewBeans>> getLiveUserList() {
        return liveUserList;
    }

    public NotificationReceiver getNotiReceiver() {
        return mNotiReceiver;
    }

    public MutableLiveData<Integer> getLiveSelected() {
        return liveSelected;
    }

    public MutableLiveData<String> getLiveSpeedUnits() {
        return liveSpeedUnits;
    }

    public MutableLiveData<String> getLiveToast() {
        return liveToast;
    }

    public MutableLiveData<SpeedData> getLiveSpeed() {
        return liveSpeed;
    }

    public void setUserID(int userID) {
        this.userID = userID;
        userBeans = userDao.queryByID(userID);
        DateData dateData = dateDataDao.queryTop(userID);
        if (dateData == null) {
            return;
        }
        ArrayList<SpeedData> speedDataArrayList = dateData.getOptionGroupsListDesc();
        if (speedDataArrayList != null && speedDataArrayList.size() > 0) {
            SpeedData speedData = speedDataArrayList.get(0);
            second = speedData.isSecend();
            liveSpeed.postValue(speedData);
        } else {
            second = false;
            liveSpeed.postValue(new SpeedData());
        }
    }

    public Boolean getSecond() {
        return second;
    }

    public void test() {
        Random random = new Random();
        Intent i = new Intent(INCOMING_MSG);
        i.putExtra("STR", random.nextInt(100)+"");
        i.putExtra("COUNTER", -1);
        PotentApplication.getContext().sendBroadcast(i);
    }
}
