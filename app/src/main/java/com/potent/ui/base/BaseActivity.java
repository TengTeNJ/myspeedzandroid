package com.potent.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.potent.R;
import com.potent.util.AppLanguageUtils;

import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Motk
 * @description: TODO<所有ACTIVITY的基类>
 * @date: 2014/10/15 11:47
 */
public class BaseActivity extends FragmentActivity {
    @BindView(value = R.id.text_back)
    TextView text_back;
    @BindView(value = R.id.text_title)
    TextView text_title;
    private Class<?> m_wgoBackClass = null;
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        AppLanguageUtils.changeAppLanguage(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.setContentView(layoutResID);
    }

    protected void setTile(String title) {
        text_title.setText(title);
    }

    protected void setTile(int resString) {
        text_title.setText(resString);
    }

    protected void showBack(String backTitle, Class<?> wgoBackClass) {
        text_back.setText(getString(R.string.back) + backTitle);
        m_wgoBackClass = wgoBackClass;
        text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_wgoBackClass != null) {
                    Intent backIntent = new Intent(BaseActivity.this, m_wgoBackClass);
                    startActivity(backIntent);
                } else {
                    finish();
                }
            }
        });
        text_back.setVisibility(View.VISIBLE);
    }

    @Override

    public void onBackPressed() {
        super.onBackPressed();

    }
}
