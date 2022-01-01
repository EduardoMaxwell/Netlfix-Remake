package br.com.eduardomaxwell.netflixremake.kotlin

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardomaxwell.netflixremake.R
import br.com.eduardomaxwell.netflixremake.databinding.ActivityMovieBinding
import br.com.eduardomaxwell.netflixremake.databinding.MovieItemBinding
import br.com.eduardomaxwell.netflixremake.model.Movie
import br.com.eduardomaxwell.netflixremake.util.MovieDetailTask
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target

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
                    txtCastMovie.text = getString(R.string.cast, movieDetail.movie.cast)

                    Glide.with(this@MovieActivity)
                        .load(movieDetail.movie.coverUrl)
                        .listener(object : RequestListener<Drawable> {

                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                val drawable: LayerDrawable = ContextCompat.getDrawable(
                                    baseContext,
                                    R.drawable.shadows
                                ) as LayerDrawable

                                drawable.let {
                                    drawable.setDrawableByLayerId(R.id.cover_drawable, resource)
                                    (target as DrawableImageViewTarget).view.setImageDrawable(drawable)
                                }
                                return true
                            }

                        })
                        .into(binding.ivCover)

                    movieAdapter.movies.clear()
                    movieAdapter.movies.addAll(movieDetail.similarMovies)
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
                Glide.with(itemView.context)
                    .load(movie.coverUrl)
                    .placeholder(R.drawable.placeholder_bg)
                    .into(binding.ivCover)
            }
        }
    }
}