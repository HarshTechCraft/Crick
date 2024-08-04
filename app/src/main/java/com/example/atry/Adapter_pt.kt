package com.example.atry

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter_pt(
    private val context: Context,
    private val flattenedData: List<FlattenedData>
) : RecyclerView.Adapter<Adapter_pt.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_pt, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = flattenedData[position]
        if (item.isGroupName) {
            if(item.groupName == "Teams")
            {
                holder.groupName.visibility = View.GONE
            }
            else
            {
                holder.groupName.visibility = View.VISIBLE
            }
            holder.groupName.text = item.groupName
            holder.name.visibility = View.GONE
            holder.match.visibility = View.GONE
            holder.win.visibility = View.GONE
            holder.lose.visibility = View.GONE
            holder.nr.visibility = View.GONE
            holder.points.visibility = View.GONE
            holder.nrr.visibility = View.GONE
            holder.line.visibility = View.VISIBLE
            holder.line2.visibility = View.VISIBLE
        } else {
            val pointsTableInfo = item.pointsTableInfo
            holder.groupName.visibility = View.GONE
            holder.header.visibility = View.GONE
            holder.line.visibility = View.GONE
            holder.line2.visibility = View.GONE
            holder.name.visibility = View.VISIBLE
            holder.match.visibility = View.VISIBLE
            holder.win.visibility = View.VISIBLE
            holder.lose.visibility = View.VISIBLE
            holder.nr.visibility = View.VISIBLE
            holder.points.visibility = View.VISIBLE
            holder.nrr.visibility = View.VISIBLE
            holder.name.text = pointsTableInfo?.teamName
            holder.match.text = pointsTableInfo?.matchesPlayed?.toString()
            holder.win.text = pointsTableInfo?.matchesWon?.toString()
            holder.lose.text = pointsTableInfo?.matchesLost?.toString()
            holder.nr.text = pointsTableInfo?.noResult?.toString()
            holder.points.text = pointsTableInfo?.points?.toString()
            holder.nrr.text = pointsTableInfo?.nrr
            val backgroundColorAttr = if (position % 2 == 0) R.attr.row_even else R.attr.row_odd
            val typedValue = TypedValue()
            context.theme.resolveAttribute(backgroundColorAttr, typedValue, true)
            holder.itemView.setBackgroundColor(typedValue.data)
        }
    }

    override fun getItemCount(): Int {
        return flattenedData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName: TextView = itemView.findViewById(R.id.group_name)
        val header: TableRow = itemView.findViewById(R.id.header)
        val line: View = itemView.findViewById(R.id.view5)
        val line2: View = itemView.findViewById(R.id.view)
        val lose: TextView = itemView.findViewById(R.id.lose)
        val match: TextView = itemView.findViewById(R.id.match)
        val name: TextView = itemView.findViewById(R.id.name)
        val nr: TextView = itemView.findViewById(R.id.nr)
        val nrr: TextView = itemView.findViewById(R.id.nrr)
        val points: TextView = itemView.findViewById(R.id.pts)
        val win: TextView = itemView.findViewById(R.id.win)
    }
}