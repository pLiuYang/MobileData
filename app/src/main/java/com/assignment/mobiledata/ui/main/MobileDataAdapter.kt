package com.assignment.mobiledata.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.mobiledata.R
import com.assignment.mobiledata.data.RecordDisplayModel
import kotlinx.android.synthetic.main.list_item.view.imageView
import kotlinx.android.synthetic.main.list_item.view.textViewDataConsumption

class MobileDataAdapter(private val clickAction: () -> Unit) :
    RecyclerView.Adapter<MobileDataAdapter.MobileDataViewHolder>() {

    private val items = mutableListOf<RecordDisplayModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MobileDataViewHolder {
        return MobileDataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            ),
            clickAction
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MobileDataViewHolder, position: Int) {
        holder.bind(items[position])
    }

    /**
     * Update items and refresh UI.
     */
    fun setItems(newItems: List<RecordDisplayModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class MobileDataViewHolder(itemView: View, clickAction: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val textView = itemView.textViewDataConsumption
        private val imageView = itemView.imageView

        init {
            imageView.setOnClickListener { clickAction.invoke() }
        }

        fun bind(data: RecordDisplayModel) {
            textView.text = itemView.context.getString(
                R.string.mobile_data_consumption,
                data.dataVolume,
                data.year
            )
            imageView.visibility =
                if (data.downTrending) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

}
