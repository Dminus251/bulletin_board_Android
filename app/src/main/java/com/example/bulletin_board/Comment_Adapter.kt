package com.example.bulletin_board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Comment_Adapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<Comment_Adapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val authorTextView: TextView = view.findViewById(R.id.textViewCommentAuthor)
        val contentTextView: TextView = view.findViewById(R.id.textViewCommentContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.authorTextView.text = comment.author
        holder.contentTextView.text = comment.content
    }

    override fun getItemCount() = commentList.size
}

