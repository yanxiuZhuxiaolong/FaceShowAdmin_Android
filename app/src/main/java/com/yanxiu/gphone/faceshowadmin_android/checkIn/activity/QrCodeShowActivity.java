package com.yanxiu.gphone.faceshowadmin_android.checkIn.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * show qrCode class
 *
 * @author fengrongcheng
 */
public class QrCodeShowActivity extends FaceShowBaseActivity {
    public final int MESSAGE_WHAT = 1;
    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.img_code)
    ImageView imgQrCode;
    private String qrCodeUrl = "http://orz.yanxiu.com/pxt/platform/data.api?method=interact.signInQrcode&stepId=";
    private int qrCodeRefreshInterval = 0;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_show);
        ButterKnife.bind(this);
        initTitle();
        getData();
        createQrCode();
        isNeedRefreshQrCode();
    }

    private void isNeedRefreshQrCode() {
        if (qrCodeRefreshInterval > 0) {
            handler = new MyHandler(this);
            handler.sendEmptyMessageDelayed(MESSAGE_WHAT, qrCodeRefreshInterval * 1000);
        }
    }


    private void getData() {
        String stepId = getIntent().getStringExtra("stepId");
        qrCodeRefreshInterval = getIntent().getIntExtra("qrCodeRefreshRate", 0);
        qrCodeUrl = qrCodeUrl + stepId;
    }

    static class MyHandler extends Handler {
        WeakReference<QrCodeShowActivity> activityWeakReference;
        int MESSAGE_WHAT = 1;

        MyHandler(QrCodeShowActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_WHAT) {
                QrCodeShowActivity activity = activityWeakReference.get();
                activity.createQrCode();
                this.sendEmptyMessageDelayed(MESSAGE_WHAT, activity.qrCodeRefreshInterval * 1000);
            }
        }
    }


    private void createQrCode() {

        // TODO: 17-11-2  need placeHolder View
        Glide.with(this).load(qrCodeUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgQrCode);
    }

    private void initTitle() {
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutTitle.setText(R.string.check_in_code);


    }

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && handler.hasMessages(MESSAGE_WHAT)) {
            handler.removeMessages(MESSAGE_WHAT);
        }
    }
}
