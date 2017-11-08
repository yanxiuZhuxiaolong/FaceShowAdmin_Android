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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.faceshowadmin_android.base.ActivityManger;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.ClassCircleFragment;
import com.yanxiu.gphone.faceshowadmin_android.course.CourseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.login.activity.LoginActivity;
import com.yanxiu.gphone.faceshowadmin_android.main.LeftDrawerListAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.ui.fragment.MainFragment;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListResponse;
import com.yanxiu.gphone.faceshowadmin_android.task.fragment.TaskFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FaceShowBaseActivity {
    @BindView(R.id.content_frame)
    FrameLayout mContentFrameLayout;
    @BindView(R.id.left_drawer)
    RelativeLayout mLeftDrawerLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.tab_main)
    View tabMain;
    @BindView(R.id.tab_course)
    View tabCourse;
    @BindView(R.id.tab_task)
    View tabTask;
    @BindView(R.id.tab_class_circle)
    View tabClassCircle;
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    @BindView(R.id.left_drawer_list)
    RecyclerView mLeftDrawerList;
    @BindView(R.id.navi_switcher)
    View mBottomView;
    @BindView(R.id.exit)
    TextView mExitView;

    private Context mContext;
    private final String TAB_MAIN = "tab_main";
    private final String TAB_TASK = "tab_task";
    private final String TAB_COURSE = "tab_course";
    private final String TAB_CLASS_CIRCLE = "tab_class_circle";

    private MainFragment mMainFragment;

    private final int mNavBarViewsCount = 4;
    private View[] mNavBarViews = new View[mNavBarViewsCount];
    private ImageView[] mNavIconViews = new ImageView[mNavBarViewsCount];
    private TextView[] mNavTextViews = new TextView[mNavBarViewsCount];
    private int mNormalNavTxtColor, mSelNavTxtColor;
    private int mLastSelectTabIndex = 0;


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

    private void initBottomBar() {
        mSelNavTxtColor = getResources().getColor(R.color.color_0065b8);
        mNormalNavTxtColor = getResources().getColor(R.color.color_aab1bd);
        mNavBarViews[0] = findViewById(R.id.tab_main);
        mNavBarViews[1] = findViewById(R.id.tab_course);
        mNavBarViews[2] = findViewById(R.id.tab_task);
        mNavBarViews[3] = findViewById(R.id.tab_class_circle);
        for (int i = 0; i < mNavBarViews.length; i++) {
            mNavIconViews[i] = mNavBarViews[i].findViewById(R.id.nav_icon);
            mNavTextViews[i] = mNavBarViews[i].findViewById(R.id.nav_txt);
        }
        mNavIconViews[0].setEnabled(false);
        checkBottomBar(0);
    }

    private void setMainContentView() {
        initBottomBar();
        initFragments();
    }

    private void initFragments() {
        fragmentManager = getSupportFragmentManager();
        mMainFragment = new MainFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_content, mMainFragment, TAB_MAIN).commit();
    }

    public void setLeftDrawer() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLeftDrawerList.setLayoutManager(linearLayoutManager);
        LeftDrawerListAdapter leftDrawerListAdapter = new LeftDrawerListAdapter(mContext,mMainFragment.getmData());
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
            default:
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

    @OnClick({R.id.tab_main, R.id.tab_course, R.id.tab_task, R.id.tab_class_circle,R.id.exit})
    public void onViewClicked(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        switch (view.getId()) {
            case R.id.tab_main:
                mNavIconViews[0].setEnabled(false);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(true);
                changeTabFragment(TAB_MAIN, 0);
                break;
            case R.id.tab_course:
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(false);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(true);
                changeTabFragment(TAB_COURSE, 1);
                break;
            case R.id.tab_task:
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(false);
                mNavIconViews[3].setEnabled(true);
                changeTabFragment(TAB_TASK, 2);
                break;
            case R.id.tab_class_circle:
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(false);
                changeTabFragment(TAB_CLASS_CIRCLE, 3);
                break;
            case R.id.exit :
                exitApp();
                mDrawerLayout.closeDrawer(mLeftDrawerLayout);
                break;
            default:
        }
    }

    private void changeTabFragment(String tabName, int index) {
        if (mLastSelectTabIndex == index)
            return;
        checkBottomBar(index);
        fragmentManager.beginTransaction().
                replace(R.id.fragment_content, fragmentManager.findFragmentByTag(tabName) != null ?
                        fragmentManager.findFragmentByTag(tabName) : createdNewFragment(tabName)).commit();
        mLastSelectTabIndex = index;
    }

    private Fragment createdNewFragment(String tab) {
        switch (tab) {
            case TAB_MAIN:
                mMainFragment = new MainFragment();
                return mMainFragment;
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

    private void checkBottomBar(int index) {
        if (index >= 0 && index < mNavBarViews.length) {
            for (int i = 0; i < mNavBarViews.length; i++) {
                if (i == index) {
                    mNavTextViews[index].setTextColor(mSelNavTxtColor);
                } else {
                    mNavTextViews[i].setTextColor(mNormalNavTxtColor);
                }
            }
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

    public void openLeftDrawer() {
        mDrawerLayout.openDrawer(mLeftDrawerLayout);
    }

    public void setBottomVisibility(int visibility) {
        if (mBottomView != null) {
            mBottomView.setVisibility(visibility);
//            findViewById(R.id.line).setVisibility(visibility);
        }
    }
}
