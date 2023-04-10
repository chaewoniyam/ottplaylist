package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.testapp.InfoActivity.Companion.TAG
import com.google.firebase.firestore.FirebaseFirestore
import org.jsoup.Jsoup

class MovieProfileListFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieList: MutableList<MovieProfiles>
    private var isHtmlReceived = false
    private var searchString: String? = null

    // 새로운 RecyclerView를 생성할 변수
    private lateinit var playlistTitleRecyclerView: RecyclerView
    private lateinit var playlistTitleList: MutableList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        webView = view.findViewById<WebView>(R.id.wv_movie_list)
        recyclerView = view.findViewById<RecyclerView>(R.id.rv_movie_list)

        playlistTitleRecyclerView = view.findViewById<RecyclerView>(R.id.rv_playlist_title)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieList = mutableListOf()
        playlistTitleList = mutableListOf()

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

        searchString = arguments?.getString("searchString")
        val url = "https://m.kinolights.com/search/movies?keyword=$searchString"

        // WebView에서 URL 로딩
        webView.loadUrl(url)

        view.findViewById<ImageButton>(R.id.ib_more).setOnClickListener {
            val intent = Intent(activity, MoreMovieProfileListActivity::class.java)
            intent.putExtra("searchString", searchString)
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
            for (i in 0 until 3) {
                movieList.add(
                    MovieProfiles(
                        posterImageUrl[i],
                        movieName[i],
                        movieGenreAndYear[i],
                        toInfoUrl[i],
                    )
                )
            }

            val db = FirebaseFirestore.getInstance()
            val postsRef = db.collection("post")

            if (searchString != null) {
                postsRef.whereEqualTo("isPublished", true)
                    .whereArrayContains("title", searchString!!)
                    .get()
                    .addOnSuccessListener { documents ->
                        val filteredMovieList = mutableListOf<ContentDTO>()
                        for (document in documents) {
                            val contentDTO = ContentDTO(
                                userId = document.getString("userId"),
                                title = document.getString("title") ?: "",
                                imageUrl = document.getString("imageUrl"),
                                postId = document.getString("postId"),
                            )
                            filteredMovieList.add(contentDTO)
                        }

                        // RecyclerView 설정
                        val adapter = MovieProfileAdapter(ArrayList(movieList))
                        recyclerView.layoutManager = LinearLayoutManager(activity)
                        recyclerView.adapter = adapter

                        // 새로운 RecyclerView에 대한 설정
                        val playlistTitleAdapter =
                            SearchPlaylistTitleAdapter(ArrayList(filteredMovieList))
                        playlistTitleRecyclerView.layoutManager = LinearLayoutManager(activity)
                        playlistTitleRecyclerView.adapter = playlistTitleAdapter
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            }
        }
    }
}