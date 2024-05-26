package com.example.bulletin_board

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.ImageAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity(), ImageAdapter.OnItemClickListener {
    private lateinit var clickAnimation: Animation
    private lateinit var apiService: ApiService
    private lateinit var drawerLayout: DrawerLayout
    private val selectedButtons = mutableMapOf<Int, Boolean>()
    lateinit var images: List<Int>
    lateinit var titles: List<String>
    private var random_number:Int = 30

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    lateinit var shuffledImages: List<Int>
    lateinit var shuffledTitles: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clickAnimation = AnimationUtils.loadAnimation(this, R.anim.click_scale)



        val navBtn = findViewById<ImageView>(R.id.drawer)
        val nav_close_btn = findViewById<Button>(R.id.close_nav_btn)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navLayout =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigation_view)

        navBtn.setOnClickListener {
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
            val intent = Intent(this, FAQ::class.java)
            startActivity(intent)
        }
        val sisang = findViewById<Button>(R.id.sisang)
        sisang.setOnClickListener {
            val intent = Intent(this, bulletin_board::class.java)
            startActivity(intent)
        }
        val loginBtn = findViewById<TextView>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val intent = Intent(this, Login2::class.java)
            startActivity(intent)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.202.98.49/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val search_editText = findViewById<EditText>(R.id.search_editText)
        val total_searching_result = findViewById<TextView>(R.id.total_searching_result) //총 검색 결과
        val searching_result_count = findViewById<TextView>(R.id.searching_result_count) //숫자
        search_editText.setOnTouchListener { _, event -> //돋보기 클릭 리스너
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (search_editText.right - search_editText.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val search_word = search_editText.text.toString()
                    total_searching_result.visibility = View.VISIBLE
                    searching_result_count.visibility = View.VISIBLE

                    sendPostToServer(search_word)
                    search_editText.text.clear()
                    random_number = Random.nextInt(6, random_number)
                    searching_result_count.text = "${random_number.toString()}건"
                    shuffle_kongko(titles, images)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        val mypage = findViewById<ImageView>(R.id.mypage)
        val alarm = findViewById<ImageView>(R.id.alarm)
        val scrap = findViewById<ImageView>(R.id.scrap)


        mypage.setOnClickListener {
            it.startAnimation(clickAnimation)
            val intent = Intent(this, Mypage::class.java)
            startActivity(intent)
        }
        alarm.setOnClickListener {

        }
        scrap.setOnClickListener {

        }

        val gonggoButton: Button = findViewById(R.id.gonggo)
        gonggoButton.setOnClickListener { showPopup(it, searching_result_count) }

        for (i in 1..15) {
            selectedButtons[i] = false
        }

        images = listOf(
            R.drawable.kongko_sample1,
            R.drawable.kongko_sample2,
            R.drawable.sample_kongko3,
            R.drawable.sample_kongko4,
            R.drawable.sample_kongko5,
            R.drawable.sample_kongko6,
            R.drawable.sample_kongko7,
            R.drawable.sample_kongko8
        )
        shuffledImages = images
        titles = listOf(
            "2024 다양한 가족의 재발견 영상 공모전",
            "2024년 트래블 이노베이션 아이디어 공모전",
            "Young Marketers Championship",
            "2024년 대전 공공데이터 활용 창업 경진 대회",
            "VHS 비디오 공모전",
            "2024 통계 데이터 활용 대회",
            "제복인의 선행 미담 영상 콘텐츠 공모전",
            "제 2회 정읍 웹툰 공모전"
        )
        val order_by_popularity = findViewById<Button>(R.id.order_by_popularity)
        val order_by_registration = findViewById<Button>(R.id.order_by_registration)
        order_by_popularity.setOnClickListener {
            shuffle_kongko(titles, images)
        }
        order_by_registration.setOnClickListener {
            shuffle_kongko(titles, images)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        imageAdapter = ImageAdapter(this, images, titles, this)
        recyclerView.adapter = imageAdapter
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

    private fun showPopup(anchorView: View, searching_result_count:TextView) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_layout, null)




        val popupWindow = PopupWindow(popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true)

        val buttons = listOf(
            popupView.findViewById<Button>(R.id.button1),
            popupView.findViewById(R.id.button2),
            popupView.findViewById(R.id.button3),
            popupView.findViewById(R.id.button4),
            popupView.findViewById(R.id.button5),
            popupView.findViewById(R.id.button6),
            popupView.findViewById(R.id.button7),
            popupView.findViewById(R.id.button8),
            popupView.findViewById(R.id.button9),
            popupView.findViewById(R.id.button10),
            popupView.findViewById(R.id.button11),
            popupView.findViewById(R.id.button12),
            popupView.findViewById(R.id.button13),
            popupView.findViewById(R.id.button14),
            popupView.findViewById(R.id.button15)
        )

        val defaultBackground: Drawable? = ContextCompat.getDrawable(this, R.drawable.popup_ripple)
        val selectedBackground: Drawable? = ContextCompat.getDrawable(this, R.drawable.rounded_button_selected)

        buttons.forEach { button ->
            button.setOnClickListener {
                if (button.background.constantState == defaultBackground?.constantState) {
                    button.background = selectedBackground
                } else {
                    button.background = defaultBackground
                }
            }
        }

        val yOffset = -200
        popupWindow.showAsDropDown(anchorView, 0, yOffset)

        val selectButton: Button = popupView.findViewById(R.id.selecting)
        selectButton.setOnClickListener {
            shuffle_kongko(titles, images)
            random_number = Random.nextInt(6, 21)//6부터 20까지 랜덤
            searching_result_count.text = "${random_number.toString()}건"
            popupWindow.dismiss() //팝업 닫기
        }
    }

    override fun onItemClick(view: View, position: Int) { //공고 클릭 리스너
        view.startAnimation(clickAnimation)
        var intent = Intent(this@MainActivity, kongko_outline::class.java)
        intent.putExtra("position", position)
        val imagesArrayList = ArrayList(shuffledImages)
        intent.putIntegerArrayListExtra("images", imagesArrayList)

        startActivity(intent)

    }

    private fun shuffle_kongko(titles:  List<String>, images: List<Int>){
        // 두 리스트를 Pair로 결합
        val pairedList = images.zip(titles)

        // 리스트를 랜덤으로 섞음
        val shuffledList = pairedList.shuffled(kotlin.random.Random)

        // 섞인 리스트를 분리
        val (shuffledImagesList, shuffledTitlesList) = shuffledList.unzip()

        // 섞인 값을 멤버 변수에 할당
        shuffledImages = shuffledImagesList
        shuffledTitles = shuffledTitlesList

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        imageAdapter = ImageAdapter(this@MainActivity, shuffledImages, shuffledTitles, this@MainActivity)
        recyclerView.adapter = imageAdapter
    }
}
