package com.example.myfilms.presentation.common.adapter.CastAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myfilms.data.model.Cast

object CastDiffCallback : DiffUtil.ItemCallback<Cast>() {
    override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem == newItem
    }
}