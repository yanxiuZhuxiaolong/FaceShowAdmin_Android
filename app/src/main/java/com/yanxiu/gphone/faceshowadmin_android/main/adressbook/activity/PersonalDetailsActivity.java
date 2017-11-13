package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.PersonalDetailsRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.PersonalDetailsSignRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.PersonalDetailsResponse;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.PersonalDetailsSignResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.CornersImageTarget;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 14:56.
 * Function :
 */
public class PersonalDetailsActivity extends FaceShowBaseActivity implements View.OnClickListener {

    private static final String KEY_USERID = "key_userid";
    private static final String KEY_ISTEACHER="key_isteacher";

    private Context mContext;
    private PublicLoadLayout rootView;
    private ImageView mBackView;
    private TextView mTitleView;
    private ImageView mHeadImgView;
    private TextView mNameView;
    private TextView mMobileView;
    private TextView mSexView;
    private TextView mStageView;
    private TextView mSubjectView;
    private TextView mSchoolView;
    private TextView mRateView;
    private ImageView mSginRecordView;
    private View mIsTeacher1View;
    private View mIsTeacher2View;
    private View mIsTeacher3View;
    private View mIsTeacher4View;

    private boolean isTeacher=false;
    private String mUserId;
    private String mUserName;

    private UUID mSignRequest;
    private UUID mDetailsRequest;

    public static void LuanchActivity(Context context, String userId,boolean isTeacher){
        Intent intent = new Intent(context, PersonalDetailsActivity.class);
        intent.putExtra(KEY_USERID, userId);
        intent.putExtra(KEY_ISTEACHER,isTeacher);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_personaldetails);
        setContentView(rootView);
        mUserId = getIntent().getStringExtra(KEY_USERID);
        isTeacher=getIntent().getBooleanExtra(KEY_ISTEACHER,false);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSignRequest!=null){
            RequestBase.cancelRequestWithUUID(mSignRequest);
            mSignRequest=null;
        }
        if (mDetailsRequest!=null){
            RequestBase.cancelRequestWithUUID(mDetailsRequest);
            mDetailsRequest=null;
        }
    }

    private void initView() {
        mBackView = findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.title_layout_title);

        mHeadImgView = findViewById(R.id.iv_head_img);
        mNameView = findViewById(R.id.tv_name);
        mMobileView = findViewById(R.id.tv_mobile);
        mSexView = findViewById(R.id.tv_sex);
        mStageView = findViewById(R.id.tv_stage);
        mSubjectView = findViewById(R.id.tv_subject);
        mSchoolView = findViewById(R.id.tv_school);
        mRateView = findViewById(R.id.tv_rate);
        mSginRecordView = findViewById(R.id.iv_sign_record);

        mIsTeacher1View=findViewById(R.id.ll_is_teacher_false_1);
        mIsTeacher2View=findViewById(R.id.ll_is_teacher_false_2);
        mIsTeacher3View=findViewById(R.id.ll_is_teacher_false_3);
        mIsTeacher4View=findViewById(R.id.ll_is_teacher_false_4);
    }

    private void listener() {
        mBackView.setOnClickListener(this);
        mSginRecordView.setOnClickListener(this);
        rootView.setRetryButtonOnclickListener(this);
    }

    private void initData() {
        mTitleView.setText("资料详情");
        startPersonalDetailsRequest();

        if (isTeacher){
            mIsTeacher1View.setVisibility(View.GONE);
            mIsTeacher2View.setVisibility(View.GONE);
            mIsTeacher3View.setVisibility(View.GONE);
            mIsTeacher4View.setVisibility(View.GONE);
        }else {
            mIsTeacher1View.setVisibility(View.VISIBLE);
            mIsTeacher2View.setVisibility(View.VISIBLE);
            mIsTeacher3View.setVisibility(View.VISIBLE);
            mIsTeacher4View.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.retry_button:
                startPersonalDetailsRequest();
                break;
            case R.id.iv_sign_record:
                SignRecordActivity.LuanchActivity(mContext,mUserId,mUserName);
                break;
        }
    }

    /**
     * 签到率
     * */
    private void startPersonalDetailsSignRequest() {
//        rootView.showLoadingView();
        PersonalDetailsSignRequest signRequest = new PersonalDetailsSignRequest();
        signRequest.userId = mUserId;
        mSignRequest=signRequest.startRequest(PersonalDetailsSignResponse.class, new HttpCallback<PersonalDetailsSignResponse>() {
            @Override
            public void onSuccess(RequestBase request, PersonalDetailsSignResponse ret) {
                mSignRequest=null;
                rootView.hiddenLoadingView();
                if (ret != null && ret.data != null && ret.getCode() == 0) {
                    setPersonalSignMessage(ret.data);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mSignRequest=null;
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext, error.getMessage());
            }
        });
    }

    /**
     * 个人详情
     * */
    private void startPersonalDetailsRequest() {
        rootView.hiddenNetErrorView();
        rootView.showLoadingView();
        PersonalDetailsRequest detailsRequest = new PersonalDetailsRequest();
        detailsRequest.userId = mUserId;
        mDetailsRequest=detailsRequest.startRequest(PersonalDetailsResponse.class, new HttpCallback<PersonalDetailsResponse>() {
            @Override
            public void onSuccess(RequestBase request, PersonalDetailsResponse ret) {
//                rootView.hiddenLoadingView();
                mDetailsRequest=null;
                if (ret != null && ret.data != null && ret.getCode() == 0) {
                    setPersonalMessage(ret.data);
                    startPersonalDetailsSignRequest();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mDetailsRequest=null;
                rootView.hiddenLoadingView();
                rootView.showNetErrorView();
                ToastUtil.showToast(mContext, error.getMessage());
            }
        });
    }

    private void setPersonalMessage(PersonalDetailsResponse.PersonalDetailsData message) {
        Glide.with(mContext).load(message.avatar).asBitmap().placeholder(R.drawable.classcircle_headimg_small).centerCrop().into(new CornersImageTarget(mContext, mHeadImgView, 10));
        mMobileView.setText(message.mobilePhone);
        mNameView.setText(message.realName);
        mUserName=message.realName;
        if (!TextUtils.isEmpty(message.school)) {
            mSchoolView.setText(message.school);
        }else {
            mSchoolView.setText("暂无");
        }
        if (!TextUtils.isEmpty(message.stageName)) {
            mStageView.setText(message.stageName);
        }else {
            mStageView.setText("暂无");
        }
        if (!TextUtils.isEmpty(message.subjectName)) {
            mSubjectView.setText(message.subjectName);
        }else {
            mSubjectView.setText("暂无");
        }
        String sex;
        switch (message.sex) {
            case 0:
                sex = "女";
                break;
            case 1:
                sex = "男";
                break;
            default:
                sex = "未知";
                break;
        }
        mSexView.setText(sex);
    }

    private void setPersonalSignMessage(PersonalDetailsSignResponse.PersonalDetailsSignData data) {
        mRateView.setText(getPercent(data.userSigninNum,data.totalSigninNum));
    }

    String getPercent(int y, int z) {
        if (y == 0) {
            return "0%";
        } else {
            String baifenbi;// 接受百分比的值
            double baiy = y * 1.0;
            double baiz = z * 1.0;
            double fen = baiy / baiz;
            // NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法
            // nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
            DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
            // 百分比格式，后面不足2位的用0补齐
            // baifenbi=nf.format(fen);
            baifenbi = df1.format(fen);
            System.out.println(baifenbi);
            return baifenbi;
        }
    }
}
