package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.dialog.ChooseSexDialog;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.AddStudentRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.AddStudentResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.UUID;
/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 16:52.
 * Function :
 */
public class AddStudentActivity extends FaceShowBaseActivity implements View.OnClickListener, ChooseSexDialog.OnViewClickListener, TextWatcher {

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mBackView;
    private TextView mTitleView;
    private TextView mFunctionView;
    private EditText mNameView;
    private EditText mSexView;
    private LinearLayout mChooseSexView;
    private EditText mMobileView;
    private EditText mSchoolView;

    private ChooseSexDialog mSexDialog;

    private boolean isNameReady=false;
    private boolean isSexReady=false;
    private boolean isMobileReady=false;
    private boolean isSchoolReady=true;

    private UUID mAddStudentRequest;

    public static void LuanchActivity(Context context){
        Intent intent=new Intent(context,AddStudentActivity.class);
        ((Activity)context).startActivityForResult(intent,1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_add_student);
        setContentView(rootView);
        mSexDialog=new ChooseSexDialog(mContext);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAddStudentRequest!=null){
            RequestBase.cancelRequestWithUUID(mAddStudentRequest);
            mAddStudentRequest=null;
        }
    }

    private void initView() {
        mBackView = findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.title_layout_title);
        mFunctionView=findViewById(R.id.title_layout_right_txt);
        mFunctionView.setVisibility(View.VISIBLE);

        mNameView=findViewById(R.id.ed_name);
        mSexView=findViewById(R.id.tv_sex);
        mChooseSexView=findViewById(R.id.iv_choose_sex);
        mMobileView=findViewById(R.id.ed_mobile);
        mSchoolView=findViewById(R.id.ed_school);
    }

    private void listener() {
        mBackView.setOnClickListener(this);
        mFunctionView.setOnClickListener(this);
        mChooseSexView.setOnClickListener(this);
        mSexDialog.setClickListener(this);
        mMobileView.addTextChangedListener(this);
        mNameView.addTextChangedListener(this);
        mSchoolView.addTextChangedListener(this);
    }

    @SuppressLint("ResourceType")
    private void initData() {
        mTitleView.setText("添加成员");
        mFunctionView.setText("保存");
        mFunctionView.setTextColor(ContextCompat.getColorStateList(mContext,R.drawable.selector_addstudent_save_button));
        mFunctionView.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.title_layout_right_txt:
                String name=mNameView.getText().toString();
                String mobile=mMobileView.getText().toString();
                String school=mSchoolView.getText().toString();
                String sex = (String) mSexView.getTag();

                startCreateStudentRequest(name,sex,mobile,school);
                break;
            case R.id.iv_choose_sex:
                if (mSexDialog==null){
                    mSexDialog=new ChooseSexDialog(mContext);
                }
                mSexDialog.show();
                break;
        }
    }

    private void startCreateStudentRequest(String realName,String sex,String mobile,String schoolName){
        rootView.showLoadingView();
        AddStudentRequest addStudentRequest=new AddStudentRequest();
        addStudentRequest.mobilePhone=mobile;
        addStudentRequest.realName=realName;
        addStudentRequest.school=schoolName;
        addStudentRequest.sex=sex;
        mAddStudentRequest=addStudentRequest.startRequest(AddStudentResponse.class, new HttpCallback<AddStudentResponse>() {
            @Override
            public void onSuccess(RequestBase request, AddStudentResponse ret) {
                mAddStudentRequest=null;
                rootView.hiddenLoadingView();
                if (ret!=null&&ret.getCode()==0){
                    ToastUtil.showToast(mContext,"保存成功");
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    AddStudentActivity.this.finish();
                }else {
                    try {
                        assert ret != null;
                        ToastUtil.showToast(mContext,ret.getError().getMessage());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mAddStudentRequest=null;
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext,error.getMessage());
            }
        });
    }

    @Override
    public void onChoosekMan() {
        mSexView.setText("男");
        mSexView.setTag("1");
        isSexReady=true;
        setButtonCanClick();
    }

    @Override
    public void onChooseWoman() {
        mSexView.setText("女");
        mSexView.setTag("0");
        isSexReady=true;
        setButtonCanClick();
    }

    private void setButtonCanClick(){
        if (isMobileReady&&isNameReady&&isSchoolReady&&isSexReady){
            mFunctionView.setEnabled(true);
        }else {
            mFunctionView.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String name=mNameView.getText().toString();
        String mobile=mMobileView.getText().toString();
        String school=mSchoolView.getText().toString();

        if (name.length()>0){
            isNameReady=true;
        }else {
            isNameReady=false;
        }

        if (mobile.length()==11){
            isMobileReady=true;
        }else {
            isMobileReady=false;
        }

//        if (school.length()>0){
//            isSchoolReady=true;
//        }else {
//            isSchoolReady=false;
//        }
        setButtonCanClick();
    }
}
