package com.p2pdinner.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.p2pdinner.R;
import com.p2pdinner.common.Constants;

public class LegalViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_view);
        String uri = getIntent().getExtras().getString(Constants.LEGAL_LOAD_URI);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(uri);
    }
}
