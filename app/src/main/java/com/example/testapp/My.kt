package com.example.testapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView

class My : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        val goToMakePlaylist = view.findViewById<ImageButton>(R.id.go_to_make_playlist)
        goToMakePlaylist.setOnClickListener {
            val intent = Intent(activity, MakePlaylistActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}