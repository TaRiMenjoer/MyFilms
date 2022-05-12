package com.example.myfilms.presentation.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myfilms.R
import com.example.myfilms.databinding.FragmentDetailsBinding
import com.example.myfilms.presentation.Utils.LoadingState
import com.example.myfilms.presentation.viewModel.ViewModelDetails
import com.example.myfilms.presentation.viewModel.ViewModelProviderFactory
import com.squareup.picasso.Picasso

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = _binding ?: throw RuntimeException("DetailsFragment is null")

    private lateinit var viewModel: ViewModelDetails

    private lateinit var prefSettings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefSettings = context?.getSharedPreferences(
            LoginFragment.APP_SETTINGS, Context.MODE_PRIVATE
        ) as SharedPreferences
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSessionId()
        initViewModel()
        setStar()
        getMovieById(movieId)
        setOnClickFavourites()
    }

    private fun setOnClickFavourites() {
        binding.ivAddFavorite.setOnClickListener {
            viewModel.addOrRemoveFavourites(movieId, sessionId)
        }
    }

    private fun setStar() {
        viewModel.movie.observe(viewLifecycleOwner) {

            if (it.isLiked) {
                binding.ivAddFavorite.setImageResource(R.drawable.ic_star_yellow)
            } else {
                binding.ivAddFavorite.setImageResource(R.drawable.ic_star_white)
            }
        }
    }

    private fun getSessionId() {
        try {
            sessionId = prefSettings.getString(LoginFragment.SESSION_ID_KEY, null) as String
        } catch (e: java.lang.Exception) {
        }
    }

    private fun initViewModel() {

        val viewModelProviderFactory = ViewModelProviderFactory(requireActivity())

        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[ViewModelDetails::class.java]

        viewModel = ViewModelProvider(this)[ViewModelDetails::class.java]
    }

    private fun getMovieById(movieId: Int) {

        viewModel.getMovieById(movieId, sessionId)
        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                LoadingState.IS_LOADING -> binding.progressBar.visibility = View.VISIBLE
                LoadingState.FINISHED -> binding.progressBar.visibility = View.GONE
                LoadingState.SUCCESS -> {
                    viewModel.movie.observe(viewLifecycleOwner) {
                        Picasso.get().load(IMG_URL + it.backdropPath)
                            .into(binding.ivPoster)
                        binding.tvTitle.text = it.title
                        binding.tvOverview.text = it.overview
                    }
                }
                else -> throw RuntimeException(getString(R.string.error))
            }
        }
    }

    private fun parseArgs() {

        requireArguments().getInt(KEY_MOVIE).apply {
            movieId = this
        }
    }

    companion object {

        private var movieId: Int = 0
        private var sessionId: String = ""
        private const val IMG_URL = "https://image.tmdb.org/t/p/w500"
        const val KEY_MOVIE = "Movie_id"
    }
}