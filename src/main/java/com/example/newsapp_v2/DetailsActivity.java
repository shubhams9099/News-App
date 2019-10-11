package com.example.newsapp_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class DetailsActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar loader;
    String url="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        loader=findViewById(R.id.loader);
        webView=findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl(url);

        webView.setWebChromeClient(
                new WebChromeClient(){
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        if(newProgress==100)
                            loader.setVisibility(View.GONE);
                        else
                            loader.setVisibility(View.VISIBLE);
                    }
                }
        );
    }
}
