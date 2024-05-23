package com.example.myapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bulletin_board.R

class ImageAdapter(
    private val context: Context,
    private val images: List<Int>,
    private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    //한 item_image에 imge1, img2밖에 없으니까?
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img1: ImageButton = itemView.findViewById(R.id.img1)
        val img2: ImageButton = itemView.findViewById(R.id.img2)

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
        val firstImageIndex = position * 2
        holder.img1.setImageResource(images[firstImageIndex])
        Log.d("ImageAdapter", "Setting img1 with resource id: ${images[firstImageIndex]}")

        val secondImageIndex = firstImageIndex + 1
        if (secondImageIndex < images.size) {
            holder.img2.setImageResource(images[secondImageIndex])
            holder.img2.visibility = View.VISIBLE
            Log.d("ImageAdapter", "Setting img2 with resource id: ${images[secondImageIndex]}")
        } else {
            holder.img2.visibility = View.GONE
            Log.d("ImageAdapter", "Hiding img2 as there is no image")
        }
        // 클릭 리스너 바인딩
        holder.bindClickListener(position, itemClickListener)

    }

    override fun getItemCount() = (images.size + 1) / 2
}
