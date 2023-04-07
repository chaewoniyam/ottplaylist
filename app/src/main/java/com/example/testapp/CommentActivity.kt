package com.example.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class CommentActivity : AppCompatActivity() {
    var contentUid : String? =null
    //    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        contentUid = intent.getStringExtra("contentUid")

        val comment_recyclerView = findViewById<RecyclerView>(R.id.comment_recyclerView)

        comment_recyclerView.adapter = CommentRecyclerviewAdapter()
        comment_recyclerView.layoutManager = LinearLayoutManager(this)


        findViewById<Button>(R.id.comment_button).setOnClickListener{
            var comment = ContentDTO.Comment()
            comment.userId = FirebaseAuth.getInstance().currentUser?.email
            comment.uid = FirebaseAuth.getInstance().currentUser?.uid
            comment.comment = findViewById<TextView>(R.id.comment_editView).text.toString()
            comment.date = Date()

            FirebaseFirestore.getInstance().collection("post").document(contentUid!!).collection("comments").document().set(comment)

            findViewById<TextView>(R.id.comment_editView).setText("")
        }
    }

    inner class CommentRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var comments: ArrayList<ContentDTO.Comment> = arrayListOf()

        init {
            FirebaseFirestore.getInstance()
                .collection("post")
                .document(contentUid!!)
                .collection("comments")
                .orderBy("date")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    comments.clear()
                    for (snapshot in querySnapshot.documents) {
                        comments.add(snapshot.toObject(ContentDTO.Comment::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, position: Int):RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false)

            return CustomViewHolder(view)

        }

        private  inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val commentTextView: TextView = view.findViewById(R.id.commentview_comment_textView)
            val profileTextView: TextView = view.findViewById(R.id.commentview_profile_textView)

        }

        override fun getItemCount(): Int {
            return comments.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = holder.itemView
            val customViewHolder = holder as CustomViewHolder

            customViewHolder.commentTextView.text = comments[position].comment
            customViewHolder.profileTextView.text = comments[position].userId



        }


    }
}