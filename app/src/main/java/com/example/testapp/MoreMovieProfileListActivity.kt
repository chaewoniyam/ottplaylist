package com.example.testapp

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jsoup.Jsoup

class MoreMovieProfileListActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieList: MutableList<MovieProfiles>
    private var isHtmlReceived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_movie_list)

        webView = findViewById(R.id.wv_more_list)
        recyclerView = findViewById(R.id.rv_more_list)

        movieList = mutableListOf()

        // WebView 설정
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(MyJavascriptInterface(), "Android")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (!isHtmlReceived) {
                    view!!.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);")
                }
            }
        }

        val searchString = intent.getStringExtra("searchString")
        val url = "https://m.kinolights.com/search/movies?keyword=$searchString"

        // WebView에서 URL 로딩
        webView.loadUrl(url)
    }

    inner class MyJavascriptInterface {
        @JavascriptInterface
        fun getHtml(html: String) {
            // HTML 파싱
            val doc = Jsoup.parse(html)

            // 영화 정보 추출

            val movieName = doc.select(".name").eachText()
            val movieGenreAndYear = doc.select(".metadata").eachText()
            val toInfoUrl = doc.select(".movie-list-item-wrap > a").eachAttr("href")
            val posterImageUrl = doc.select("div.image-box img").map {
                if (it.hasAttr("data-src") && !it.attr("data-src").isBlank()) {
                    it.attr("data-src")
                } else {
                    it.attr("src")
                }
            }

            // 영화 정보 리스트에 추가
            for (i in 0 until movieName.size) {
                movieList.add(
                    MovieProfiles(
                        posterImageUrl[i],
                        movieName[i],
                        movieGenreAndYear[i],
                        toInfoUrl[i]
                    )
                )
            }


            // RecyclerView 설정
            val adapter = MoreMovieProfileListActivityAdapter(ArrayList(movieList))
            recyclerView.layoutManager = LinearLayoutManager(this@MoreMovieProfileListActivity)
            recyclerView.adapter = adapter
        }
    }
}