package com.neo.ilapagerassignment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neo.ilapagerassignment.databinding.ItemSubItemsBinding
import com.neo.ilapagerassignment.model.SubItems

class SubItemsListAdapter : RecyclerView.Adapter<SubItemsViewHolder>() {

    private var listSubItems = arrayListOf<SubItems>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubItemsViewHolder {
        val binding =
            ItemSubItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubItemsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.listSubItems.size
    }

    override fun onBindViewHolder(holder: SubItemsViewHolder, position: Int) {
        holder.bind(listSubItems[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(listSubItems: List<SubItems>) {
        this.listSubItems = listSubItems as ArrayList<SubItems>
        notifyDataSetChanged()
    }
}

class SubItemsViewHolder(private val itemBinding: ItemSubItemsBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(item: SubItems) {
        itemBinding.itemIvAvatar.setImageResource(item.itemIcon)
        itemBinding.itemTxtName.text = item.itemName
    }
}