package com.example.myapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bulletin_board.R

class ImageAdapter(
    private val context: Context,
    private val images: List<Int>,
    private val titles: List<String>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img1: ImageButton = itemView.findViewById(R.id.img1)
        val img2: ImageButton = itemView.findViewById(R.id.img2)
        val txt1: TextView = itemView.findViewById(R.id.txt1)
        val txt2: TextView = itemView.findViewById(R.id.txt2)

        fun bindClickListener(position: Int, clickListener: OnItemClickListener) {
            img1.setOnClickListener {
                clickListener.onItemClick(it, position * 2)
            }
            img2.setOnClickListener {
                clickListener.onItemClick(it, position * 2 + 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // 이미지 및 텍스트 설정
        holder.img1.setImageResource(images[position * 2])
        holder.txt1.text = titles[position * 2]

        // 두 번째 이미지와 텍스트가 있을 경우 설정
        if ((position * 2 + 1) < images.size) {
            holder.img2.setImageResource(images[position * 2 + 1])
            holder.txt2.text = titles[position * 2 + 1]
        } else {
            // 두 번째 아이템이 없을 경우 보이지 않도록 설정
            holder.img2.visibility = View.GONE
            holder.txt2.visibility = View.GONE
        }

        // 클릭 리스너 바인딩
        holder.bindClickListener(position, itemClickListener)
    }

    override fun getItemCount(): Int {
        return (images.size + 1) / 2
    }
}
