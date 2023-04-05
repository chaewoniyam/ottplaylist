package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class PostingActivity :AppCompatActivity() {

    lateinit var imageIv:ImageView
    lateinit var title: EditText
    lateinit var story: EditText
    lateinit var tag1: EditText
    lateinit var tag2: EditText
    lateinit var tag3: EditText
    lateinit var uploadBtn:Button

    val IMAGE_PICK=1111
    var fbAuth : FirebaseAuth? = null
    var selectImage:Uri?=null
    private lateinit var auth: FirebaseAuth


    lateinit var storage:FirebaseStorage
    lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_playlist)
        auth = Firebase.auth

        storage= FirebaseStorage.getInstance()
        firestore=FirebaseFirestore.getInstance()

        var postInfo = PostInfo()

        imageIv=findViewById(R.id.profileImg)
        fbAuth = FirebaseAuth.getInstance() // 인증 객체 초기화
        postInfo.userId = fbAuth?.uid // 사용자 UID 설정
        title=findViewById(R.id.title)
        story=findViewById(R.id.story)
        tag1=findViewById(R.id.hashtag1)
        tag2=findViewById(R.id.hashtag2)
        tag3=findViewById(R.id.hashtag3)
        uploadBtn=findViewById(R.id.add_movie_button)

        imageIv.setOnClickListener {
            var intent= Intent(Intent.ACTION_PICK) //선택하면 무언가를 띄움. 묵시적 호출
            intent.type="image/*"
            startActivityForResult(intent,IMAGE_PICK)
        }
        uploadBtn.setOnClickListener {
            if(selectImage!=null) {
                var fileName =
                    SimpleDateFormat("yyyyMMddHHmmss").format(Date()) // 파일명이 겹치면 안되기 떄문에 시년월일분초 지정
                storage.getReference().child("image").child(fileName)
                    .putFile(selectImage!!)//어디에 업로드할지 지정
                    .addOnSuccessListener { taskSnapshot -> // 업로드 정보를 담는다
                        taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { it->
                            var imageUrl=it.toString()
                            var photo=PostInfo(fbAuth?.uid.toString(),title.text.toString(),story.text.toString(),tag1.text.toString(),tag2.text.toString(),tag3.text.toString(),imageUrl)
                            firestore.collection("post")
                                .document().set(photo)
                                .addOnSuccessListener {
                                    finish()
                                }
                        }
                    }
            }
            val intent = Intent(this, addPlaylistMovieSearchAndLike::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_PICK&&resultCode==Activity.RESULT_OK){
            selectImage=data?.data
            imageIv.setImageURI(selectImage)
        }
    }
}