package br.com.eduardomaxwell.netflixremake.kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardomaxwell.netflixremake.R
import br.com.eduardomaxwell.netflixremake.databinding.ActivityMainBinding
import br.com.eduardomaxwell.netflixremake.databinding.CategoryItemBinding
import br.com.eduardomaxwell.netflixremake.databinding.MovieItemBinding
import br.com.eduardomaxwell.netflixremake.model.Categories
import br.com.eduardomaxwell.netflixremake.model.Category
import br.com.eduardomaxwell.netflixremake.model.Movie
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categories = arrayListOf<Category>()
        mainAdapter = MainAdapter(categories)
        binding.recyclerMain.adapter = mainAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this)

        retrofit().create(NetflixAPI::class.java)
            .listCategories().enqueue(object : Callback<Categories> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            mainAdapter.categories.clear()
                            mainAdapter.categories.addAll(it.categories)
                            mainAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<Categories>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    private inner class MainAdapter(val categories: MutableList<Category>) :
        RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder =
            CategoryHolder(layoutInflater.inflate(R.layout.category_item, parent, false))


        override fun onBindViewHolder(holder: CategoryHolder, position: Int) =
            holder.bind(categories[position])

        override fun getItemCount() = categories.size

    }

    private inner class MovieAdapter(
        val movies: List<Movie>,
        private val listener: ((Movie) -> Unit)
    ) :
        RecyclerView.Adapter<MovieHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(layoutInflater.inflate(R.layout.movie_item, parent, false), listener)
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies[position]
            holder.bind(movie)
        }

        override fun getItemCount() = movies.size

    }

    private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CategoryItemBinding.bind(itemView)
        fun bind(category: Category) {

            binding.apply {
                txtTitleCategory.text = category.name
                rvMovie.adapter = MovieAdapter(category.movies) { movie ->
                    if (movie.id > 3) {
                        Toast.makeText(
                            this@MainActivity,
                            "Não foi implementado o clique para essa opção!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val intent = Intent(this@MainActivity, MovieActivity::class.java)
                        intent.putExtra("id", movie.id)
                        startActivity(intent)
                    }
                }
                rvMovie.layoutManager =
                    LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    private class MovieHolder(itemView: View, val onClick: ((Movie) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {
        val binding = MovieItemBinding.bind(itemView)
        fun bind(movie: Movie) {

            Glide.with(itemView.context)
                .load(movie.coverUrl)
                .placeholder(R.drawable.placeholder_bg)
                .into(binding.ivCover)

            binding.ivCover.setOnClickListener {
                onClick?.invoke(movie)
            }
        }
    }
}
