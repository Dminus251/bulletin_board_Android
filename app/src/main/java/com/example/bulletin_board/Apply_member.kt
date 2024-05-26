package com.example.bulletin_board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class Apply_member : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_member)
        val post_uploadBtn = findViewById<Button>(R.id.post_uploadBtn)
        post_uploadBtn.setOnClickListener {
            Toast.makeText(this, "신청을 완료했습니다.", Toast.LENGTH_SHORT).show()
        }
    }


}