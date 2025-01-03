package com.potent.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.potent.R;
import com.potent.common.Constants;
import com.potent.common.PotentApplication;
import com.potent.common.beans.DateData;
import com.potent.common.beans.SpeedData;
import com.potent.common.beans.UserBeans;
import com.potent.db.dao.UserDao;
import com.potent.ui.base.BaseActivity;
import com.potent.util.SPUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;

import static com.potent.ui.ActivityMain.INCOMING_MSG;

public class DynamicalAddingActivity extends BaseActivity implements OnChartValueSelectedListener {
    @BindView(R.id.chart1)
    LineChart mChart;

    @BindView(R.id.name1)
    TextView name1;
    @BindView(R.id.name2)
    TextView name2;
    @BindView(R.id.rl_name2)
    RelativeLayout rl_name2;
    private SharedPreferences instance;
    private UserDao userDao;
    private int userID;
    boolean isKPH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_linechart_noseekbar);
        ButterKnife.bind(this);
        showBack(getString(R.string.title_activity_activity_main), null);
        instance = SPUtils.getInstance(getApplicationContext(), Constants.SPNAME, Context.MODE_PRIVATE);
        isKPH = instance.getString(Constants.SpeedUnits, "Km/h").equals("Km/h");
        String SpeedUnits = instance.getString(Constants.SpeedUnits, "Km/h");
        userID = instance.getInt(Constants.USERID, 0);
        userDao = new UserDao(getApplicationContext());
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawYValues(false);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        addEmptyData();
        addDataSet();
        mChart.invalidate();
        IntentFilter mFilter01 = new IntentFilter(INCOMING_MSG);
        PotentApplication.getContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                userDao = new UserDao(context);
                addEmptyData();
                addDataSet();
            }
        }, mFilter01);
    }


    private void addDataSet() {

        LineData data = mChart.getDataOriginal();

        if (data != null) {
            UserBeans userBeansList = userDao.queryByID(userID);
            //for(UserBeans userBeans:userBeansList){
            if (!userBeansList.isDoubleModel()) {
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                DateData topDate = userBeansList.getTopDate();
                if (null != topDate) {
                    ArrayList<SpeedData> speedDatas = topDate.getOptionGroupsList();
                    int length = speedDatas.size();
                    for (int i = 0; i < length; i++) {
                        SpeedData speedData = speedDatas.get(i);
                        yVals.add(new Entry((float) (speedData.getSpeed(isKPH)), i));
                    }
                }
                LineDataSet set = new LineDataSet(yVals, userBeansList.getUserName());
                set.setLineWidth(2.5f);
                set.setCircleSize(3.0f);

                int color = Color.rgb(0, 0, 225);

                set.setColor(color);
                set.setCircleColor(color);
                set.setHighLightColor(color);

                data.addDataSet(set);
                rl_name2.setVisibility(View.GONE);
                name1.setText(userBeansList.getUserName());
            } else {
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                DateData topDate = userBeansList.getTopDate();
                if (null != topDate) {
                    ArrayList<SpeedData> speedDatas = topDate.getOptionGroupsList();
                    ArrayList<SpeedData> speedDatasnew = new ArrayList<>();
                    for (SpeedData speednew : speedDatas) {
                        if (!speednew.isSecend()) {
                            speedDatasnew.add(speednew);
                        }
                    }
                    int length = speedDatasnew.size();
                    for (int i = 0; i < length; i++) {
                        SpeedData speedData = speedDatasnew.get(i);
                        yVals.add(new Entry((float) (speedData.getSpeed(isKPH)), i));
                    }
                }
                LineDataSet set = new LineDataSet(yVals, userBeansList.getUserName());
                set.setLineWidth(2.5f);
                set.setCircleSize(3.0f);

                int color = Color.rgb(0, 0, 225);

                set.setColor(color);
                set.setCircleColor(color);
                set.setHighLightColor(color);

                data.addDataSet(set);
                ArrayList<Entry> yVals1 = new ArrayList<Entry>();
                DateData topDate1 = userBeansList.getTopDate();
                if (null != topDate1) {
                    ArrayList<SpeedData> speedDatas = topDate.getOptionGroupsList();
                    ArrayList<SpeedData> speedDatasnew = new ArrayList<>();
                    for (SpeedData speednew : speedDatas) {
                        if (speednew.isSecend()) {
                            speedDatasnew.add(speednew);
                        }
                    }
                    int length = speedDatasnew.size();
                    for (int i = 0; i < length; i++) {
                        SpeedData speedData = speedDatasnew.get(i);
                        yVals1.add(new Entry((float) (speedData.getSpeed(isKPH)), i));
                    }
                }
                LineDataSet set1 = new LineDataSet(yVals1, userBeansList.getUserName1());
                set1.setLineWidth(2.5f);
                set1.setCircleSize(3.0f);

                int color1 = Color.rgb(200, 0, 11);

                set1.setColor(color1);
                set1.setCircleColor(color1);
                set1.setHighLightColor(color1);

                data.addDataSet(set1);

                rl_name2.setVisibility(View.VISIBLE);
                name1.setText(userBeansList.getUserName());
                name2.setText(userBeansList.getUserName1());
            }

        }
        // create 10 y-vals

        mChart.notifyDataSetChanged();
        mChart.invalidate();
        //}
    }

    private void addEmptyData() {

        // create 30 x-vals
        UserBeans userBeansList = userDao.queryByID(userID);
        int length = 10;
        String[] xVals = new String[length];

        for (int i = 0; i < length; i++)
            xVals[i] = i + 1 + "";

        // create a chartdata object that contains only the x-axis labels (no entries or datasets)
        LineData data = new LineData(xVals);

        mChart.setData(data);
        mChart.setYRange(1f, 300f, true);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        Toast.makeText(this, e.getVal() + " " + (isKPH ? "Km/h" : "Mp/h"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dynamical, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionAddEntry:
                addEntry();
                Toast.makeText(this, "Entry added!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.actionRemoveEntry:
                removeLastEntry();
                Toast.makeText(this, "Entry removed!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.actionAddDataSet:
                addDataSet();
                Toast.makeText(this, "DataSet added!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.actionRemoveDataSet:
                removeDataSet();
                Toast.makeText(this, "DataSet removed!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.actionAddEmptyLineData:
                addEmptyData();
                Toast.makeText(this, "Empty data added!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.actionClear:
                mChart.clear();
                Toast.makeText(this, "Chart cleared!", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }*/

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleSize(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }
}
