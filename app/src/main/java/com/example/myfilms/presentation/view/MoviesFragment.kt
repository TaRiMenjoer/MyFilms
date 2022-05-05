package com.example.myfilms.presentation.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myfilms.R
import com.example.myfilms.data.model.Movie
import com.example.myfilms.databinding.FragmentMoviesBinding
import com.example.myfilms.presentation.Utils.LoadingState
import com.example.myfilms.presentation.view.adapter.MoviesAdapter
import com.example.myfilms.presentation.viewModel.ViewModelMovie
import com.example.myfilms.presentation.viewModel.ViewModelProviderFactory

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = _binding ?: throw RuntimeException("FragmentFilmsBinding is null")

    private val adapter = MoviesAdapter()
    private lateinit var viewModel: ViewModelMovie

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
    }

    private fun initAndObserveViewModel() {

        val viewModelProviderFactory = ViewModelProviderFactory(requireActivity())

        viewModel = ViewModelProvider(this, viewModelProviderFactory)[ViewModelMovie::class.java]


        viewModel.downloadData(PAGE)

        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                LoadingState.IS_LOADING -> binding.progressBar.visibility = View.VISIBLE
                LoadingState.FINISHED -> binding.progressBar.visibility = View.GONE
                LoadingState.SUCCESS -> viewModel.movies.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                    binding.rvMovies.adapter = adapter
                }
                else -> throw RuntimeException("Error")
            }
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

//                viewModel.deleteSession(sessionId)
//                editor.clear().commit()
//                findNavController().popBackStack(R.id.login_fragment , true)

                requireContext().let {
                    AlertDialog
                        .Builder(it)
                        .setMessage("Выйти?")
                        .setPositiveButton("Да") { dialogInterface, i ->
                            requireActivity().finish()
                        }
                        .setNegativeButton("Нет") { dialogInterface, i -> }
                        .create()
                        .show()
                }

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    companion object {

        var sessionId: String = ""
        var PAGE = 1

    }
}