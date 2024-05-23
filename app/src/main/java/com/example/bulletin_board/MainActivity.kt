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
import androidx.recyclerview.widget.GridLayoutManager
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

//ImageAdapter의 OnItemClickListener도 상속받음 -> interface 구현해야 함 (onItemClick 메서드)
class MainActivity : AppCompatActivity(), ImageAdapter.OnItemClickListener {
    private lateinit var clickAnimation: Animation //여기서 초기화하면 에러 발생함
    private lateinit var apiService: ApiService
    private lateinit var drawerLayout: DrawerLayout
    // 선택 상태를 저장할 MutableMap
    private val selectedButtons = mutableMapOf<Int, Boolean>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clickAnimation = AnimationUtils.loadAnimation(this, R.anim.click_scale)
//        val testBtn = findViewById<Button>(R.id.postTestBtn)
//        testBtn.setOnClickListener {
//            var intent = Intent(this, bulletin_board::class.java)
//            startActivity(intent)
//        }
//
//        val getTestBtn = findViewById<Button>(R.id.getTestBtn)
//        getTestBtn.setOnClickListener {
//            var intent = Intent(this, GETpost::class.java)
//            startActivity(intent)
//        }


        //------------------------------------------부터 사이드 네비게이션 요소
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

        val FAQbtn = findViewById<Button>(R.id.FAQ)
        FAQbtn.setOnClickListener {
            var intent = Intent(this, FAQ::class.java)
            startActivity(intent)

        }
        val sisang = findViewById<Button>(R.id.sisang)
        sisang.setOnClickListener {
            var intent = Intent(this, bulletin_board::class.java)
            startActivity(intent)
        }
        val loginBtn = findViewById<TextView>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            var intent = Intent(this, Login2::class.java)
            startActivity(intent)
        }
        //------------------------------------------까지 사이드네비게이션 요소


        //------------------------------------------부터 검색 Retrofit
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
        //------------------------------------------까지 검색 Retrofit

        //------------------------------------------부터 icon click listener
        val mypage = findViewById<ImageView>(R.id.mypage)
        val alarm = findViewById<ImageView>(R.id.alarm)
        val scrap = findViewById<ImageView>(R.id.scrap)


        mypage.setOnClickListener {
            it.startAnimation(clickAnimation)
            var intent = Intent(this, Mypage::class.java)

            startActivity(intent)
        }
        alarm.setOnClickListener {

        }
        scrap.setOnClickListener {

        }
        //------------------------------------------까지 icon click listener


        val gonggoButton: Button = findViewById(R.id.gonggo) // 공고 분야 버튼 예시
        gonggoButton.setOnClickListener { showPopup(it) }

        // 초기 선택 상태를 false로 설정
        for (i in 1..15) {
            selectedButtons[i] = false
        }

        //--------------------------------공고 리사이클러 뷰
        val images = listOf(
            R.drawable.kongko_sample1,
            R.drawable.kongko_sample2,
            R.drawable.sample_kongko3,
            R.drawable.sample_kongko4,
            R.drawable.sample_kongko5,
            R.drawable.sample_kongko6,
            R.drawable.sample_kongko7,
            R.drawable.sample_kongko8



        )

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ImageAdapter(this, images, this)


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
    private fun showPopup(anchorView: View) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_layout, null)

        val popupWindow = PopupWindow(popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true)

        // 버튼 클릭 리스너 설정
        val buttons = listOf<Button>(
            popupView.findViewById(R.id.button1),
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


        // 팝업을 AnchorView 아래에 오프셋을 두고 표시
        val yOffset = -200 // 원래 위치보다 50픽셀 위로 이동
        popupWindow?.showAsDropDown(anchorView, 0, yOffset)

        // 선택 완료 버튼 리스너
        val selectButton: Button = popupView.findViewById(R.id.select)
        selectButton.setOnClickListener {
            showSelectedButtons()
            popupWindow?.dismiss()
        }


    }
    private fun showSelectedButtons() {
        val selectedButtonIds = selectedButtons.filter { it.value }.keys
        Toast.makeText(this, "Selected Buttons: $selectedButtonIds", Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        view.startAnimation(clickAnimation)
        // 여기서 추가 동작을 수행할 수 있습니다.
    }


}
