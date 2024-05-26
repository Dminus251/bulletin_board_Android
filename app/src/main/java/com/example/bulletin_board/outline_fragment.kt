package com.example.bulletin_board

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class outline_fragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트의 레이아웃을 inflate
        val view =  inflater.inflate(R.layout.fragment_example, container, false)

        val assembling = view.findViewById<Button>(R.id.assembling) //모집 중 X 명
        val random_number = Random.nextInt(1, 6)
        assembling.text = "모집 중 ${random_number}명"
        assembling.setOnClickListener {
            var intent = Intent(requireContext(), Assembling_team_member::class.java)
            startActivity(intent)
        }


        val assemble_team_member = view.findViewById<Button>(R.id.assemble_team_member) // 팀원 구하기
        assemble_team_member.setOnClickListener {
            var intent = Intent(requireContext(), bulletin_board::class.java) //requireContext()는 Fragment가 연결된 Context를 반환함
            startActivity(intent)
        }

        val share = view.findViewById<Button>(R.id.share) //공유

        share.setOnClickListener {
            val imageUri: Uri = Uri.parse("android.resource://" + "com.example.bulletin_board" + "/" + R.drawable.alarm)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/*"
            }
            val chooserIntent = Intent.createChooser(shareIntent, "Share Image using")
            startActivity(chooserIntent)


        }

        val scrap_button = view.findViewById<Button>(R.id.scrap_button)
        scrap_button.setOnClickListener {view -> //스크랩
            showSnackbar(view)
        }

        val apply_via_email = view.findViewById<Button>(R.id.apply_via_email) //이메일 지원
        apply_via_email.setOnClickListener {
            showEmailDialog()
        }

        return view
    }
    private fun showSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "공고를 스크랩했습니다.", Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.parseColor("#83dcb7"))
        snackbar.setAction("보러 가기") {
            val intent = Intent(requireContext(),Scrap::class.java)
            startActivity(intent)
        }
        snackbar.show()
    }

    private fun showEmailDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_dialog_email, null)

        val emailTextView: TextView = dialogView.findViewById(R.id.email_text)
        val emailAddressTextView: TextView = dialogView.findViewById(R.id.email_address)
        val closeButton: Button = dialogView.findViewById(R.id.close_button)

        // 이메일 텍스트에 ClickableSpan 추가
        val spannableString = SpannableString("이 공모전은 이메일로 지원 가능합니다.")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // 클립보드에 이메일 복사
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("email", "koshawebtoon@gmail.com")
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "이메일이 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        spannableString.setSpan(clickableSpan, 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        emailTextView.text = spannableString
        emailTextView.movementMethod = LinkMovementMethod.getInstance()

        // 다이얼로그 생성 및 표시
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
            .setView(dialogView)
            .create()

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}