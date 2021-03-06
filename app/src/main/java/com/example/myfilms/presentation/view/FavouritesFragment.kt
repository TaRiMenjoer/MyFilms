package com.example.myfilms.presentation.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myfilms.R
import com.example.myfilms.data.model.Movie
import com.example.myfilms.databinding.FragmentFavouritesBinding
import com.example.myfilms.presentation.Utils.LoadingState
import com.example.myfilms.presentation.view.adapter.MoviesAdapter
import com.example.myfilms.presentation.viewModel.ViewModelFavourites
import com.example.myfilms.presentation.viewModel.ViewModelProviderFactory


class FavouritesFragment : Fragment() {


    private var _binding: FragmentFavouritesBinding? = null
    private val binding: FragmentFavouritesBinding
        get() = _binding ?: throw RuntimeException("FavoritesFragment is null")


    private val adapter = MoviesAdapter()
    private lateinit var viewModel: ViewModelFavourites


    private lateinit var prefSettings: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProviderFactory = ViewModelProviderFactory(requireActivity())

        getSessionId()

        onMovieClickListener()

        viewModel = ViewModelProvider(this , viewModelProviderFactory)[ViewModelFavourites::class.java]

        viewModel.downloadData(MoviesFragment.PAGE , MoviesFragment.sessionId)

        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                LoadingState.IS_LOADING -> binding.progressBar.visibility = View.VISIBLE
                LoadingState.FINISHED -> binding.progressBar.visibility = View.GONE
                LoadingState.SUCCESS -> viewModel.movies.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                    binding.rvFavouritesMovies.adapter = adapter
                }
                else -> throw RuntimeException("Error")
            }
        }
    }
    private fun getSessionId() {
        try {
            MoviesFragment.sessionId = prefSettings.getString(LoginFragment.SESSION_ID_KEY, null) as String
        } catch (e: Exception) {
        }
    }

    private fun onMovieClickListener() {

        adapter.onFilmClickListener = object : MoviesAdapter.OnFilmClickListener {
            override fun onFilmClick(movie: Movie) {
                launchDetailFragment(movie.id)
            }
        }
    }

    private fun launchDetailFragment(movieId: Int) {
        val args = Bundle().apply {
            putInt(DetailsFragment.KEY_MOVIE, movieId)
        }
        findNavController().navigate(R.id.action_favouritesFragment_to_detailsFragment, args)
    }

}