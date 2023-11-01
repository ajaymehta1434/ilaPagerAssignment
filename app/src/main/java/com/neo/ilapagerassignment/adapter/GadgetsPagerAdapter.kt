package com.neo.ilapagerassignment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neo.ilapagerassignment.databinding.ItemGadgetsBinding
import com.neo.ilapagerassignment.model.Gadgets

class GadgetsPagerAdapter : RecyclerView.Adapter<PagerViewHolder>() {

    private var listGadgets = arrayListOf<Gadgets>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            ItemGadgetsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.listGadgets.size
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(listGadgets[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(listGadgets: List<Gadgets>) {
        this.listGadgets = listGadgets as ArrayList<Gadgets>
        notifyDataSetChanged()
    }
}

class PagerViewHolder(private val itemBinding: ItemGadgetsBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(item: Gadgets) {
        itemBinding.itemIvGadget.setImageResource(item.gadgetIcon)
    }
}