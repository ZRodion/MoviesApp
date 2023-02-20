package com.example.movies.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.api.POSTER_BASE_URL
import com.example.movies.api.TheMovieDBClient
import com.example.movies.databinding.FragmentMovieDetailsBinding
import com.example.movies.model.MovieDetails
import com.example.movies.repository.MovieDetailsRepository
import com.example.movies.repository.NetworkState
import com.example.movies.viewmodel.MovieDetailsViewModel
import com.example.movies.viewmodel.MovieDetailsViewModelFactory
import java.text.NumberFormat
import java.util.*

class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var movieDetailsRepository: MovieDetailsRepository

    private val viewModel: MovieDetailsViewModel by viewModels {
        MovieDetailsViewModelFactory(
            MovieDetailsRepository(apiService = TheMovieDBClient.getClient()),
            315162
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieDetailsRepository = MovieDetailsRepository(apiService = TheMovieDBClient.getClient())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            movieDetails?.let { updateUI(it) }
        }
        viewModel.networkState.observe(viewLifecycleOwner) { networkState ->
            binding.apply {
                progressBar.visibility =
                    if (networkState == NetworkState.LOADING) View.VISIBLE else View.GONE
                textError.visibility =
                    if (networkState == NetworkState.ERROR) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun updateUI(movieDetails: MovieDetails) {
        binding.apply {
            movieTitle.text = movieDetails.title
            movieTagline.text = movieDetails.tagline
            movieDate.text = movieDetails.releaseDate
            movieRating.text = movieDetails.rating.toString()
            movieRuntime.text = movieDetails.runtime.toString() + " minutes"
            movieOverview.text = movieDetails.overview

            val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
            movieBudget.text = formatCurrency.format(movieDetails.budget).toString()
            movieRevenue.text = formatCurrency.format(movieDetails.revenue).toString()

            val moviePosterUrl = POSTER_BASE_URL + movieDetails.posterPath
            Glide.with(requireContext())
                .load(moviePosterUrl)
                .into(movieImage)
        }
    }
}