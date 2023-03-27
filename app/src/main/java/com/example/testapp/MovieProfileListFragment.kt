package com.example.testapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jsoup.Jsoup


class MovieProfileListFragment : Fragment() { //childFragment임...

    private lateinit var webView: WebView
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieList: MutableList<MovieProfiles>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        webView = view.findViewById(R.id.wv_profile_info)
        recyclerView = view.findViewById(R.id.rv_movie_profile)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val searchString = arguments?.getString("searchString")
        val url = "https://m.kinolights.com/search/movies?keyword=$searchString"

        val moreButton = view.findViewById<ImageButton>(R.id.ib_more)


        moreButton.setOnClickListener {
            // 이동할 액티비티를 지정합니다.
            val intent = Intent(activity, MoreMovieProfileListActivity::class.java)
            intent.putExtra("searchString", searchString)
            // 액티비티를 시작합니다.
            startActivity(intent)

        }

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
            for (i in 0 until 3) {
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
            val adapter = MovieProfileAdapter(ArrayList(movieList))
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = adapter
        }
    }
}

