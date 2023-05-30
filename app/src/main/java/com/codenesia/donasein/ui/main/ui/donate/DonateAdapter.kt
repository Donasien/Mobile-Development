package com.codenesia.donasein.ui.main.ui.donate

import android.annotation.SuppressLint
import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codenesia.donasein.data.remote.response.DataItem
import com.codenesia.donasein.databinding.ItemDonateBinding
import java.util.Locale

class DonateAdapter(private val onItemClick: (DataItem) -> Unit): ListAdapter<DataItem, DonateAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {

    class ListViewHolder(private val binding: ItemDonateBinding, val onItemClick: (DataItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(donate: DataItem){
            val localeID : Locale = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)

            binding.itemDonateTitle.text = donate.title
            binding.itemDonateDays.text = donate.daysLeft.toString().plus(" days left")
            val target = formatRupiah.format(donate.targetAmount?.toDouble())
            val latest = formatRupiah.format(donate.latestAmount?.toDouble())
            binding.itemDonateCost.text = target
            binding.itemDonateRaised.text = latest
            binding.animatedProgressBar.setMax(donate.targetAmount!!)
            binding.animatedProgressBar.setProgress(donate.latestAmount!!)

            Glide.with(itemView.context)
                .load(donate.coverPhoto)
                .into(binding.itemHealthImage)
            itemView.setOnClickListener {
                onItemClick(donate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemDonateBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ListViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val donate = getItem(position)
        holder.bind(donate)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DataItem> =
            object : DiffUtil.ItemCallback<DataItem>() {
                override fun areItemsTheSame(oldUser: DataItem, newUser: DataItem): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: DataItem, newUser: DataItem): Boolean {
                    return oldUser == newUser
                }
            }
    }
}