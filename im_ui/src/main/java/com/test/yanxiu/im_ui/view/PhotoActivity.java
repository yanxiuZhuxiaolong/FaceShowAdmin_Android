package com.test.yanxiu.im_ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.faceshow_ui_base.ImBaseActivity;
import com.test.yanxiu.im_ui.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Canghaixiao.
 * Time : 2017/6/23 14:11.
 * Function :
 */
public class PhotoActivity extends ImBaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, PhotoPagerAdapter.onClickPhotoListener {

    private static final String PATH_LIST = "paths";
    private static final String SELECTID = "select";
    private static final String FROM = "from";

    private static final String ISCANDELETE = "delete";
    public static final int DELETE_CAN = 0;
    public static final int DELETE_CANNOT = 1;

    private Context mContext;
    private ViewPager mImagePhotoView;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private ImageView mDeleteView;
    private PhotoPagerAdapter mAdapter;
    private PhotoDeleteDialog mDeleteDialog;

    private int mFromId;
    private int mSelectPosition = -1;
    private int mTotalNum;
    private int mIsCanDelete = DELETE_CAN;

    public static void LaunchActivity(Context context, ArrayList<String> list, int selectPosition, int fromId, int isCanDelete) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putStringArrayListExtra(PATH_LIST, list);
        intent.putExtra(SELECTID, selectPosition);
        intent.putExtra(FROM, fromId);
        intent.putExtra(ISCANDELETE, isCanDelete);

        context.startActivity(intent);
    }

    public static void LaunchActivity(Activity context, int reqCode, ArrayList<String> list, int selectPosition, int fromId, int isCanDelete) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putStringArrayListExtra(PATH_LIST, list);
        intent.putExtra(SELECTID, selectPosition);
        intent.putExtra(FROM, fromId);
        intent.putExtra(ISCANDELETE, isCanDelete);

        context.startActivityForResult(intent, reqCode);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.im_ui_activity_photo);
        mContext = PhotoActivity.this;
        mSelectPosition = getIntent().getIntExtra(SELECTID, -1);
        mFromId = getIntent().getIntExtra(FROM, -1);
        mIsCanDelete = getIntent().getIntExtra(ISCANDELETE, DELETE_CAN);
        initView();
        initData(getIntent().getStringArrayListExtra(PATH_LIST));
        listener();
    }

    private void initView() {
        mTopView = findViewById(R.id.include_top);
        mBackView = (ImageView) findViewById(R.id.im_ui_iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mDeleteView = (ImageView) findViewById(R.id.im_ui_iv_right);

        mImagePhotoView = (ViewPager) findViewById(R.id.vp_image_review);
        mAdapter = new PhotoPagerAdapter(mContext);
        mImagePhotoView.setAdapter(mAdapter);
    }

    private void listener() {
        mBackView.setOnClickListener(PhotoActivity.this);
        mDeleteView.setOnClickListener(PhotoActivity.this);
        mImagePhotoView.addOnPageChangeListener(PhotoActivity.this);
        mAdapter.setOnClickPhotoListener(PhotoActivity.this);
    }

    private void initData(ArrayList<String> list) {
        mBackView.setVisibility(View.VISIBLE);
        if (mIsCanDelete == DELETE_CAN) {
            mDeleteView.setVisibility(View.VISIBLE);
        } else {
            mDeleteView.setVisibility(View.GONE);
        }
        mTopView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
        if (list == null) {
            return;
        }
        mTotalNum = list.size();
//        List<ZoomImageView> zoomImageViews=new ArrayList<>();
        List<View> previewImageView = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.photo_preview_item, null);
            ZoomImageView imageView = view.findViewById(R.id.photo_preview_item_zoom_imageview);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTopView.setVisibility(View.VISIBLE);
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mTopView.setVisibility(View.GONE);
                    return true;
                }
            });
            previewImageView.add(view);
        }

//        for (int i=0;i<list.size();i++){
//            ZoomImageView imageView=new ZoomImageView(mContext);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mTopView.setVisibility(View.VISIBLE);
//                }
//            });
//            imageView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mTopView.setVisibility(View.GONE);
//                    return true;
//                }
//            });
//            zoomImageViews.add(imageView);
//        }
//        mAdapter.setData(list,zoomImageViews);
        mAdapter.setData(list, previewImageView);
        mImagePhotoView.setCurrentItem(mSelectPosition);
        mTitleView.setText((mSelectPosition + 1) + "/" + mTotalNum);
        mBackView.setBackgroundResource(R.drawable.selector_back);
        mDeleteView.setBackgroundResource(R.drawable.selector_large_delete);
    }

    private void showDialog() {
        if (mDeleteDialog == null) {
            mDeleteDialog = new PhotoDeleteDialog(mContext);
            mDeleteDialog.setClickListener(new PhotoDeleteDialog.OnViewClickListener() {
                @Override
                public void onDeleteClick() {
                    PhotoDeleteBean deleteBean = new PhotoDeleteBean();
                    deleteBean.formId = mFromId;
                    deleteBean.deleteId = mSelectPosition;
                    EventBus.getDefault().post(deleteBean);
                    mAdapter.deleteItem(mSelectPosition);
                    mTotalNum = mAdapter.getCount();

                    if (mTotalNum == 0) {
                        PhotoActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        mTitleView.setText((mSelectPosition + 1) + "/" + mTotalNum);
                    }
                }
            });
        }
        mDeleteDialog.show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.mSelectPosition = position;
        mTitleView.setText((mSelectPosition + 1) + "/" + mTotalNum);
        for (View view : mAdapter.getImageViews()) {
            try {
                ZoomImageView zoomImageView = view.findViewById(R.id.photo_preview_item_zoom_imageview);
                zoomImageView.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mTopView.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.im_ui_iv_left) {
            onBackPressed();
        } else if (i == R.id.im_ui_iv_right) {
            showDialog();

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", mSelectPosition);
        PhotoActivity.this.setResult(RESULT_OK, intent);
        PhotoActivity.this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view, int position) {
        onBackPressed();
    }

}
