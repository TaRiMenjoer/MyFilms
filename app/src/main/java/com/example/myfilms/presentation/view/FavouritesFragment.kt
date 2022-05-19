package com.example.myfilms.presentation.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
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
        get() = _binding ?: throw RuntimeException(getString(R.string.favourites_fragment_is_null))

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

        val viewModelProviderFactory = ViewModelProviderFactory(requireActivity().application)
        getSessionId()
        onMovieClickListener()
        initViewModel(viewModelProviderFactory)
        downloadData()
        onBackPressed()
    }

    private fun downloadData() {
        viewModel.downloadData(MoviesFragment.sessionId)
        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                LoadingState.IS_LOADING -> binding.progressBar.visibility = View.VISIBLE
                LoadingState.FINISHED -> binding.progressBar.visibility = View.GONE
                LoadingState.SUCCESS -> viewModel.movies.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                    binding.rvFavouritesMovies.adapter = adapter
                }
                else -> throw RuntimeException(getString(R.string.error))
            }
        }
    }

    private fun initViewModel(viewModelProviderFactory: ViewModelProviderFactory) {
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[ViewModelFavourites::class.java]
    }

    private fun getSessionId() {
        try {
            MoviesFragment.sessionId = prefSettings.getString(
                LoginFragment.SESSION_ID_KEY,
                null
            ) as String
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

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                requireContext().let {
                    AlertDialog
                        .Builder(it)
                        .setMessage(R.string.exit_app)
                        .setPositiveButton(R.string.yes) { dialogInterface, i ->
                            requireActivity().finish()
                        }
                        .setNegativeButton(R.string.No) { dialogInterface, i -> }
                        .create()
                        .show()
                }

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}