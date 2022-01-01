package br.com.eduardomaxwell.netflixremake.kotlin

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardomaxwell.netflixremake.R
import br.com.eduardomaxwell.netflixremake.databinding.ActivityMovieBinding
import br.com.eduardomaxwell.netflixremake.databinding.MovieItemBinding
import br.com.eduardomaxwell.netflixremake.model.Movie
import br.com.eduardomaxwell.netflixremake.model.MovieDetail
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            val id = it.getInt("id")

            retrofit().create(NetflixAPI::class.java)
                .getMovieBy(id)
                .enqueue(object : Callback<MovieDetail> {
                    override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                        Toast.makeText(this@MovieActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<MovieDetail>,
                        response: Response<MovieDetail>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { movieDetail ->
                                binding.apply {
                                    txtMovieTitle.text = movieDetail.title
                                    txtDescMovie.text = movieDetail.desc
                                    txtCastMovie.text = getString(R.string.cast, movieDetail.cast)

                                    Glide.with(this@MovieActivity)
                                        .load(movieDetail.coverUrl)
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
                                                val drawable: LayerDrawable =
                                                    ContextCompat.getDrawable(
                                                        baseContext,
                                                        R.drawable.shadows
                                                    ) as LayerDrawable

                                                drawable.let {
                                                    drawable.setDrawableByLayerId(
                                                        R.id.cover_drawable,
                                                        resource
                                                    )
                                                    (target as DrawableImageViewTarget).view.setImageDrawable(
                                                        drawable
                                                    )
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
                        }
                    }

                })
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