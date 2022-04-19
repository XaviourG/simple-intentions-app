package com.xaviourg.simpleintentions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xaviourg.simpleintentions.databinding.IntentionListingBinding
import com.xaviourg.simpleintentions.intentiondb.Scope

class BlockAdapter(scope: Scope) : RecyclerView.Adapter<BlockAdapter.BlockViewHolder>() {
    private var intentions = mutableListOf<String>()

    class BlockViewHolder(val binding: IntentionListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val binding = IntentionListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        //populate intention listings given the DB contents
        holder.binding.textView.text = intentions[position]
    }

    override fun getItemCount(): Int {
        return intentions.size
    }

    fun setListingsCount(count: Int){
        var newList = mutableListOf<String>()
        for (i in 1..count) {
            newList.add("intention eh...")
        }
        intentions = newList
        notifyDataSetChanged()
    }
}