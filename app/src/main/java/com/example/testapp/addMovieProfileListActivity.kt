package com.example.testapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.InfoActivity.Companion.TAG
import com.google.firebase.firestore.FirebaseFirestore
import org.jsoup.Jsoup

class addMovieProfileListActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieList: MutableList<MovieProfiles>
    private val checkedItems = mutableListOf<MovieProfiles>()
    private var isHtmlReceived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie_list)

        val movieProfileList = arrayListOf<MovieProfiles>()
        val adapter = addMovieProfileListActivityAdapter(movieProfileList)

        // RecyclerView 설정
        recyclerView = findViewById(R.id.rv_add_movie_list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        webView = findViewById(R.id.wv_add_movie_list)

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

        val searchButton = findViewById<Button>(R.id.seach_click)

        searchButton.setOnClickListener {
            val searchEditText = findViewById<EditText>(R.id.input)
            // 검색어 가져오기
            val searchString = searchEditText.text.toString()

            // 키보드 내리기
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            // RecyclerView 초기화
            movieList.clear()

            // WebView에서 URL 로딩
            val url = "https://m.kinolights.com/search/movies?keyword=$searchString"
            webView.loadUrl(url)
        }

        val addImageButton = findViewById<ImageButton>(R.id.confirmButton)

        addImageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
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
            // RecyclerView 설정
            val adapter = addMovieProfileListActivityAdapter(ArrayList(movieList))
            recyclerView.layoutManager = LinearLayoutManager(this@addMovieProfileListActivity)
            recyclerView.adapter = adapter

        }
    }
}
