package com.example.ppaps.ui.main.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ppaps.R

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){

    private var historyList = listOf<HistoryItem>()

    fun submitList(list: List<HistoryItem>) {
        historyList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]

        holder.ambulanceIcon.setImageResource(currentItem.ambulanceIcon)
        holder.hospitalNameTextView.text = currentItem.hospitalName
        holder.bookingDateTextView.text = currentItem.bookingDate
        holder.bookingStatusTextView.text = currentItem.bookingStatus
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ambulanceIcon: ImageView = itemView.findViewById(R.id.ambulanceIcon)
        val hospitalNameTextView: TextView= itemView.findViewById(R.id.hospitalNameTextView)
        val bookingDateTextView: TextView = itemView.findViewById(R.id.bookingDateTextView)
        val bookingStatusTextView: TextView = itemView.findViewById(R.id.bookingStatusTextView)
    }
}