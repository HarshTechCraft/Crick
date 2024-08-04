package com.example.atry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterScoreCard2(
     val context: Context,
     val bowlingList: List<BowlingStats>
) : RecyclerView.Adapter<AdapterScoreCard2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_scorecard2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bowlingStats = bowlingList[position]

        holder.bowler.text = bowlingStats.name ?: "N/A"
        holder.over.text = bowlingStats.overs.toString()
        holder.maiden.text = bowlingStats.maidens.toString()
        holder.runs.text = bowlingStats.runs.toString()
        holder.wickets.text = bowlingStats.wickets.toString()
        holder.nb.text = bowlingStats.noBalls.toString()
        holder.wd.text = bowlingStats.wides.toString()
        holder.eco.text = bowlingStats.economy.toString()
    }

    override fun getItemCount(): Int = bowlingList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bowler: TextView = itemView.findViewById(R.id.bowler)
        val over: TextView = itemView.findViewById(R.id.over)
        val maiden: TextView = itemView.findViewById(R.id.maiden)
        val runs: TextView = itemView.findViewById(R.id.runsb)
        val wickets: TextView = itemView.findViewById(R.id.wicket)
        val nb: TextView = itemView.findViewById(R.id.nb)
        val wd: TextView = itemView.findViewById(R.id.wd)
        val eco: TextView = itemView.findViewById(R.id.eco)
    }
}
