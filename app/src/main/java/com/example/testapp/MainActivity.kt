package com.example.testapp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testapp.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: com.google.firebase.auth.FirebaseAuth // 인스턴스 선언

    companion object{
        const val TAG: String = "로그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        // 로그인 성공한 회원은 메인 액티비티로
        // 유저가 아닌 회원은 로그인 액티비티로
        val user = Firebase.auth.currentUser
        if(user == null) { //Firebase.auth.currentUser == null
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        else{
            user?.let {
                for (profile in it.providerData) {
                    val name = profile.displayName
                    //val email = profile.email
                    //val photoUrl = profile.photoUrl

                    if(name != null){
                        if(name.length == 0) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }
                        }
                    }

                }
            }


        replaceFragment(Home()) //앱을 실행하면 가장 먼저 갖게되는 것은 홈

// 클릭하면 이동해야 하니까 seleclistener 호출
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){

                R.id.home -> replaceFragment(Home())
                R.id.search -> replaceFragment(SearchFragment())
                R.id.ranking -> replaceFragment(Ranking())
                R.id.my -> replaceFragment(My())

                else -> {


                }
            }

            true

        }

    }

    private fun replaceFragment(fragment: Fragment){ // 프랴그먼트 교차 함구 처음에 이 함수 호출
        if(fragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
        }
    }

    fun viewInfo_button(v : View){ //info 버튼 눌렀을 경우
        Log.d(MainActivity.TAG, "info - onCreate() called")
        startActivity(Intent(this, InfoActivity :: class.java ))
    }

}