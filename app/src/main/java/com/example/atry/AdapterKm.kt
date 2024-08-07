package com.example.atry

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterKm(private val context: Context, private val commList: MutableList<DataKm>) : RecyclerView.Adapter<AdapterKm.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_km, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comm = commList[position]

        holder.commText.text = comm.commtxt
        holder.over.text = "${comm.overs} - "

        holder.img.visibility = View.VISIBLE

        when (comm.event) {
            "WICKET" -> holder.img.setImageResource(R.drawable.w)
            "FOUR" -> holder.img.setImageResource(R.drawable.four)
            "SIX" -> holder.img.setImageResource(R.drawable.six)
            "DROPPED" -> holder.img.setImageResource(R.drawable.drop)
            "FIFTY" -> holder.img.setImageResource(R.drawable.fifty)
            else -> holder.img.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return commList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commText: TextView = itemView.findViewById(R.id.commText)
        val img: ImageView = itemView.findViewById(R.id.img)
        val over: TextView = itemView.findViewById(R.id.overs)
    }
}