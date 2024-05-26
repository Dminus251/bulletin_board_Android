package com.example.bulletin_board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class kongko_outline : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kongko_outline)

        // Fragment를 추가합니다.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, outline_fragment())
                .commit()
        }

        val kongko_image = findViewById<ImageView>(R.id.kongko_image)

        val position = intent.getIntExtra("position", 0)
        val imagesArrayList = intent.getIntegerArrayListExtra("images") //이미지 경로가 저장된 List
        if (imagesArrayList != null) {
            val images_list: List<Int> = imagesArrayList.toList() //ArrayList를 list로
            val image_location = images_list[position].toString()  //list를 String으로
            val resId = resources.getIdentifier(image_location, "drawable", packageName) //해당하는 리소스 id 추출
            kongko_image.setImageResource(resId)
        }



    }
}