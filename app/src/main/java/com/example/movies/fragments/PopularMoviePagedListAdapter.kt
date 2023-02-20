package com.example.movies.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.api.POSTER_BASE_URL
import com.example.movies.databinding.MovieListItemBinding
import com.example.movies.databinding.NetworkStateItemBinding
import com.example.movies.model.Movie
import com.example.movies.repository.NetworkState

class PopularMoviePagedListAdapter(private val context: Context, private val onMovieClick: (movieId: Int) -> Unit) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    //two types to show in the recyclerView
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == MOVIE_VIEW_TYPE) {
            MovieViewHolder(MovieListItemBinding.inflate(inflater, parent, false))
        } else {
            NetworkStateViewHolder(NetworkStateItemBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieViewHolder).bind(getItem(position), context, onMovieClick)
        }else{
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousNetworkState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount()) //remove progressbar at the end
            }else{
                notifyItemInserted(super.getItemCount()) //add the progressbar at the end
            }
        } else if(hasExtraRow && previousNetworkState != newNetworkState){
            notifyItemChanged(itemCount - 1)
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

}

class MovieViewHolder(
    private val binding: MovieListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie?, context: Context, onMovieClick: (movieId: Int) -> Unit) {
        binding.movieTitle.text = movie?.title
        binding.movieReleaseDate.text = movie?.releaseDate

        val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
        Glide.with(context)
            .load(moviePosterURL)
            .into(binding.moviePoster)

        binding.root.setOnClickListener {
            movie?.let { movie -> onMovieClick(movie.id) }
        }
    }

}

class NetworkStateViewHolder(
    private val binding: NetworkStateItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(networkState: NetworkState?) {
        networkState?.let {
            when (networkState) {
                NetworkState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorItem.visibility = View.GONE
                }
                NetworkState.END_OF_LIST, NetworkState.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorItem.visibility = View.VISIBLE
                    binding.errorItem.text = networkState.msg
                }
            }
        }

    }
}