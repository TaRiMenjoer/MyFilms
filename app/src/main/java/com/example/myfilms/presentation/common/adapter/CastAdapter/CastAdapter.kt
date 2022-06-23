package com.example.myfilms.presentation.common.adapter.CastAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myfilms.data.model.Cast
import com.example.myfilms.databinding.ItemCastBinding

class CastAdapter : ListAdapter<Cast, CastViewHolder>(CastDiffCallback) {

//    var onFilmClickListener: OnFilmClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            ItemCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = getItem(position)

        with(holder.binding) {
//            Picasso.get().load(IMG_URL + movie.posterPath).into(ivMovie)
//            movieItemID.setOnClickListener {
//
//                onFilmClickListener?.onFilmClick(movie)
//            }
            tvName.text = cast.name
        }
    }
//
//    interface OnFilmClickListener {
//
//        fun onFilmClick(movie: Movie)
//    }
}