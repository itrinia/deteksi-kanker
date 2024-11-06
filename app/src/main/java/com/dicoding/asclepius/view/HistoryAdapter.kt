package com.dicoding.asclepius.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.AnalysisResultEntity

class HistoryAdapter (private var listHistory: List<AnalysisResultEntity>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvDate: TextView = itemView.findViewById(R.id.tv_item_date)
        val tvResult: TextView = itemView.findViewById(R.id.tv_item_result)
    }

    fun updateData(newList: List<AnalysisResultEntity>) {
        listHistory = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listHistory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, photo, date, result) = listHistory[position]
        holder.tvDate.text = date.toString()
        holder.tvResult.text = result
        Glide.with(holder.itemView.context)
            .load(Uri.parse(photo))
            .into(holder.imgPhoto)
    }
}