package com.example.myfilms.presentation.movieDetails

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myfilms.R
import com.example.myfilms.databinding.FragmentDetailsBinding
import com.example.myfilms.domain.MovieRepository.MovieRepository
import com.example.myfilms.presentation.common.Utils.LoadingState
import com.example.myfilms.presentation.common.adapter.CastAdapter.CastAdapter
import com.example.myfilms.presentation.common.adapter.MoviesAdapter
import com.example.myfilms.presentation.login.LoginFragment
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = _binding ?: throw RuntimeException("DetailsFragment is null")
    

    private val viewModel by viewModel<ViewModelDetails>()

    private lateinit var prefSettings: SharedPreferences

    private val adapter = CastAdapter()

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
        setStar()
        observeActors()
        getMovieById(movieId)
        setOnClickFavourites()
        setBackListener()

    }
    private fun setBackListener(){
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
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


    private fun getMovieById(movieId: Int) {

        viewModel.getMovieById(movieId, sessionId)

        viewModel.getCreditResponse(movieId)

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
                        binding.tvRating.text = it.voteAverage.toString()
                    }
                }
                else -> throw RuntimeException(getString(R.string.error))
            }
        }
    }

    private fun observeActors(){
        viewModel.actors.observe(viewLifecycleOwner){
            adapter.submitList(it)
            binding.rvMainActors.adapter = adapter

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