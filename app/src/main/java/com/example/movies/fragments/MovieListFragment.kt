package com.example.movies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.api.TheMovieDBClient
import com.example.movies.databinding.FragmentMovieListBinding
import com.example.movies.repository.MoviePagedListRepository
import com.example.movies.repository.NetworkState
import com.example.movies.viewmodel.MovieListViewModel
import com.example.movies.viewmodel.MovieListViewModelFactory

class MovieListFragment: Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewModel: MovieListViewModel by viewModels {
        MovieListViewModelFactory(
            MoviePagedListRepository(movieDBApi = TheMovieDBClient.getClient())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagedListAdapter = PopularMoviePagedListAdapter(requireContext()) { movieId ->
            findNavController().navigate(
                MovieListFragmentDirections.toMovieDetailsFragment(movieId)
            )
        }
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = pagedListAdapter.getItemViewType(position)
                return if(viewType == pagedListAdapter.MOVIE_VIEW_TYPE) 1 else 3
            }
        }

        binding.movieRecyclerView.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(false)
            adapter = pagedListAdapter
        }

        viewModel.moviePagedList.observe(viewLifecycleOwner, Observer { list ->
            pagedListAdapter.submitList(list)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {networkState ->
            binding.progressBar.visibility = if(viewModel.listIsEmpty() && networkState == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.errorTextView.visibility = if(viewModel.listIsEmpty() && networkState == NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                pagedListAdapter.setNetworkState(networkState)
            }
        })
    }


}