package com.example.atry

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class Adapter_squad(private var context: Context, private var playerList: List<DataSquad>) : RecyclerView.Adapter<Adapter_squad.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_squad, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentPos = position
        for (item in playerList) {
            if (item.isTeamName) {
                if (currentPos == 0) {
                    holder.name.visibility = View.GONE
                    holder.role.visibility = View.GONE
                    holder.teamName.visibility = View.VISIBLE
                    holder.line1.visibility = View.VISIBLE
                    holder.line2.visibility = View.VISIBLE
                    holder.teamName.text = item.teamName
                    holder.teamImg.visibility = View.VISIBLE
                    val teamImgUrl = item.team1Img ?: item.team2Img
                    Glide.with(context).load(teamImgUrl).into(holder.teamImg)
                    return
                }
                currentPos--
            } else {
                val items = item.teamInfo ?: continue
                if (currentPos < items.size) {
                    val player = items[currentPos]
                    holder.teamImg.visibility = View.GONE
                    holder.teamName.visibility = View.GONE
                    holder.line1.visibility = View.GONE
                    holder.line2.visibility = View.GONE
                    holder.name.visibility = View.VISIBLE
                    holder.role.visibility = View.VISIBLE
                    holder.name.text = player.name
                    holder.role.text = player.role
                    val backgroundColorAttr = if (position % 2 == 0) R.attr.row_even else R.attr.row_odd
                    val typedValue = TypedValue()
                    context.theme.resolveAttribute(backgroundColorAttr, typedValue, true)
                    holder.itemView.setBackgroundColor(typedValue.data)
                    return
                }
                currentPos -= items.size
            }
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        for (item in playerList) {
            if (item.isTeamName) {
                count++
            } else {
                count += item.teamInfo?.size ?: 0
            }
        }
        return count
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teamName: TextView = itemView.findViewById(R.id.teamName)
        val name: TextView = itemView.findViewById(R.id.name)
        val role: TextView = itemView.findViewById(R.id.role)
        val line1: View = itemView.findViewById(R.id.line1)
        val line2: View = itemView.findViewById(R.id.line2)
        val teamImg: CircleImageView = itemView.findViewById(R.id.teamImg)
    }
}