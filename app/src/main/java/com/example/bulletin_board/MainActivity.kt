package com.example.bulletin_board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testBtn = findViewById<Button>(R.id.postTestBtn)
        testBtn.setOnClickListener {
            var intent = Intent(this, bulletin_board::class.java)
            startActivity(intent)
        }

        val getTestBtn = findViewById<Button>(R.id.getTestBtn)
        getTestBtn.setOnClickListener {
            var intent = Intent(this, GETpost::class.java)
            startActivity(intent)
        }


        //------------------------------------------부터 네비게이션 요소
        val navBtn = findViewById<ImageView>(R.id.drawer)
        val nav_close_btn = findViewById<Button>(R.id.close_nav_btn)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navLayout = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigation_view)

        navBtn.setOnClickListener { //드로어 레이아웃
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        nav_close_btn.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        //------------------------------------------까지 네비게이션 요소

        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.202.98.49/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val search_editText = findViewById<EditText>(R.id.search_editText)
        search_editText.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2 // 오른쪽 그림을 나타내는 상수
            // 사용자가 손을 뗐을 때 (ACTION_UP 이벤트)
            if (event.action == MotionEvent.ACTION_UP) {
                // 터치 이벤트가 EditText의 오른쪽 드로어블(그림) 내에 있는지 확인
                if (event.rawX >= (search_editText.right - search_editText.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val search_word = search_editText.text.toString()
                    sendPostToServer(search_word)
                    //Toast.makeText(this, "$search_word", Toast.LENGTH_SHORT).show()

                    search_editText.text.clear() //검색창 단어 삭제


                    return@setOnTouchListener true // 터치 이벤트를 소비했음을 나타냄
                }
            }
            return@setOnTouchListener false // 터치 이벤트를 소비하지 않았음을 나타냄
        }



    }
    private fun sendPostToServer(title: String) {
        val postRequest = PostRequest(title)
        val call = apiService.createPost(postRequest)

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    Toast.makeText(this@MainActivity, "Post sent successfully!", Toast.LENGTH_SHORT).show()
                    Log.d("create", "${response.body()?.string()}")
                } else {
                    Toast.makeText(this@MainActivity, "Failed to send post.", Toast.LENGTH_SHORT).show()
                    Log.d("create", "$response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }




}
