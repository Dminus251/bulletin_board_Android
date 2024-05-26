package com.example.bulletin_board

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Login2 : AppCompatActivity() {

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_login2)


        context = this

        //naver_init() //네이버 로그인초기화 -> 네이버 안 씀

        val text_id = findViewById<TextView>(R.id.text_id)
        val editText_id = findViewById<EditText>(R.id.editText_id) //아이디
        val text_pw = findViewById<TextView>(R.id.text_pw)
        val editText_pw = findViewById<EditText>(R.id.editText_pw) //패스워드
        val sign_up_btn = findViewById<TextView>(R.id.sign_up)
        val kakao_login_btn = findViewById<Button>(R.id.kakaoLoginBtn)
        //------------------------------------------부터 사이드 네비게이션 요소
        val navBtn = findViewById<ImageView>(R.id.drawer)
        val nav_close_btn = findViewById<Button>(R.id.close_nav_btn)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navLayout =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigation_view)
        val loginBtn_in_activity = findViewById<Button>(R.id.loginBtn_in_activity)
        loginBtn_in_activity.setOnClickListener {
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            finish()
        }
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

        val FAQbtn = findViewById<Button>(R.id.FAQ)
        FAQbtn.setOnClickListener {
            var intent = Intent(this, FAQ::class.java)
            startActivity(intent)

        }

        //------------------------------------------까지 사이드네비게이션 요소

        //비밀번호 텍스트 마스킹
        editText_pw.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val maskedText = maskPassword(p0)
                editText_pw.removeTextChangedListener(this)
                editText_pw.setText(maskedText)
                editText_pw.setSelection(maskedText.length)
                editText_pw.addTextChangedListener(this)
            }

            override fun afterTextChanged(p0: Editable?) {  }

        })






        editText_id.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                text_id.setTextColor(Color.parseColor("#99ccff"))
                editText_id.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.black)
            } else {
                text_id.setTextColor(Color.parseColor("#000000"))
                editText_id.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.black)
            }

        }

        editText_pw.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                text_pw.setTextColor(Color.parseColor("#99ccff"))
                editText_pw.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.black)
            } else {
                text_pw.setTextColor(Color.parseColor("#000000"))
                editText_pw.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.black)
            }
        }



    }


    private fun maskPassword(s: CharSequence?): String { // CharSequence 객체 s를 매개변수로 받음
        // 최근에 입력된 문자만 표시하고 나머지는 특수문자로 마스킹하기
        val maskedText = StringBuilder()
        s?.let {
            for (i in 0 until it.length - 1) {
                maskedText.append("*")
            }
            if (it.isNotEmpty()) {
                maskedText.append(it[it.length - 1]) // 마지막 문자는 표시
            }
        }
        return maskedText.toString()
    }

}