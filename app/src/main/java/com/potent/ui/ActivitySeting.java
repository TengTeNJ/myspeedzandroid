package com.potent.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.daimajia.swipe.SwipeLayout;
import com.devspark.appmsg.AppMsg;
import com.potent.R;
import com.potent.common.Constants;
import com.potent.common.beans.UserBeans;
import com.potent.db.dao.UserDao;
import com.potent.ui.base.BaseActivity;
import com.potent.ui.view.CustomDialog;
import com.potent.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.Gravity.CENTER_VERTICAL;

public class ActivitySeting extends BaseActivity {
    @BindView(R.id.userlist)
    LinearLayout userlist;
    @BindView(R.id.first)
    ExpandableLayout first;
    @BindView(R.id.secend)
    ExpandableLayout secend;
    @BindView(R.id.third)
    ExpandableLayout third;

    @BindView(R.id.tv_set_tips_title)
    TextView tips_title;
    @BindView(R.id.tv_set_tips_message)
    TextView tips_message;
    @BindView(R.id.tv_set_tips_bottom)
    TextView tips_bottom;

    private UserDao userDao;
    private int userID;
    private SharedPreferences instance;
    private String Model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //应用内配置语言
        setContentView(R.layout.set_layout);
        ButterKnife.bind(this);
        showBack(getString(R.string.title_activity_activity_main), null);
        userDao = new UserDao(getApplicationContext());
        instance = SPUtils.getInstance(getApplicationContext(), Constants.SPNAME, Context.MODE_PRIVATE);
        userID = instance.getInt(Constants.USERID, 0);
        initView();
        initUser();
    }

    private void initView() {

        Model = instance.getString(Constants.Model, "Single Model");
        RelativeLayout firstHeadLayout = first.getHeaderRelativeLayout();
        TextView text_values = (TextView) firstHeadLayout.findViewById(R.id.text_value);
        TextView text_keys = (TextView) firstHeadLayout.findViewById(R.id.text_key);
        text_keys.setText(R.string.Model);
        text_values.setText("Single Model".equals(Model) ? R.string.single_model : R.string.battle_model);

        String SpeedUnits = instance.getString(Constants.SpeedUnits, "Km/h");
        RelativeLayout secendHeadLayout = secend.getHeaderRelativeLayout();
        TextView text_value = (TextView) secendHeadLayout.findViewById(R.id.text_value);
        TextView text_key = (TextView) secendHeadLayout.findViewById(R.id.text_key);
        text_value.setText(SpeedUnits);
        text_key.setText(R.string.speed_units);


        final String Language = instance.getString(Constants.Language, "English");
        RelativeLayout thirdHeadLayout = third.getHeaderRelativeLayout();
        TextView text_valuet = (TextView) thirdHeadLayout.findViewById(R.id.text_value);
        TextView text_keyt = (TextView) thirdHeadLayout.findViewById(R.id.text_key);
        text_valuet.setText(Language);
        text_keyt.setText(getString(R.string.language));

        RelativeLayout firstContent = first.getContentRelativeLayout();
        TextView m_value_1 = (TextView) firstContent.findViewById(R.id.value_1);
        TextView m_value_2 = (TextView) firstContent.findViewById(R.id.value_2);
        m_value_1.setText(R.string.single_model);
        m_value_2.setText(R.string.battle_model);

        RelativeLayout secendContent = secend.getContentRelativeLayout();
        TextView s_value_1 = (TextView) secendContent.findViewById(R.id.value_1);
        TextView s_value_2 = (TextView) secendContent.findViewById(R.id.value_2);
        s_value_1.setText("Km/h");
        s_value_2.setText("Mp/h");

        RelativeLayout thirdContent = third.getContentRelativeLayout();
        TextView t_value_1 = (TextView) thirdContent.findViewById(R.id.value_1);
        TextView t_value_2 = (TextView) thirdContent.findViewById(R.id.value_2);
        t_value_1.setText("English");
        t_value_2.setText("French");
        s_value_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.setSharedString(instance, Constants.SpeedUnits, "Km/h");
                secend.hide();
                initView();
            }
        });
        s_value_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.setSharedString(instance, Constants.SpeedUnits, "Mp/h");
                secend.hide();
                initView();
            }
        });
        m_value_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.setSharedString(instance, Constants.Model, "Single Model");
                first.hide();
                initView();
                checkUser();
                initUser();
            }
        });
        m_value_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.setSharedString(instance, Constants.Model, "Battle Model");
                first.hide();
                initView();
                checkUser();
                initUser();
            }
        });
        t_value_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Language.equals("English")) {
                    SPUtils.setSharedString(instance, Constants.Language, "English");
                    toastMsg(getString(R.string.effect));
                }
                third.hide();
                initView();
            }
        });
        t_value_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Language.equals("French")) {
                    SPUtils.setSharedString(instance, Constants.Language, "French");
                    toastMsg(getString(R.string.effect));
                }
                third.hide();
                initView();
            }
        });
    }

    private void checkUser() {
        List<UserBeans> userBeansList = userDao.queryForAll();
        boolean isDoubl = Model.equals("Battle Model");
        for (final UserBeans userBeans : userBeansList) {
            if (userBeans.isDoubleModel() == isDoubl) {
                userID = userBeans.getID();
                chooseUser();
                return;
            }
        }
        UserBeans userBeans = new UserBeans();
        userBeans.setUserName(getString(R.string.user1));
        userBeans.setDoubleModel(isDoubl);
        userBeans.setID1(1);
        userBeans.setUserName1(getString(R.string.user2));
        userDao.add(userBeans);
        checkUser();
    }

    private void initUser() {
        Model = instance.getString(Constants.Model, "Single Model");
        userlist.removeAllViews();
        List<UserBeans> userList = userDao.queryForAll();
        final List<UserBeans> userBeansList = new ArrayList<UserBeans>();
        boolean modelDouble = false;
        if (Model.equals("Battle Model")) {
            modelDouble = true;
        }
        for (final UserBeans userBeans : userList) {
            if (userBeans.isDoubleModel() == modelDouble) {
                userBeansList.add(userBeans);
            }
        }
        if (null == userBeansList || userBeansList.size() == 0) {
            return;
        }

        if (Model.equals("Battle Model")) {
            modelDouble = true;
        }
        for (final UserBeans userBeans : userBeansList) {
            LinearLayout viewcontent = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate
                    (R.layout.item_user, null);
            userlist.addView(viewcontent);

            final SwipeLayout swipeuser = (SwipeLayout) viewcontent.findViewById(R.id.swipeuser);
            swipeuser.setShowMode(SwipeLayout.ShowMode.LayDown);
            swipeuser.setDragEdge(SwipeLayout.DragEdge.Right);

            TextView username_text = (TextView) swipeuser.findViewById(R.id.username_text);
            username_text.setText(userBeans.getUserName() + " \t\t\t    " + (modelDouble ? userBeans.getUserName1() : ""));
            if (userID == userBeans.getID()) {
                username_text.setTextColor(getResources().getColor(R.color.select));
            }
            /*username_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeuser.open();
                }
            });*/
            swipeuser.findViewById(R.id.rename).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout viewcontent = (LinearLayout) LayoutInflater.from(ActivitySeting.this).inflate
                            (R.layout.layout_input_username, null);
                    TextView textView = (TextView) viewcontent.findViewById(R.id.text_new_user);
                    textView.setText(R.string.new_name);
                    final EditText editText = (EditText) viewcontent.findViewById(R.id.edit_user);
                    TextView textView1 = (TextView) viewcontent.findViewById(R.id.text_new_user1);
                    textView1.setText(R.string.new_name);
                    final EditText editText1 = (EditText) viewcontent.findViewById(R.id.edit_user1);
                    if (Model.equals("Single Model")) {
                        textView1.setVisibility(View.GONE);
                        editText1.setVisibility(View.GONE);
                    }
                    CustomDialog.Builder customBuilder = new
                            CustomDialog.Builder(ActivitySeting.this);
                    customBuilder.setContentView(viewcontent)
                            .setNegativeButton(getString(R.string.confirm),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String s = editText.getText().toString().trim();
                                            String s2 = editText1.getText().toString().trim();
                                            if (Model.equals("Single Model")) {
                                                if (!s.equals("")) {
                                                    userBeans.setUserName(s);
                                                    userDao.updateUser(userBeans);
                                                    initUser();
                                                    dialog.dismiss();
                                                }
                                            } else {
                                                if (!s.equals("") && !s2.equals("")) {
                                                    userBeans.setUserName(s);
                                                    userBeans.setUserName1(s2);
                                                    userDao.updateUser(userBeans);
                                                    initUser();
                                                    dialog.dismiss();
                                                }
                                            }
                                        }
                                    }
                            )
                            .setPositiveButton(getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }
                            );

                    CustomDialog dialog = customBuilder.create();
                    dialog.show();
                }
            });
            swipeuser.findViewById(R.id.chooseuser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog.Builder customBuilder = new
                            CustomDialog.Builder(ActivitySeting.this);
                    customBuilder.setMessage("Sure Choose User?")
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
                                            userID = userBeans.getID();
                                            chooseUser();
                                            initUser();
                                            dialog.dismiss();
                                        }
                                    });
                    CustomDialog dialog = customBuilder.create();
                    dialog.show();
                }
            });
            swipeuser.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userBeansList.size() > 1) {
                        CustomDialog.Builder customBuilder = new
                                CustomDialog.Builder(ActivitySeting.this);
                        customBuilder.setMessage("Sure delete User?")
                                .setPositiveButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }
                                )
                                .setNegativeButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (userID == userBeans.getID()) {
                                                    for (UserBeans user : userBeansList) {
                                                        if (userBeans != user) {
                                                            userID = user.getID();
                                                            break;
                                                        }
                                                    }
                                                    chooseUser();
                                                }
                                                userDao.deletUser(userBeans);
                                                initUser();
                                                dialog.dismiss();
                                            }
                                        }
                                );
                        CustomDialog dialog = customBuilder.create();
                        dialog.show();
                    }
                }

            });

        }
        refreshTips();
    }

    private void chooseUser() {
        SharedPreferences instance = SPUtils.getInstance(getApplicationContext(),
                Constants.SPNAME, Context.MODE_PRIVATE);
        SPUtils.setSharedInt(instance, Constants.USERID, userID);
    }

    @OnClick(R.id.adduser)
    public void addUser() {

        LinearLayout viewcontent = (LinearLayout) LayoutInflater.from(ActivitySeting.this).inflate
                (R.layout.layout_input_username, null);
        TextView textView = (TextView) viewcontent.findViewById(R.id.text_new_user);
        textView.setText(R.string.new_user_name);
        final EditText editText = (EditText) viewcontent.findViewById(R.id.edit_user);

        TextView textView1 = (TextView) viewcontent.findViewById(R.id.text_new_user1);
        textView1.setText(R.string.new_user_name);
        final EditText editText1 = (EditText) viewcontent.findViewById(R.id.edit_user1);
        if (Model.equals("Single Model")) {
            textView1.setVisibility(View.GONE);
            editText1.setVisibility(View.GONE);
        }
        CustomDialog.Builder customBuilder = new
                CustomDialog.Builder(ActivitySeting.this);
        customBuilder.setContentView(viewcontent)
                .setNegativeButton(getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String s = editText.getText().toString().trim();
                                String s2 = editText1.getText().toString().trim();
                                if (Model.equals("Single Model")) {
                                    if (!s.equals("")) {
                                        UserBeans userBeans = new UserBeans();
                                        userBeans.setUserName(s);
                                        userDao.add(userBeans);
                                        initUser();
                                        dialog.dismiss();
                                    }
                                } else {
                                    if (!s.equals("") && !s2.equals("")) {
                                        UserBeans userBeans = new UserBeans();
                                        userBeans.setUserName(s);
                                        userBeans.setDoubleModel(true);
                                        userBeans.setID1(5000);
                                        userBeans.setUserName1(s2);
                                        userDao.add(userBeans);
                                        initUser();
                                        dialog.dismiss();
                                    }
                                }

                            }
                        }
                )
                .setPositiveButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }
                );

        CustomDialog dialog = customBuilder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_seting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void refreshTips() {
        if ("Battle Model".equals(Model)) {
            tips_title.setText(getString(R.string.model2));
            tips_message.setText(getString(R.string.model_messge2));
            tips_bottom.setText(getString(R.string.model_bottom2));
        } else {
            tips_title.setText(getString(R.string.model1));
            tips_message.setText(getString(R.string.model_messge1));
            tips_bottom.setText(getString(R.string.model_bottom1));
        }
    }
}
