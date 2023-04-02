package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.jsoup.Jsoup

class MovieInfoActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var recyclerView: RecyclerView
    private lateinit var ottList: MutableList<OttInfo>
    private var isHtmlReceived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info)

        webView = findViewById(R.id.wv_movie_list)
        recyclerView = findViewById(R.id.rv_ott_platform)
        ottList = mutableListOf()

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(MyJavascriptInterface(), "Android")

        val url = intent.getStringExtra("url")!!


        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (!isHtmlReceived) {
                    view!!.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);")
                }
            }
        }

        // 해당 url의 페이지를 WebView에서 열기
        webView.loadUrl(url)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // requestCode가 같은 경우에만 처리
        if (requestCode == 1) { // 위의 예시 코드와 같은 requestCode를 사용하면 됩니다.
            // 결과를 처리하는 코드 작성
            if (resultCode == Activity.RESULT_OK) {
                // 결과가 OK일 경우 처리
            } else {
                // 결과가 취소되거나 오류가 발생한 경우 처리
            }
        }
    }


    inner class MyJavascriptInterface {
        @JavascriptInterface
        fun getHtml(html: String) {
            val handler = Handler(Looper.getMainLooper())
            // HTML 파싱
            val doc = Jsoup.parse(html)

            val ottName = doc.select(".provider-info").eachText()

            // 스트리밍 VOD 링크 가져오기
            val streamingVodList = doc.select("a#streamingVodList").eachAttr("href")

            // OttInfo 객체에 이미지 URL 추가하기
           // val ottPlatformImgList = doc.select(".provider-info img").eachAttr("src")

            // 영화 정보 리스트에 추가
            for (i in 0 until ottName.size) {
                ottList.add(
                    OttInfo(
                        ottName[i],
                        streamingVodList[i]
                        //ottPlatformImgList[i] // 이미지 URL 추가
                    )
                )
            }
            handler.post {
                // HTML 파싱
                val doc = Jsoup.parse(html)
                val backImg = findViewById<ImageView>(R.id.iv_backgroud)
                val imgUrl = doc.select("div.backdrop img[src]").attr("src")

                Glide.with(this@MovieInfoActivity)
                    .load(imgUrl)
                    .into(backImg)

                val posterImg = this@MovieInfoActivity.findViewById<ImageView>(R.id.iv_info_poster)
                val posterUrl = doc.select("div.poster img[src]")?.firstOrNull()?.attr("data-src")
                    ?: doc.select("div.poster img[src]")?.firstOrNull()?.attr("src")

                Glide.with(this@MovieInfoActivity)
                    .load(posterUrl)
                    .into(posterImg)

            }

            val title = doc.select(".title-kr").first()?.text()
            val tv_name = findViewById<TextView>(R.id.tv_name)
            tv_name.text = title

            val movieAdditionalInformation = doc.select(".metadata").first()?.text()
            val tv_movie_info = findViewById<TextView>(R.id.tv_movie_info)
            tv_movie_info.text = movieAdditionalInformation

            val story = doc.select(".synopsis").first()?.text()
            val tv_story = findViewById<TextView>(R.id.tv_story)
            tv_story.text = story



            // RecyclerView 설정
            val adapter = OttInfoAdapter(ArrayList(ottList))
            recyclerView.layoutManager = LinearLayoutManager(this@MovieInfoActivity)
            recyclerView.adapter = adapter
        }
    }
}
