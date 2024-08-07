package com.example.atry

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterUpcoming(
    private val context: Context,
    private val list: List<DataUpcoming>
) : RecyclerView.Adapter<AdapterUpcoming.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_wc, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "DiscouragedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.team1.text = item.team1Short
        holder.team2.text = item.team2Short

        val team1NameFormatted = item.team1Name.replace("Women", "").replace(" ", "")
        val team2NameFormatted = item.team2Name.replace("Women", "").replace(" ", "")

        val resources = context.resources
        val team1ImgId = resources.getIdentifier(team1NameFormatted, "string", context.packageName)
        val team2ImgId = resources.getIdentifier(team2NameFormatted, "string", context.packageName)

        val team1Img = if (team1ImgId != 0) resources.getString(team1ImgId) else ""
        val team2Img = if (team2ImgId != 0) resources.getString(team2ImgId) else ""

        Glide.with(context).load(team1Img).into(holder.team1img)
        Glide.with(context).load(team2Img).into(holder.team2img)

        holder.format.text = "${item.matchFormat}, ${item.seriesName}"
        holder.venue.text = "${item.ground}, ${item.city}"
        holder.date.text = item.date
        holder.status.text = "Not Started"
        holder.card_status.setCardBackgroundColor(Color.parseColor("#E03C31"))

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Match::class.java).apply {
                putExtra("matchId", item.matchId)
                putExtra("team1img", team1Img)
                putExtra("team2img", team2Img)
                putExtra("team1", item.team1Name)
                putExtra("team2", item.team2Name)
                putExtra("date", item.date)
                putExtra(NotificationCompat.CATEGORY_STATUS, false)
                putExtra("seriesId", item.seriesId)
                putExtra("team1Short", item.team1Short)
                putExtra("team2Short", item.team2Short)
                putExtra("matchType", item.seriesName)
                putExtra("started", false)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team1img: ImageView = itemView.findViewById(R.id.team1img)
        val team2img: ImageView = itemView.findViewById(R.id.team2img)
        val team1: TextView = itemView.findViewById(R.id.team1)
        val team2: TextView = itemView.findViewById(R.id.team2)
        val date: TextView = itemView.findViewById(R.id.date)
        val format: TextView = itemView.findViewById(R.id.format)
        val venue: TextView = itemView.findViewById(R.id.venue)
        val status: TextView = itemView.findViewById(R.id.status)
        val card_status: CardView = itemView.findViewById(R.id.card_status)
    }
}
