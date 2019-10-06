package com.assignment.mobiledata.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.mobiledata.R
import kotlinx.android.synthetic.main.list_item.view.imageView
import kotlinx.android.synthetic.main.list_item.view.textViewDataConsumption

class MobileDataAdapter : RecyclerView.Adapter<MobileDataAdapter.MobileDataViewHolder>() {

    private val items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MobileDataViewHolder {
        return MobileDataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MobileDataViewHolder, position: Int) {
        holder.bind(items[position])
    }

    /**
     * Update items and refresh UI.
     */
    fun setItems(newItems: List<String>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class MobileDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textView = itemView.textViewDataConsumption
        private val imageView = itemView.imageView

        fun bind(data: String) {
            textView.text = data
        }
    }

}
