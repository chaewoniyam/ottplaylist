package com.example.testapp

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MovieInfoActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info)

        webView = findViewById<WebView>(R.id.wv_profile_info)
        webView.settings.javaScriptEnabled = true

        val url = intent.getStringExtra("url")!!
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // HTML 파싱
                view!!.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);")
            }
        }

        // 해당 url의 페이지를 WebView에서 열기
        webView.loadUrl(url)
    }
}