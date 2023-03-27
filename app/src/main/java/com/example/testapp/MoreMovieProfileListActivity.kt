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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_movie_list)

        webView = findViewById(R.id.wv_profile_info)
        recyclerView = findViewById(R.id.rv_more_movie_profile)

        movieList = mutableListOf()

        // WebView 설정
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(MyJavascriptInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // HTML 파싱
                view!!.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);")
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
            val title = doc.select("tit")

            // 영화 정보 추출
            //val moviePoster = doc.select(".image-box").eachAttr("data-src")

            val movieName = doc.select(".name").eachText()
            val movieGenreAndYear = doc.select(".metadata").eachText()
            val toInfoUrl = doc.select(".movie-list-item-wrap > a").eachAttr("href")

            // 영화 정보 리스트에 추가
            for (i in 0 until movieName.size) {
                movieList.add(
                    MovieProfiles(
                        //moviePoster[i].toInt(),
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
