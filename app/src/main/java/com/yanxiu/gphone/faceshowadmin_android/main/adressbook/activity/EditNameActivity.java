package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.util.UserMessageUpdateManager;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import static com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity.UserMessageActivity.RESULT_NAME;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 16:52.
 * Function :
 */
public class EditNameActivity extends FaceShowBaseActivity implements View.OnClickListener, UserMessageUpdateManager.onRequestCallback, TextWatcher {

    private static final String KEY_NAME="key_name";

    private Context mContext;
    private PublicLoadLayout rootView;

    private UserMessageUpdateManager mUpdateManager;
    private View mBackView;
    private TextView mTitleView;
    private TextView mFunctionView;
    private EditText mNameView;
    private ImageView mClearView;

    private String mName;

    public static void LuanchActivity(Context context,int requestCode,String name){
        Intent intent=new Intent(context,EditNameActivity.class);
        intent.putExtra(KEY_NAME,name);
        ((Activity)context).startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_edit_name);
        setContentView(rootView);
        mUpdateManager=UserMessageUpdateManager.create();
        mName=getIntent().getStringExtra(KEY_NAME);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUpdateManager.requestFinish();
    }

    private void initView() {
        mBackView = findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.title_layout_title);
        mFunctionView=findViewById(R.id.title_layout_right_txt);
        mFunctionView.setVisibility(View.VISIBLE);

        mNameView=findViewById(R.id.ed_name);
        mClearView=findViewById(R.id.iv_clear);
    }

    private void listener() {
        mBackView.setOnClickListener(this);
        mFunctionView.setOnClickListener(this);
        mNameView.addTextChangedListener(this);
        mClearView.setOnClickListener(this);
    }

    @SuppressLint("ResourceType")
    private void initData() {
        mTitleView.setText("编辑姓名");
        mFunctionView.setText("保存");
        mFunctionView.setTextColor(ContextCompat.getColorStateList(mContext,R.drawable.selector_addstudent_save_button));
        mFunctionView.setEnabled(false);

        if (!TextUtils.isEmpty(mName)){
            mNameView.setText(mName);
            mNameView.setSelection(mName.length());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.iv_clear:
                mNameView.setText("");
                break;
            case R.id.title_layout_right_txt:
                mName=mNameView.getText().toString();
                mUpdateManager.updataRealName(mName,this);
                break;
        }
    }

    @Override
    public void onHttpStart() {
        rootView.showLoadingView();
    }

    @Override
    public void onSuccess(FaceShowBaseResponse response, String updateMessage, String updataType) {
        if (response!=null) {
            if (response.getCode() == 0) {
                ToastUtil.showToast(mContext,"保存成功");
                Intent intent=new Intent();
                intent.putExtra(RESULT_NAME,mName);
                setResult(RESULT_OK,intent);
                EditNameActivity.this.finish();
            } else {
                ToastUtil.showToast(mContext, response.getMessage());
            }
        }
    }

    @Override
    public void onError(Error error) {
        ToastUtil.showToast(mContext,error.getMessage());
    }

    @Override
    public void onHttpFinish() {
        rootView.hiddenLoadingView();
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
        if (name.length()>0){
            mFunctionView.setEnabled(true);
        }else {
            mFunctionView.setEnabled(false);
        }
    }
}
