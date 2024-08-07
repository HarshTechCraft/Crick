package com.example.atry

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream

class AdapterNews(
    private var context: Context,
    private var storyListData: List<DataNews>
) : RecyclerView.Adapter<AdapterNews.ViewHolder>() {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = storyListData[position]
        holder.hline.text = story.hline
        holder.story.text = story.intro
        Glide.with(context).load(story.image).into(holder.image)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, News2::class.java).apply {
                putExtra("newsId", story.newsId.toString())
                val byteArrayOutputStream = ByteArrayOutputStream()
                story.image?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                putExtra("imgByteArray", byteArrayOutputStream.toByteArray())
            }
            Log.d("ajeto", story.imageId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return storyListData.size
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom).apply {
                startOffset = (position * 100).toLong()
            }
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hline: TextView = itemView.findViewById(R.id.hline)
        val story: TextView = itemView.findViewById(R.id.story)
        val image: ImageView = itemView.findViewById(R.id.image)
    }
}
