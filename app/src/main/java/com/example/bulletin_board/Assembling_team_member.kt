package com.example.bulletin_board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Assembling_team_member : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assembling_team_member)

        val whwkd1 = findViewById<Button>(R.id.whwkd1)

        whwkd1.setOnClickListener {
            var intent = Intent(this, Yourpage::class.java)
            startActivity(intent)
        }

        val whwkd2 = findViewById<Button>(R.id.whwkd2)
        whwkd2.setOnClickListener {
            var intent = Intent(this, Yourpage::class.java)
            startActivity(intent)
        }

        val tlscjd1 = findViewById<Button>(R.id.tlscjd1)
        val tlscjd2 = findViewById<Button>(R.id.tlscjd2)
        tlscjd1.setOnClickListener {
            var intent = Intent(this, Apply_member::class.java)
            startActivity(intent)
        }
        tlscjd2.setOnClickListener {
            var intent = Intent(this, Apply_member::class.java)
            startActivity(intent)
        }
    }
}