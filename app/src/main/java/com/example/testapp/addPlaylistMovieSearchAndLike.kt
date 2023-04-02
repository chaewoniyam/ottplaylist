package com.example.testapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class addPlaylistMovieSearchAndLike: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_like)

        val SearchButton = findViewById<Button>(R.id.seach_click)
        val searchEditText = findViewById<EditText>(R.id.input)
        SearchButton.setOnClickListener {
            val searchString = searchEditText.text.toString()
            println("Search string is $searchString") // 변수에 사용자가 입력한 값 들어 왔는지 확인

            val intent = Intent(this, addMovieProfileListActivity::class.java)
            intent.putExtra("searchString", searchString)
            startActivity(intent)

    }
    }

}