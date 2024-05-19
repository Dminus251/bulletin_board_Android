package com.example.bulletin_board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class bulletin_board : AppCompatActivity() {

    private lateinit var commentAdapter: Comment_Adapter
    private val commentList = mutableListOf<Comment>()
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bulletin_board)

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
        val title_ofPost = findViewById<EditText>(R.id.title_ofPost) //포스팅 제목
        val content_ofPost = findViewById<EditText>(R.id.content_ofPost) //포스팅 내용
        val post_uploadBtn = findViewById<Button>(R.id.post_uploadBtn) //포스팅 버튼

        val content_ofComment = findViewById<EditText>(R.id.content_ofComment) //댓글 내용
        val comment_uploadBtn = findViewById<Button>(R.id.comment_uploadBtn) //댓글 버튼
        val recyclerViewComments: RecyclerView = findViewById(R.id.recyclerViewComments)

        commentAdapter = Comment_Adapter(commentList)
        recyclerViewComments.adapter = commentAdapter
        recyclerViewComments.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.202.98.49/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        post_uploadBtn.setOnClickListener {
            val postTitle = title_ofPost.text.toString()
            val postContent = content_ofPost.text.toString()

            //텍스트 제거
            title_ofPost.text.clear()
            content_ofPost.text.clear()

            //서버로 보내기
            sendPost(postTitle, postContent)



        }

    }

    private fun sendPost(postTitle: String, postContent: String){
        val posting = Posting(postTitle, postContent) //Posting Data Class
        val call = apiService.sendPost(posting)

        call.enqueue(object: retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    Toast.makeText(this@bulletin_board, "Post sent successfully!", Toast.LENGTH_SHORT).show()
                    Log.d("create1", "${response.body()?.string()}")
                    //Log.d("create2", "$response")
                } else {
                    Toast.makeText(this@bulletin_board, "Failed to send post.", Toast.LENGTH_SHORT).show()
                    Log.d("create3", "$response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@bulletin_board, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

}