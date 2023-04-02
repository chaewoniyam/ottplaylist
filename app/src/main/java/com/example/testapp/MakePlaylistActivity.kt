package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MakePlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_playlist)

        val addMovieButton = findViewById<Button>(R.id.add_movie_button)
        addMovieButton.setOnClickListener{
            val intent = Intent(this, addPlaylistMovieSearchAndLike::class.java)
            startActivity(intent)
        }
    }
}