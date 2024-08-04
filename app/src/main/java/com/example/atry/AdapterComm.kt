package com.example.atry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterComm(
    private val context: Context,
    private var commentry: List<DataComm>
) : RecyclerView.Adapter<AdapterComm.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_comm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentry[position]
        holder.comm.text = comment.text
        holder.head.text = comment.head
    }

    override fun getItemCount(): Int = commentry.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val comm: TextView = itemView.findViewById(R.id.comm)
        val head: TextView = itemView.findViewById(R.id.head)
    }
}
