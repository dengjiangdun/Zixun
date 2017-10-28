package com.johndon.cmcc.zixun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class NewsDetailActivity extends AppCompatActivity {
    private WebView mWebView;
    private static final String BASE_URL = "http://daily.zhihu.com/story/";
    private String storyUrl="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        layout.setTitle(getIntent().getStringExtra("newsTitle"));
        Log.i("+++++++++++++++", "onCreate: title == " + getIntent().getStringExtra("id"));
        storyUrl = BASE_URL +getIntent().getStringExtra("id");
        mWebView = (WebView) findViewById(R.id.news_details);
        mWebView.loadUrl(storyUrl);

        FloatingActionButton buttonShare = (FloatingActionButton)  findViewById(R.id.fab_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, storyUrl);
                intent.setType("text/plain");
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent,"share to"));
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }
}
