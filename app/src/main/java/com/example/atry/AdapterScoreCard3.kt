package com.example.atry

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class AdapterScoreCard3(
    private val context: Context,
    private val battingList: List<BattingStats>
) : RecyclerView.Adapter<AdapterScoreCard3.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_scorecard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val battingStats = battingList[position]

        holder.bat.text = battingStats.name ?: "N/A"
        holder.run.text = battingStats.runs.toString()
        holder.fours.text = battingStats.fours.toString()
        holder.sixs.text = battingStats.sixes.toString()
        holder.sr.text = battingStats.strikeRate.toString()
        holder.balls.text = battingStats.balls.toString()
        holder.dismissaltext.text = battingStats.dismissalText ?: "N/A"

        val backgroundColorAttr = if (position % 2 == 0) R.attr.row_even else R.attr.row_odd
        val typedValue = TypedValue()
        context.theme.resolveAttribute(backgroundColorAttr, typedValue, true)
        holder.itemView.setBackgroundColor(typedValue.data)
    }

    override fun getItemCount(): Int = battingList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bat: TextView = itemView.findViewById(R.id.bat)
        val run: TextView = itemView.findViewById(R.id.runs)
        val fours: TextView = itemView.findViewById(R.id.fours)
        val sixs: TextView = itemView.findViewById(R.id.sixs)
        val sr: TextView = itemView.findViewById(R.id.sr)
        val balls: TextView = itemView.findViewById(R.id.balls)
        val dismissaltext: TextView = itemView.findViewById(R.id.dismissal)
    }
}