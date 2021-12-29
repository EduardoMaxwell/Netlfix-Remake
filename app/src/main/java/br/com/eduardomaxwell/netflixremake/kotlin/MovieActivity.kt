package br.com.eduardomaxwell.netflixremake.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardomaxwell.netflixremake.R
import br.com.eduardomaxwell.netflixremake.databinding.ActivityMovieBinding
import br.com.eduardomaxwell.netflixremake.databinding.MovieItemBinding
import br.com.eduardomaxwell.netflixremake.model.Movie
import br.com.eduardomaxwell.netflixremake.util.ImageDownloaderTask
import br.com.eduardomaxwell.netflixremake.util.MovieDetailTask

class MovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            val id = it.getInt("id")
            val movieDetailTask = MovieDetailTask(this)
            movieDetailTask.setMovieDetailLoader { movieDetail ->
                binding.apply {
                    txtMovieTitle.text = movieDetail.movie.title
                    txtDescMovie.text = movieDetail.movie.desc
                    txtCastMovie.text = getString(R.string.cast, movieDetail.movie.title)

                    ImageDownloaderTask(binding.ivCover).apply {
                        setShadowEnabled(true)
                        execute(movieDetail.movie.coverUrl)
                    }
                    movieAdapter.movies.clear()
                    movieAdapter.movies.addAll(movieDetail.moviesSimilar)
                    movieAdapter.notifyDataSetChanged()
                }
            }
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/$id")
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let { toolbar ->
            toolbar.setDisplayHomeAsUpEnabled(true)
            toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            toolbar.title = null
        }
        val movies = arrayListOf<Movie>()
        movieAdapter = MovieAdapter(movies)
        binding.rvSimilar.adapter = movieAdapter
        binding.rvSimilar.layoutManager = GridLayoutManager(this, 3)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class MovieAdapter(val movies: MutableList<Movie>) :
        RecyclerView.Adapter<MovieHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
            MovieHolder(layoutInflater.inflate(R.layout.movie_item_similar, parent, false))

        override fun onBindViewHolder(holder: MovieHolder, position: Int) =
            holder.bind(movies[position])

        override fun getItemCount() = movies.size

    }

    private inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = MovieItemBinding.bind(itemView)

        fun bind(movie: Movie) {
            binding.apply {
                ImageDownloaderTask(ivCover).execute(movie.coverUrl)
            }
        }
    }
}