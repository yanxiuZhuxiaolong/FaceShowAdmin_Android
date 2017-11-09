package com.yanxiu.gphone.faceshowadmin_android.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;
import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.ActivityManger;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.login.adapter.ClassManagerListAdapter;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 课程选择页面
 */
public class ClassManageActivity extends FaceShowBaseActivity {

    private PublicLoadLayout mRootView;
    private static final String FROM = "from";
    private static final String FROM_LEFTDRAWER = "from_LeftDrawer";//首页抽屉点击进来
    private String mFromType;//跳转来源

    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;

    @BindView(R.id.title_layout_right_txt)
    TextView title_layout_right_txt;
    @BindView(R.id.title_layout_left_img)
    ImageView title_layout_left_img;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int mSelcetPosition = -1;//选中的position


    public static void toThisAct(Activity activity, GetClazzListResponse.DataBean data) {
        Intent intent = new Intent(activity, ClassManageActivity.class);
        intent.putExtra("classInfos", data);
        activity.startActivity(intent);
    }

    private GetClazzListResponse.DataBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_class_manage_actvity);
        setContentView(mRootView);
        ButterKnife.bind(this);
        initView();

        mFromType = getIntent().getStringExtra(FROM);
        if (FROM_LEFTDRAWER.equals(mFromType)) {
            requestClassList();
        } else {
            mData = (GetClazzListResponse.DataBean) getIntent().getSerializableExtra("classInfos");
            setRecyclerViewData();
        }

    }

    private void setRecyclerViewData() {

        ClassManagerListAdapter classManagerListAdapter = new ClassManagerListAdapter(mData.getClazsInfos());
        recyclerView.setAdapter(classManagerListAdapter);
        classManagerListAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mSelcetPosition = position;
                title_layout_right_txt.setEnabled(true);
            }
        });
    }

    private void initView() {
        titleLayoutTitle.setText(R.string.manager_clazz);
        title_layout_right_txt.setText(R.string.yes);
        title_layout_right_txt.setEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.title_layout_right_txt, R.id.retry_button, R.id.title_layout_left_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_right_txt:
                SpManager.saveCurrentClassInfo(mData.getClazsInfos().get(mSelcetPosition));
                if (FROM_LEFTDRAWER.equals(mFromType)) {
                    //finish 掉首页
                    ActivityManger.finishMainActivity();
                }
                MainActivity.invoke(ClassManageActivity.this, mData.getClazsInfos().get(mSelcetPosition));
                ClassManageActivity.this.finish();
                break;
            case R.id.retry_button:
                requestClassList();
                break;
            case R.id.title_layout_left_img:
                finish();
                break;
            default:
        }
    }

    /**
     * 请求项目班级信息，如果code!=0，那么，不能进入首页，并且弹出toast提示
     */
    private void requestClassList() {
        mRootView.showLoadingView();
        GetClazzListRequest getClazzListRequest = new GetClazzListRequest();
        getClazzListRequest.token = UserInfo.getInstance().getInfo().getToken();
        getClazzListRequest.startRequest(GetClazzListResponse.class, new HttpCallback<GetClazzListResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetClazzListResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret != null && ret.getCode() == 0 && ret.getData().getClazsInfos() != null && ret.getData().getClazsInfos().size() > 0) {
                    SpManager.saveClassListInfo(ret.getData());
                    mData = ret.getData();
                    setRecyclerViewData();
                } else {
                    mRootView.showNetErrorView();
                    if (ret != null && ret.getError() != null) {
                        ToastUtil.showToast(FSAApplication.getInstance().getApplicationContext(), ret.getError().getMessage());
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.showNetErrorView();
                if (error != null) {
                    ToastUtil.showToast(FSAApplication.getInstance().getApplicationContext(), error.getMessage());
                }
            }
        });

    }

    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, ClassManageActivity.class);
        intent.putExtra(FROM, FROM_LEFTDRAWER);
        activity.startActivity(intent);
    }

}
