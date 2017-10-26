package com.yanxiu.gphone.faceshowadmin_android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.classCircle.ClassCircleFragment;
import com.yanxiu.gphone.faceshowadmin_android.course.CourseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.main.MainFragment;
import com.yanxiu.gphone.faceshowadmin_android.task.TaskFragment;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
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


    }

    @OnClick({R.id.tab_main, R.id.tab_course, R.id.tab_task, R.id.tab_class_circle})
    public void onViewClicked(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        switch (view.getId()) {
            case R.id.tab_main:
                changeTabFragment(TAB_MAIN);
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

}
