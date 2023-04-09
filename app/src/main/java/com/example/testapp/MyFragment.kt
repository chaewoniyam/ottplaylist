package com.example.testapp


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyFragment : Fragment() {
    var fragmentView: View? = null
    lateinit var firestore: FirebaseFirestore
    var uid: String? = null
    var auth: FirebaseAuth? = null
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        val go_to_make_playlist = view.findViewById<ImageButton>(R.id.go_to_make_playlist)
        go_to_make_playlist.setOnClickListener {
            val intent = Intent(requireContext(), PostingActivity::class.java)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_accont_movie_list)
        imageAdapter = ImageAdapter()
        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        uid = FirebaseAuth.getInstance().currentUser?.uid
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("post")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { documents ->
                val contents = documents.toObjects(ContentDTO::class.java)
                imageAdapter.setData(contents)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents: ", e)
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result!!.documents.isNotEmpty()) {
                    for (document in task.result!!.documents) {
                        val content = document.toObject(ContentDTO::class.java)
                        content?.let { it.postId = document.id }
                    }
                    imageAdapter.setData(task.result!!.toObjects(ContentDTO::class.java))
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }


        return view
    }

    class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
        private val contents = mutableListOf<ContentDTO>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val content = contents[position]
            Glide.with(holder.itemView.context)
                .load(content.imageUrl)
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, PostDetailActivity::class.java)
                intent.putExtra("postId", content.postId)
                holder.itemView.context.startActivity(intent)
            }
        }


        override fun getItemCount(): Int {
            return contents.size
        }

        fun setData(newContents: List<ContentDTO>) {
            contents.clear()
            contents.addAll(newContents)
            notifyDataSetChanged()
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
        }
    }
}
