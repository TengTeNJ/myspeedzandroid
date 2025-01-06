package com.potent.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.potent.R;
import com.potent.common.Constants;
import com.potent.common.beans.DateData;
import com.potent.common.beans.SpeedData;
import com.potent.common.beans.TabViewBeans;
import com.potent.common.beans.UserBeans;
import com.potent.common.event.SpeedUpdate;
import com.potent.db.dao.DateDataDao;
import com.potent.db.dao.SpeedDataDao;
import com.potent.db.dao.UserDao;
import com.potent.server.BleDeviceManager;
import com.potent.ui.adapter.TabViewPageAdapter;
import com.potent.ui.base.BaseActivity;
import com.potent.ui.fragment.login.other.FragmentUser;
import com.potent.util.SPUtils;
import com.potent.viewmodel.MainViewModel;
import com.zyq.easypermission.EasyPermission;
import com.zyq.easypermission.EasyPermissionHelper;
import com.zyq.easypermission.EasyPermissionResult;
import com.zyq.easypermission.bean.PermissionAlertInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static android.content.Context.*;
import static android.view.Gravity.CENTER_VERTICAL;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

public class ActivityMain extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.test)
    TextView test;
    @BindView(R.id.action_chart)
    TextView action_chart;
    @BindView(R.id.action_list)
    TextView action_list;
    @BindView(R.id.action_setup)
    TextView action_setup;
    @BindView(R.id.action_help)
    TextView action_help;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.arrow_left)
    ImageView arrow_left;
    @BindView(R.id.arrow_right)
    ImageView arrow_right;
    private String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
    };

    private TabViewPageAdapter adapter;
    private final int MY_REQUEST_CODE = 1001;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static final String INCOMING_MSG = "INCOMING_MSG";
    public static final String ALERT_MSG = "ALERT_MSG";
    private BluetoothAdapter mBluetoothAdapter = null;
    private MainViewModel mainViewModel;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;  // This is the request code

    //    @Override
//    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter) {
//        if (Build.VERSION.SDK_INT >= 34 && getApplicationInfo().targetSdkVersion >= 34) {
//            return super.registerReceiver(receiver, filter, Context.RECEIVER_VISIBLE_TO_INSTANT_APPS);
//        } else {
//            return super.registerReceiver(receiver, filter);
//        }
//    }
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        action_chart.setOnClickListener(this);
        action_list.setOnClickListener(this);
        action_setup.setOnClickListener(this);
        action_help.setOnClickListener(this);
        test.setOnClickListener(v -> mainViewModel.test());
        findViewById(R.id.button_device).setVisibility(View.VISIBLE);

        IntentFilter mFilter01;
        mFilter01 = new IntentFilter(INCOMING_MSG);
//        registerReceiver(mainViewModel.getNotiReceiver(), mFilter01);
        // 检查并申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 (API 31) 及以上版本
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // 申请蓝牙扫描和连接权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_BLUETOOTH_PERMISSIONS);
            }
        } else {
            // Android 12 以下版本，申请常规蓝牙权限和位置权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请普通蓝牙和位置权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_BLUETOOTH_PERMISSIONS);
            }
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            // 在 Android 14 及以上版本中，需要指定接收器是否导出
//            //String permission = android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
//           registerReceiver(mainViewModel.getNotiReceiver(), new IntentFilter(ALERT_MSG), String.valueOf(RECEIVER_EXPORTED), null);
//           // registerReceiver(mNotiReceiver, filter, Context.RECEIVER_NOT_EXPORTED, null);
//        } else {
//            // 在 Android 13 及以下版本中，不需要指定导出行为
//           // registerReceiver(mNotiReceiver, filter);
//            registerReceiver(mainViewModel.getNotiReceiver(), new IntentFilter(ALERT_MSG));
//        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(mainViewModel.getNotiReceiver(), mFilter01, Context.RECEIVER_EXPORTED);
            registerReceiver(mainViewModel.getNotiReceiver(), new IntentFilter(ALERT_MSG), Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(mainViewModel.getNotiReceiver(), mFilter01);
            registerReceiver(mainViewModel.getNotiReceiver(), new IntentFilter(ALERT_MSG));
        }
        //registerReceiver(mainViewModel.getNotiReceiver(), new IntentFilter(ALERT_MSG));
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // BleDeviceManager.getInstance();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            toastMsg("Bluetooth is not available");
            finish();
        }
        initObserve();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel.initUser();
    }


    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
        }
    }

    private void initObserve() {
        mainViewModel.getLiveUserList().observe(this, viewList -> {
            adapter = new TabViewPageAdapter(getSupportFragmentManager());
            ArrayList<TabViewBeans> list = new ArrayList<>(viewList);
            refreshArrow(list);
            adapter.setListView(list);
            pager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int i, float v, int i2) {

                }

                @Override
                public void onPageSelected(int i) {
                    SharedPreferences instance = SPUtils.getInstance(getApplicationContext(),
                            Constants.SPNAME, MODE_PRIVATE);
                    int userId = adapter.getM_w_views().get(i).getUserId();
                    mainViewModel.setUserID(userId);
                    SPUtils.setSharedInt(instance, Constants.USERID, userId);
                    refreshArrow(list);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        });

        mainViewModel.getLiveSelected().observe(this, select -> {
            pager.setCurrentItem(select);
        });

        mainViewModel.getLiveToast().observe(this, this::toastMsg);
    }

    private void refreshArrow(ArrayList<TabViewBeans> list) {
        arrow_left.setVisibility(View.GONE);
        arrow_right.setVisibility(View.GONE);
        int selected = 0;
        if (list == null || list.isEmpty() || list.size() == 1) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId() == mainViewModel.getUserID()) {
                selected = i;
                break;
            }
        }
        if (selected == 0) {
            arrow_right.setVisibility(View.VISIBLE);
        } else if (selected == list.size() - 1) {
            arrow_left.setVisibility(View.VISIBLE);
        } else {
            arrow_left.setVisibility(View.VISIBLE);
            arrow_right.setVisibility(View.VISIBLE);
        }


    }

    /**
     * 重载方法
     */
    private long firstTime;

    /**
     * 重载方法 连续返回两次，退出APP
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        if (System.currentTimeMillis() - firstTime < 3000) {
            finish();
        } else {
            firstTime = System.currentTimeMillis();
            toastMsg(getString(R.string.again_to_exit));
        }
    }

    /**
     * TODO<显示提醒>
     *
     * @param msg
     * @return void
     * @throw
     */
    private void toastMsg(String msg) {
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.button_primary_pressed_edge);
        AppMsg appMsg = AppMsg.makeText(this, msg, style);
        appMsg.setLayoutGravity(CENTER_VERTICAL);
        appMsg.setPriority(AppMsg.PRIORITY_HIGH);
        appMsg.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_chart:
                startActivity(new Intent(ActivityMain.this, DynamicalAddingActivity.class));
                break;
            case R.id.action_list:
                startActivity(new Intent(ActivityMain.this, ActivityList.class));
                break;
            case R.id.action_setup:
                startActivity(new Intent(ActivityMain.this, ActivitySeting.class));
                break;
            case R.id.action_help:
                startActivity(new Intent(ActivityMain.this, ActivityHelp.class));
                break;
        }
    }

    @OnClick(R.id.button_device)
    public void onDeviceClick() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            gotoDevice();
            return;
        }
        EasyPermission.build()
                .mRequestCode(MY_REQUEST_CODE)//请求code，自己定义
                .mPerms(permissions)//权限，可支持多个
                .mResult(new EasyPermissionResult() {//回调
                    @Override
                    public void onPermissionsAccess(int requestCode) {
                        //权限已通过
                        super.onPermissionsAccess(requestCode);
                        gotoDevice();
                    }

                    @Override
                    public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                        super.onPermissionsDismiss(requestCode, permissions);
                        Log.e("onPermissionsDismiss", permissions.toString());
                        //权限被拒绝
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String s : permissions) {
                            stringBuilder.append(s);
                        }
                        Toast.makeText(ActivityMain.this, stringBuilder, Toast.LENGTH_LONG).show();
                    }

                }).requestPermission();
    }

    private void gotoDevice() {
        Intent serverIntent = new Intent(ActivityMain.this, DeviceListDialogActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListDialogActivity.EXTRA_DEVICE_ADDRESS);
                    BleDeviceManager.getInstance().connect(address, new BleDeviceManager.UiConnectCallback() {
                        @Override
                        public void onSuccess() {
                            if (ActivityCompat.checkSelfPermission(ActivityMain.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            String name = BleDeviceManager.getInstance().getSelectDevice().getName();
                            sendBroadcast(name + " has connected. ", BleDeviceManager.ALERT_MSG, -1);

                        }

                        @Override
                        public void onFailed() {

                        }
                    });
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                } else {
                    // User did not enable Bluetooth or an error occured
                    toastMsg(getString(R.string.bt_not_enabled_leaving));
                    finish();
                }
                break;

            case MY_REQUEST_CODE:
                EasyPermissionHelper.getInstance().onActivityResult(requestCode, resultCode, data);
                break;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissionHelper.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void sendBroadcast(String str, String action, int num) {
        String displayString = null;
			/*aa	if (action.equals(BluetoothConn.OUTGOING_MSG)){
					displayString = "Me : "+str;
				} else if (action.equals(BluetoothConn.INCOMING_MSG)){
					displayString = deviceName+" : "+str;
				} else*/
        {
            displayString = str;
        }
        Intent i = new Intent(action);
        i.putExtra("STR", displayString);
        i.putExtra("COUNTER", num);
        sendBroadcast(i);
    }
}
