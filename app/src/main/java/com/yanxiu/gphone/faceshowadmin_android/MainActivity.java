package com.yanxiu.gphone.faceshowadmin_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.faceshowadmin_android.base.ActivityManger;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.CheckInNotesActivity;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.ClassCircleFragment;
import com.yanxiu.gphone.faceshowadmin_android.course.CourseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.login.activity.LoginActivity;
import com.yanxiu.gphone.faceshowadmin_android.main.LeftDrawerListAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.MainFragment;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListResponse;
import com.yanxiu.gphone.faceshowadmin_android.task.TaskFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FaceShowBaseActivity {
    @BindView(R.id.content_frame)
    FrameLayout mContentFrameLayout;
    @BindView(R.id.left_drawer)
    FrameLayout mLeftDrawerLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.tab_main)
    TextView tabMain;
    @BindView(R.id.tab_course)
    TextView tabCourse;
    @BindView(R.id.tab_task)
    TextView tabTask;
    @BindView(R.id.tab_class_circle)
    TextView tabClassCircle;
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    @BindView(R.id.left_drawer_list)
    RecyclerView mLeftDrawerList;

    private Context mContext;
    private final String TAB_MAIN = "tab_main";
    private final String TAB_TASK = "tab_task";
    private final String TAB_COURSE = "tab_course";
    private final String TAB_CLASS_CIRCLE = "tab_class_circle";


    private FragmentManager fragmentManager;

    private DrawerLayout.DrawerListener mDrawerToggle = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    public static void invoke(Activity activity, GetClazzListResponse.DataBean.ClazsInfosBean clazsInfosBean) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("classInfo", clazsInfosBean);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PublicLoadLayout publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_main);
        setContentView(publicLoadLayout);
        ButterKnife.bind(this);
        mContext = this;
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setMainContentView();
        setLeftDrawer();

    }

    private void setMainContentView() {
        initFragments();

    }

    private void initFragments() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_content, new MainFragment(), TAB_MAIN).commit();
    }

    private void setLeftDrawer() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLeftDrawerList.setLayoutManager(linearLayoutManager);
        LeftDrawerListAdapter leftDrawerListAdapter = new LeftDrawerListAdapter(mContext);
        mLeftDrawerList.setAdapter(leftDrawerListAdapter);
        leftDrawerListAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LeftDrawerListItemToOtherAct(position);
            }
        });


    }

    private void LeftDrawerListItemToOtherAct(int position) {
        switch (position) {
            case 0://class_home_page
                toClassHomePage();
                break;
            case 1://my data
                toMyDataAct();
                break;
            case 2://Complaint suggestion
                toComplaintSuggestionAct();
                break;
            case 3://settings
                toSettingAct();
                break;
            case 4://exit app
                exitApp();
                break;
        }
        mDrawerLayout.closeDrawer(mLeftDrawerLayout);
    }

    private void toClassHomePage() {

    }

    private void toMyDataAct() {

    }

    private void toComplaintSuggestionAct() {

    }

    private void toSettingAct() {

    }

    private void exitApp() {
        LoginActivity.toThisAct(this);
        UserInfo.getInstance().setInfo(null);
        SpManager.saveToken("");
//                Constants.UPDATA_TYPE=0;
        SpManager.loginOut();//设置为登出状态
        this.finish();
    }

    @OnClick({R.id.tab_main, R.id.tab_course, R.id.tab_task, R.id.tab_class_circle})
    public void onViewClicked(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        switch (view.getId()) {
            case R.id.tab_main:
                changeTabFragment(TAB_MAIN);
                break;
            case R.id.tab_course:
                changeTabFragment(TAB_COURSE);
                break;
            case R.id.tab_task:
                changeTabFragment(TAB_TASK);
                break;
            case R.id.tab_class_circle:
                changeTabFragment(TAB_CLASS_CIRCLE);
                break;
            default:
        }
    }

    private void changeTabFragment(String tabName) {
        fragmentManager.beginTransaction().
                replace(R.id.fragment_content, fragmentManager.findFragmentByTag(tabName) != null ?
                        fragmentManager.findFragmentByTag(tabName) : createdNewFragment(tabName)).commit();

    }

    private Fragment createdNewFragment(String tab) {
        switch (tab) {
            case TAB_MAIN:
                return new MainFragment();
            case TAB_COURSE:
                return new CourseFragment();
            case TAB_TASK:
                return new TaskFragment();
            case TAB_CLASS_CIRCLE:
                return new ClassCircleFragment();
            default:
                return null;
        }
    }

    /**
     * 退出间隔时间戳
     */
    private long mBackTimestamp = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - mBackTimestamp <= 2000) {
                //Todo 退出程序
                ActivityManger.destoryAll();
            } else {
                mBackTimestamp = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.app_exit_tip), Toast.LENGTH_SHORT).show();
            }
            return false;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }


}
