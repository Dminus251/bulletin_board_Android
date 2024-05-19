package com.example.bulletin_board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GETpost : AppCompatActivity() {
    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getpost)

        //------------------------------------------부터 네비게이션 요소
        val navBtn = findViewById<ImageView>(R.id.drawer)
        val nav_close_btn = findViewById<Button>(R.id.close_nav_btn)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navLayout =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigation_view)

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
        sendPost()
    }

    private fun sendPost() {
        val call = apiService.getPost()

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    response.body()?.let { responseBody ->
                        val json = responseBody.string()
                        Log.d("create1", json)

                        // JSON 파싱
                        val gson = Gson()
                        val postListType = object : TypeToken<List<Posting>>() {}.type
                        val posts: List<Posting> = gson.fromJson(json, postListType)
                        Log.d("create", "posts = $posts")
                        // UI 업데이트
                        if (posts.size == 1) {
                            val title1: TextView = findViewById(R.id.title1)
                            val content1: TextView = findViewById(R.id.content1)
                            title1.text = posts[0].title
                            content1.text = posts[0].content
                        }
                        else if (posts.size == 2) {
                            val title2: TextView = findViewById(R.id.title2)
                            val content2: TextView = findViewById(R.id.content2)
                            title2.text = posts[1].title
                            content2.text = posts[1].content
                        }
                        else if (posts.size == 3) {
                            val title3: TextView = findViewById(R.id.title3)
                            val content3: TextView = findViewById(R.id.content3)
                            title3.text = posts[2].title
                            content3.text = posts[2].content
                        }


                        }
                        Log.d("create", "${response.body()?.string()}")
                        Log.d("create", "$response")
                    }
                else {
                    Toast.makeText(this@GETpost, "Failed to send post.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("create", "$response")
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@GETpost, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
}