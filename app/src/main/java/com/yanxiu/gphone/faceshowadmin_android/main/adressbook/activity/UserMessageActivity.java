package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.dialog.ChooseSexDialog;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.PersonalDetailsRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.UserMessageUpdataRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.PersonalDetailsResponse;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.CornersImageTarget;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.UUID;

public class UserMessageActivity extends FaceShowBaseActivity implements View.OnClickListener, ChooseSexDialog.OnViewClickListener {

    private static final String UPDATA_HEADIMG="headimg";
    private static final String UPDATA_NAME="name";
    private static final String UPDATA_SEX="sex";
    private static final String UPDATA_SCHOOL="school";

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mBackView;
    private TextView mTitleView;
    private ImageView mHeadImgView;
    private TextView mNameView;
    private TextView mMobileView;
    private TextView mSexView;
    private TextView mStageView;
    private TextView mSubjectView;
    private TextView mSchoolView;

    private ChooseSexDialog mSexDialog;

    private PersonalDetailsResponse.PersonalDetailsData mDetailsData;

    private UUID mDetailsRequest;
    private UUID mUpdataRequest;

    public static void LuanchActivity(Context context){
        Intent intent=new Intent(context,UserMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_user_message);
        setContentView(rootView);
        mSexDialog=new ChooseSexDialog(mContext);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDetailsRequest!=null){
            RequestBase.cancelRequestWithUUID(mDetailsRequest);
            mDetailsRequest=null;
        }
        if (mUpdataRequest!=null){
            RequestBase.cancelRequestWithUUID(mUpdataRequest);
            mUpdataRequest=null;
        }
    }

    private void initView() {
        mBackView = findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.title_layout_title);

        mHeadImgView=findViewById(R.id.iv_head_img);
        mNameView=findViewById(R.id.tv_name);
        mMobileView=findViewById(R.id.tv_mobile);
        mSexView=findViewById(R.id.tv_sex);
        mStageView=findViewById(R.id.tv_stage);
        mSubjectView=findViewById(R.id.tv_subject);
        mSchoolView=findViewById(R.id.tv_school);
    }

    private void listener() {
        mBackView.setOnClickListener(this);
        rootView.setRetryButtonOnclickListener(this);
        mHeadImgView.setOnClickListener(this);
        mNameView.setOnClickListener(this);
        mMobileView.setOnClickListener(this);
        mSexView.setOnClickListener(this);
        mStageView.setOnClickListener(this);
        mSubjectView.setOnClickListener(this);
        mSchoolView.setOnClickListener(this);
        mSexDialog.setClickListener(this);
    }

    private void initData() {
        mTitleView.setText("我的资料");
        startUserMessageRequest();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.retry_button:
                startUserMessageRequest();
                break;
            case R.id.iv_head_img:
                break;
            case R.id.tv_name:
                break;
            case R.id.tv_sex:
                if (mSexDialog==null){
                    mSexDialog=new ChooseSexDialog(mContext);
                }
                mSexDialog.show();
                mSexDialog.show();
                break;
            case R.id.tv_school:
                break;

                /*
                 * 以下暂时未开放
                 * **/
            case R.id.tv_mobile:
                break;
            case R.id.tv_stage:
                break;
            case R.id.tv_subject:
                break;
        }
    }

    private void startUserMessageRequest(){
        rootView.showLoadingView();
        rootView.hiddenNetErrorView();
        PersonalDetailsRequest detailsRequest=new PersonalDetailsRequest();
        detailsRequest.userId= String.valueOf(UserInfo.getInstance().getInfo().getUserId());
        mDetailsRequest=detailsRequest.startRequest(PersonalDetailsResponse.class, new HttpCallback<PersonalDetailsResponse>() {
            @Override
            public void onSuccess(RequestBase request, PersonalDetailsResponse ret) {
                rootView.hiddenLoadingView();
                if (ret!=null&&ret.data!=null&&ret.getCode()==0){
                    mDetailsData=ret.data;
                    setUserMessage(ret.data);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                rootView.showNetErrorView();
                ToastUtil.showToast(mContext,error.getMessage());
            }
        });
    }

    private void setUserMessage(PersonalDetailsResponse.PersonalDetailsData message) {
        Glide.with(mContext).load(message.avatar).asBitmap().placeholder(R.drawable.classcircle_headimg_small).centerCrop().into(new CornersImageTarget(mContext, mHeadImgView, 10));
        mMobileView.setText(message.mobilePhone);
        mNameView.setText(message.realName);
        mSchoolView.setText(message.school);
        mStageView.setText(message.stageName);
        mSubjectView.setText(message.subjectName);
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

    @Override
    public void onChoosekMan() {
        startUpdataUserMessage(mDetailsData.realName,"1",mDetailsData.school,mDetailsData.avatar,UPDATA_SEX);
    }

    @Override
    public void onChooseWoman() {
        startUpdataUserMessage(mDetailsData.realName,"0",mDetailsData.school,mDetailsData.avatar,UPDATA_SEX);
    }

    private void startUpdataUserMessage(final String realName, final String sex, final String schoolName, final String url, final String updataType){
        rootView.showLoadingView();
        UserMessageUpdataRequest updataRequest=new UserMessageUpdataRequest();
        updataRequest.realName=realName;
        updataRequest.schoolName=schoolName;
        updataRequest.sex=sex;
        updataRequest.url=url;
        mUpdataRequest=updataRequest.startRequest(FaceShowBaseResponse.class, new HttpCallback<FaceShowBaseResponse>() {
            @Override
            public void onSuccess(RequestBase request, FaceShowBaseResponse ret) {
                mUpdataRequest=null;
                rootView.hiddenLoadingView();
                if (ret!=null) {
                    if (ret.getCode() == 0) {
                        ToastUtil.showToast(mContext, "保存成功");
                    } else {
                        ToastUtil.showToast(mContext, ret.getMessage());
                    }
                    switch (updataType){
                        case UPDATA_HEADIMG:
                            mDetailsData.avatar=url;
                            UserInfo.getInstance().getInfo().setAvatar(url);
                            SpManager.saveUserInfo(UserInfo.getInstance().getInfo());
                            break;
                        case UPDATA_NAME:
                            mDetailsData.realName=realName;
                            UserInfo.getInstance().getInfo().setRealName(realName);
                            SpManager.saveUserInfo(UserInfo.getInstance().getInfo());
                            break;
                        case UPDATA_SCHOOL:
                            mDetailsData.school=schoolName;
                            UserInfo.getInstance().getInfo().setSchool(schoolName);
                            SpManager.saveUserInfo(UserInfo.getInstance().getInfo());
                            break;
                        case UPDATA_SEX:
                            mDetailsData.sex=Integer.parseInt(sex);
                            UserInfo.getInstance().getInfo().setSex(mDetailsData.sex);
                            String sex;
                            switch (mDetailsData.sex) {
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
                            UserInfo.getInstance().getInfo().setSexName(sex);
                            SpManager.saveUserInfo(UserInfo.getInstance().getInfo());
                            break;
                    }
                    setUserMessage(mDetailsData);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mUpdataRequest=null;
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext,error.getMessage());
            }
        });
    }

}
