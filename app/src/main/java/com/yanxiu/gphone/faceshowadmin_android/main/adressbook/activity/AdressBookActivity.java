package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.adapter.AdressBookAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.AdressBookRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.AdressBookPeople;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.AdressBookResponse;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 10:07.
 * Function :
 */
public class AdressBookActivity extends FaceShowBaseActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.LoadMoreListener, AdressBookAdapter.onItemClickListener, View.OnClickListener {

    private Context mContext;
    private PublicLoadLayout rootView;
    private ImageView mBackView;
    private TextView mTitleView;
    private ImageView mFunctionImgView;
    private TextView mFunctionTextView;
    private AdressBookAdapter mAdapter;
    private int mOffset=0;
    private int mPeopleIndex=0;
    private SwipeRefreshLayout mRefreshLayout;
    private LoadMoreRecyclerView mAdressBookView;
    private UUID mBookRequest;

    public static void LuanchActivity(Context context){
        Intent intent=new Intent(context,AdressBookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_adressbook);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBookRequest!=null){
            RequestBase.cancelRequestWithUUID(mBookRequest);
            mBookRequest=null;
        }
    }

    private void initView() {
        mBackView = findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.title_layout_title);
        mFunctionImgView=findViewById(R.id.title_layout_right_img);
        mFunctionTextView=findViewById(R.id.title_layout_signIn);
        mFunctionImgView.setVisibility(View.VISIBLE);
        mFunctionTextView.setVisibility(View.VISIBLE);

        mRefreshLayout=findViewById(R.id.sw_adress);
        mAdressBookView=findViewById(R.id.recy_adress);
        mAdressBookView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter=new AdressBookAdapter(mContext);
        mAdressBookView.setAdapter(mAdapter);
        mAdressBookView.setLoadMoreEnable(true);
    }

    private void listener() {
        mRefreshLayout.setOnRefreshListener(this);
        mAdressBookView.setLoadMoreListener(this);
        mAdapter.setItemClickListener(this);
        mBackView.setOnClickListener(this);
        mFunctionImgView.setOnClickListener(this);
        mFunctionTextView.setOnClickListener(this);
        rootView.setRetryButtonOnclickListener(this);
    }

    private void initData() {
        mTitleView.setText("通讯录");
        mFunctionTextView.setText(R.string.add_adress);
        mFunctionTextView.setTextColor(ContextCompat.getColor(mContext,R.color.color_0068BD));
        mFunctionImgView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.selector_adress_person_bg));

        startAdressRequest(String.valueOf(mOffset));
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void startAdressRequest(final String offset){
//        rootView.showLoadingView();
        rootView.hiddenNetErrorView();
        AdressBookRequest bookRequest=new AdressBookRequest();
        bookRequest.offset=String.valueOf(Integer.parseInt(offset)*10);
        mBookRequest=bookRequest.startRequest(AdressBookResponse.class, new HttpCallback<AdressBookResponse>() {
            @Override
            public void onSuccess(RequestBase request, AdressBookResponse ret) {
//                rootView.hiddenLoadingView();
                mBookRequest=null;
                mRefreshLayout.setRefreshing(false);
                if (ret!=null&&ret.data!=null) {
                    mPeopleIndex += ret.data.students.elements.size();
                    if (mPeopleIndex<ret.data.students.totalElements){
                        mAdressBookView.setLoadMoreEnable(true);
                    }else {
                        mAdressBookView.setLoadMoreEnable(false);
                    }
                    if ("0".equals(offset)){
                        List<AdressBookPeople> list=new ArrayList<>();
                        AdressBookPeople bookPeople1=new AdressBookPeople(1,"班主任");
                        list.add(bookPeople1);
                        for (AdressBookPeople people:ret.data.masters){
                            people.isTeacher=true;
                        }
                        list.addAll(ret.data.masters);
                        AdressBookPeople bookPeople2=new AdressBookPeople(2,"学员");
                        list.add(bookPeople2);
                        list.addAll(ret.data.students.elements);
                        mAdapter.setData(list);
                    }else {
                        mAdressBookView.finishLoadMore();
                        mAdapter.addData(ret.data.students.elements);
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
//                rootView.hiddenLoadingView();
                mBookRequest=null;
                mRefreshLayout.setRefreshing(false);
                if (mAdapter.getItemCount()==0){
                    rootView.showNetErrorView();
                }
                ToastUtil.showToast(mContext,error.getMessage());
            }
        });
    }

    @Override
    public void onRefresh() {
        mOffset=0;
        startAdressRequest(String.valueOf(mOffset));
    }

    @Override
    public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
        mOffset++;
        startAdressRequest(String.valueOf(mOffset));
    }

    @Override
    public void onLoadmoreComplte() {

    }

    @Override
    public void onItemClick(int position, AdressBookPeople people) {
        if (people.userId== UserInfo.getInstance().getInfo().getUserId()){
            UserMessageActivity.LuanchActivity(mContext);
        }else {
            PersonalDetailsActivity.LuanchActivity(mContext, String.valueOf(people.userId),people.isTeacher);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_layout_right_img:
            case R.id.title_layout_signIn:
                EventUpdate.onAddStudent(mContext);
                AddStudentActivity.LuanchActivity(mContext);
                break;
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.retry_button:
                mRefreshLayout.setRefreshing(true);
                onRefresh();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null) {
            onRefresh();
        }
    }
}
