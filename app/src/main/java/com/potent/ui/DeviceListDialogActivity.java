/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.potent.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.potent.R;
import com.potent.server.BleDeviceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListDialogActivity extends Activity {
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private List<String> mDeviceMacs;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        setContentView(R.layout.device_list);

        openBluetooth();

        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);


        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mDeviceMacs = new ArrayList<>();


        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        BleDeviceManager.getInstance().setUiScanCallback(
                new BleDeviceManager.UiScanCallback() {
                    @Override
                    public void onResult(String name, String mac) {
                        mNewDevicesArrayAdapter.add(name);
                        mNewDevicesArrayAdapter.notifyDataSetChanged();
                        mDeviceMacs.add(mac);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BleDeviceManager.getInstance().stopScan();
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void scan() {
        mNewDevicesArrayAdapter.clear();
        mDeviceMacs.clear();

        // Indicate scanning in the title
        findViewById(R.id.load_more_progressbar).setVisibility(View.VISIBLE);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        BleDeviceManager.getInstance().scan();

    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            BleDeviceManager.getInstance().stopScan();
            String address = mDeviceMacs.get(arg2);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };


    /**
     * 请求打开蓝牙
     */
    private static final int REQUEST_ENABLE_BLUETOOTH = 100;

    /**
     * 是否打开蓝牙
     */
    public void openBluetooth() {
        //获取蓝牙适配器
        BluetoothAdapter bluetoothAdapter = BleDeviceManager.getInstance().getBluetoothAdapter();
        if (bluetoothAdapter != null) {//是否支持蓝牙
            if (bluetoothAdapter.isEnabled()) {//打开
                Log.i(TAG, "ble is open!");
//                showMsg("ble is open");
            } else {//未打开
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
            }
        } else {
            showMsg("bluetooth is not support");
        }
    }

    /**
     * Toast提示
     *
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
                if (BleDeviceManager.getInstance().getBluetoothAdapter().isEnabled()) {
                    //蓝牙已打开
                    showMsg("蓝牙已打开");
                } else {
                    showMsg("请打开蓝牙");
                }
            }
        }
    }

}
