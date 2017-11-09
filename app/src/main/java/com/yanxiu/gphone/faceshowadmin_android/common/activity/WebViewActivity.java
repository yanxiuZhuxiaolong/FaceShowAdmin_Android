package com.yanxiu.gphone.faceshowadmin_android.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;


/**
 * 网络浏览页面
 */
public class WebViewActivity extends FaceShowBaseActivity {

    private WebView mWebView;
    private LinearLayout mRootView;
    private ImageView mBackView;
    private String title;

    public static void loadThisAct(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_web_view, null);
        setContentView(mRootView);
        title = getIntent().getStringExtra("title");
        setBackClick();
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.loadUrl(getIntent().getStringExtra("url"));
        setWebSettings(mWebView.getSettings());
        setWebClient(mWebView);
    }

    private void setBackClick() {
        mBackView = (ImageView) findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mRootView.removeView(mWebView);
        mWebView.destroy();
        WebViewActivity.this.finish();
    }

    private void setTitle(String title) {
        ((TextView) findViewById(R.id.title_layout_title)).setText(title);
    }

    private void setWebClient(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (view.getTitle().equals("about:blank") || view.getTitle().equals("- no title specified") || TextUtils.isEmpty(view.getTitle()) || view.getTitle().equals("网页无法打开")) {
                    setTitle(title);
                } else {
                    setTitle(view.getTitle());
                }
            }
        });
    }

    private WebSettings setWebSettings(WebSettings webSettings) {
        //支持获取收视焦点，输入用户名、密码活其他
        mWebView.requestFocusFromTouch();

        webSettings.setJavaScriptEnabled(false);//是否支持js
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSettings.setUseWideViewPort(true);//将图片调整到合适webView的大小
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小

        webSettings.setSupportZoom(true);//支持缩放
        webSettings.setBuiltInZoomControls(true);//设置内置的缩放控件
        webSettings.setDisplayZoomControls(false);//隐藏原生的缩放控件

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//关闭webView中缓存
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        return webSettings;

    }

}
