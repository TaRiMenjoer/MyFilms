package com.example.myfilms.presentation.movies

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myfilms.R
import com.example.myfilms.data.model.Movie
import com.example.myfilms.data.repository.MovieRepositoryImpl
import com.example.myfilms.databinding.FragmentMoviesBinding
import com.example.myfilms.presentation.common.Utils.LoadingState
import com.example.myfilms.presentation.common.adapter.MoviesAdapter
import com.example.myfilms.presentation.login.LoginFragment
import com.example.myfilms.presentation.movieDetails.DetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = _binding ?: throw RuntimeException(getString(R.string.fragment_films_binding_is_null))

    private val adapter = MoviesAdapter()

    private val viewModel by viewModel<ViewModelMovie>()

    private lateinit var prefSettings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefSettings = context?.getSharedPreferences(
            LoginFragment.APP_SETTINGS, Context.MODE_PRIVATE
        ) as SharedPreferences
        editor = prefSettings.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSessionId()
        initAndObserveViewModel()
        onMovieClickListener()
        onBackPressed()
        setSwipeRefresh()
    }

    private fun setSwipeRefresh(){
        binding.swipeRefresh.setOnRefreshListener {

            MovieRepositoryImpl.isFirstDownloaded = false

            viewModel.refreshingData()

            viewModel.refreshingState.observe(viewLifecycleOwner) {
                when (it) {
                    LoadingState.IS_LOADING -> binding.swipeRefresh.isRefreshing = true
                    LoadingState.FINISHED -> binding.swipeRefresh.isRefreshing = false
                    else -> throw RuntimeException(getString(R.string.error))
                }
            }
        }

    }

    private fun initAndObserveViewModel() {

        viewModel.downloadData()

        viewModel.loadingState.observe(viewLifecycleOwner) {

                when (it) {
                    LoadingState.IS_LOADING -> binding.progressBar.visibility = View.VISIBLE
                    LoadingState.FINISHED -> binding.progressBar.visibility = View.GONE
                    else -> throw RuntimeException(getString(R.string.error))
                }

        }

        viewModel.movies.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.rvMovies.adapter = adapter
        }
    }

    private fun getSessionId() {
        try {
            sessionId = prefSettings.getString(LoginFragment.SESSION_ID_KEY, null) as String
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
        findNavController().navigate(R.id.action_movies_fragment_to_details_fragment, args)
    }


    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                requireContext().let {
                    AlertDialog
                        .Builder(it)
                        .setMessage(getString(R.string.exit_app))
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

    companion object {

        var sessionId: String = ""
    }
}