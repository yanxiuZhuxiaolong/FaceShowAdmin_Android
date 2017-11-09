/**
 * Copyright 2016 Bartosz Schiller
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanxiu.gphone.faceshowadmin_android.common.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.Constants;
import com.yanxiu.gphone.faceshowadmin_android.common.bean.PdfBean;
import com.yanxiu.gphone.faceshowadmin_android.resource.ResourceMangerActivity;
import com.yanxiu.gphone.faceshowadmin_android.utils.NetWorkUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.Formatter;
import java.util.Locale;

import okhttp3.Call;

/**
 * pdf
 */

public class PDFViewActivity extends Activity implements OnPageChangeListener, View.OnClickListener {

    private static final String TAG = PDFViewActivity.class.getSimpleName();
    private static final int NONE = 2;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 3;

    PDFView pdfView;
    TextView tv_pagecount;
    TextView tv_name;
    ImageView iv_back;
    TextView tv_publish_loading_progress;

    int pageNumber = 0;
    PdfBean pdfbean;
    String pdfFileName;
    static final int FADE_OUT = 1;
    View view_loading;
    ImageButton ib_publish_close;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    int timeMs;
    TextView tv_save;
    //    String from;
//    String iscollection = "0";//是否收藏
    private boolean showDialog = false;
    String downpath;
    private AlertDialog netErrorDialog;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADE_OUT:
                    tv_pagecount.setVisibility(View.INVISIBLE);
                    break;

            }
        }
    };


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeMs += 1000;
            handler.postDelayed(runnable, 1000);
            //要做的事情


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pdf);
        tv_save = (TextView) findViewById(R.id.tv_save);

        tv_save.setOnClickListener(this);
        tv_save.setVisibility(View.VISIBLE);
        view_loading = findViewById(R.id.view_loading);
        tv_publish_loading_progress = (TextView) findViewById(R.id.tv_publish_loading_progress);
        ib_publish_close = (ImageButton) findViewById(R.id.ib_publish_close);
        ib_publish_close.setOnClickListener(this);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        tv_pagecount = (TextView) findViewById(R.id.tv_pagecount);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        pdfbean = (PdfBean) bundle.getSerializable("pdfbean");
        timeMs = pdfbean.getRecord();
        tv_name.setText(pdfbean.getName());

        String name = pdfbean.getName();


//        from = getIntent().getStringExtra("from");
//        if ("0".equals(from)) {
        tv_save.setVisibility(View.INVISIBLE);
        tv_save.setClickable(false);

//        } else {//代表资源
//            iscollection = getIntent().getStringExtra("iscollection");
//            Drawable drawable = null;
//            if (iscollection.equals("0")) {
//                tv_save.setClickable(true);
//                drawable = ContextCompat.getDrawable(this, R.drawable.collection_false);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            } else {
//                tv_save.setClickable(false);
//                drawable = ContextCompat.getDrawable(this, R.drawable.collection_true);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            }
//            if (!from.equals("2")) {
//                tv_save.setCompoundDrawables(null, null, drawable, null);
//            }
//        }


        File file = null;
        file = new File(Constants.DIR_PDF + name);
        downpath = Environment.getExternalStorageDirectory() + Constants.DIR_PDF;

        if (file.exists()) {
            view_loading.setVisibility(View.GONE);
            displayFromFile(file);
        } else {
            view_loading.setVisibility(View.GONE);


            if (NetWorkUtils.isNetworkAvailable(this)) {
                if (NetWorkUtils.isWifi(this)) {
                    getfromNet();
                    return;
                } else {
                    showNoWifiDownloadDialog();
                    return;
                }
            } else {
                showNoWifiAndTrafficDownloadDialog();
            }

        }
        handler.sendEmptyMessageDelayed(FADE_OUT, 2000);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    private void showNoWifiAndTrafficDownloadDialog() {
        showDialog = true;
        netErrorDialog = new AlertDialog.Builder(this)
                .setTitle("网络异常")
                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDialog = false;
                        if (NetWorkUtils.isNetworkAvailable(PDFViewActivity.this)) {


                            if (NetWorkUtils.isWifi(PDFViewActivity.this)) {
                                getfromNet();
                            } else {
                                Log.e("fff", "onCreatewwwwwwwwwwwww: ");
                                showNoWifiDownloadDialog();
                            }
                        } else {
                            showNoWifiAndTrafficDownloadDialog();

                        }


                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDialog = false;
                        dialog.dismiss();
                        PDFViewActivity.this.finish();
                    }
                })
                .show();
    }

    private void showNoWifiDownloadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("检测到不是联网状态，是否用流量打开")

                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getfromNet();

                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        PDFViewActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if ("0".equals(from)) {
//            ClassDetailsAction action = ClassDetailsAction.getInstense();
//            Map<String, String> map = new HashMap<>();
//            map.put("name", CourseConstants.NAME_PDF);
//
//            map.put("pdftime", timeMs + "");
//
//            action.SendSeeTimeToHttp(map);
//            handler.removeCallbacks(runnable);
//
//        }
        timeMs = 0;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        if ("0".equals(from)) {
//            timeMs = 0;
//            handler.postDelayed(runnable, 1000);//每两秒执行一次runnable
//        }
    }


    //    @AfterViews
    void getfromNet() {
        view_loading.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(PDFViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PDFViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
            if (ContextCompat.checkSelfPermission(PDFViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showToast(PDFViewActivity.this, "请开启存储权限");
                return;
            }
        }
        download();


    }

    private void download() {
        Log.i("ppp", pdfbean.getUrl());
        OkHttpUtils.get().url(pdfbean.getUrl()).tag("tag").build().execute(new FileCallBack(downpath, pdfbean.getName()) {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("nnn", "onError:aaa " + e.getMessage());
                File file = new File(downpath + pdfbean.getName());
                if (file.exists()) {
                    file.delete();
                }
            }

            @Override
            public void onResponse(File response, int id) {
                displayFromFile(response);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                if (progress < 0 || total < 0) {
                    view_loading.setVisibility(View.GONE);
                    return;
                }
                int pnow = (int) Math.floor(progress * 100);
                if (progress < 1) {
                    view_loading.setVisibility(View.VISIBLE);
                    if (lastprogress != pnow) {
                        tv_publish_loading_progress.setText(pnow + "%");
                    }
                    lastprogress = pnow;

                } else {
                    view_loading.setVisibility(View.GONE);
                }

                super.inProgress(progress, total, id);
            }
        });
    }

    int lastprogress;


    private void displayFromFile(File file) {
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .load();
//        if ("0".equals(from)) {
//            handler.postDelayed(runnable, 1000);//每1秒执行一次runnable
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    download();
                }
                break;
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        if (pageNumber != page) {
            handler.removeMessages(FADE_OUT);
            tv_pagecount.setVisibility(View.VISIBLE);
            handler.sendEmptyMessageDelayed(FADE_OUT, 2000);
        }
        pageNumber = page;
        tv_pagecount.setText((page + 1) + "/" + (pageCount));//page是从0开始计算的
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:


                finish();
                break;
            case R.id.ib_publish_close:

                finish();
                break;
            case R.id.tv_save:
//                String aid = getIntent().getStringExtra("aid");
//                String type = getIntent().getStringExtra("type");
//                CacheUtils.save(this, aid, type, from, new CacheUtils.Changed() {
//                    @Override
//                    public void changed(String code) {
//                        if (from.equals("3")) {
//                            TCAgent.onEvent(PDFViewActivity.this, "工作坊资源", "收藏工作坊资源");
//                        } else {
//                            TCAgent.onEvent(PDFViewActivity.this, "资源", "收藏资源");
//                        }
//                        if ("0".equals(code)) {
////                            tv_save.setVisibility(View.INVISIBLE);
//                            Drawable drawable = ContextCompat.getDrawable(PDFViewActivity.this, R.drawable.collection_true);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            tv_save.setCompoundDrawables(null, null, drawable, null);
//                            tv_save.setClickable(false);
//                        }
//                    }
//                });


                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("tag");

    }

    public static void invoke(Activity activity, String resName, String resUrl) {
        PdfBean pdfbean = new PdfBean();
        pdfbean.setName(resName);
        pdfbean.setUrl(resUrl);
        pdfbean.setRecord(0);
        Intent intent;
        intent = new Intent(activity, PDFViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pdfbean", pdfbean);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


}
