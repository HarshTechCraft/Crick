package com.example.atry

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.atry.Match

class Adapter_wc2( private val context: Context, private val matchs: List<Data_match>): RecyclerView.Adapter<Adapter_wc2.ViewHolder>() {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_wc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matchs[position]
        holder.apply {

            team1.text = match.team1Short
            team2.text = match.team2Short
            val team1ImgName = match.team1.replace("Women", "").replace(" ", "")
            val team2ImgName = match.team2.replace("Women", "").replace(" ", "")
            val team1ImgId = context.resources.getIdentifier(team1ImgName, "string", context.packageName)
            val team2ImgId = context.resources.getIdentifier(team2ImgName, "string", context.packageName)
            val team1Img = if (team1ImgId != 0) context.getString(team1ImgId) else ""
            val team2Img = if (team2ImgId != 0) context.getString(team2ImgId) else ""
            Glide.with(context).load(team1Img).into(team1img)
            Glide.with(context).load(team2Img).into(team2img)
            format.text = match.matchType
            venue.text = match.venue
            if (match.state == "Complete") {
                date.text = match.status
                status.text = "Completed"
                cardStatus.setCardBackgroundColor(Color.parseColor("#5CB85C"))
            } else {
                date.text = match.date
                status.text = "Not Started"
                cardStatus.setCardBackgroundColor(Color.parseColor("#E03C31"))
            }
            itemView.setOnClickListener {
                    val intent = Intent(context, Match::class.java).apply {
                    putExtra("matchId", match.matchId)
                    putExtra("team1img", team1Img)
                    putExtra("team2img", team2Img)
                    putExtra("team1", match.team1)
                    putExtra("team2", match.team2)
                    putExtra("date", match.date)
                    putExtra(NotificationCompat.CATEGORY_STATUS, match.status)
                    putExtra("state", match.state)
                    putExtra("seriesId", match.seriesId)
                    putExtra("team1Short", match.team1Short)
                    putExtra("team2Short", match.team2Short)
                    putExtra("team1Score", match.team1Score)
                    putExtra("team2Score", match.team2Score)
                    putExtra("team1Over", match.team1Over)
                    putExtra("team2Over", match.team2Over)
                    putExtra("matchType", match.matchType)
                    putExtra("recent", true)
                }
                context.startActivity(intent)
            }
        }
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int = matchs.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team1img: ImageView = itemView.findViewById(R.id.team1img)
        val team2img: ImageView = itemView.findViewById(R.id.team2img)
        val team1: TextView = itemView.findViewById(R.id.team1)
        val team2: TextView = itemView.findViewById(R.id.team2)
        val date: TextView = itemView.findViewById(R.id.date)
        val format: TextView = itemView.findViewById(R.id.format)
        val venue: TextView = itemView.findViewById(R.id.venue)
        val status: TextView = itemView.findViewById(R.id.status)
        val cardStatus: CardView = itemView.findViewById(R.id.card_status)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
            animation.startOffset = position * 100L
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}
