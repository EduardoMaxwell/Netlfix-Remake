package br.com.eduardomaxwell.netflixremake.kotlin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardomaxwell.netflixremake.R
import br.com.eduardomaxwell.netflixremake.databinding.ActivityMainBinding
import br.com.eduardomaxwell.netflixremake.databinding.CategoryItemBinding
import br.com.eduardomaxwell.netflixremake.databinding.MovieItemBinding
import br.com.eduardomaxwell.netflixremake.model.Category
import br.com.eduardomaxwell.netflixremake.model.Movie
import br.com.eduardomaxwell.netflixremake.util.CategoryTask
import br.com.eduardomaxwell.netflixremake.util.ImageDownloaderTask

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

        val categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader { categories1 ->
            mainAdapter.categories.clear()
            mainAdapter.categories.addAll(categories1)
            mainAdapter.notifyDataSetChanged()
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    private inner class MainAdapter(val categories: MutableList<Category>) :
        RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            return CategoryHolder(layoutInflater.inflate(R.layout.category_item, parent, false))
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            holder.bind(category)
        }

        override fun getItemCount() = categories.size

    }

    private inner class MovieAdapter(val movies: List<Movie>) :
        RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(layoutInflater.inflate(R.layout.movie_item, parent, false))
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
            binding.txtTitleCategory.text = category.name
            binding.rvMovie.adapter = MovieAdapter(category.movies)
            binding.rvMovie.layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = MovieItemBinding.bind(itemView)
        fun bind(movie: Movie) {
            ImageDownloaderTask(binding.ivCover)
                .execute(movie.coverUrl)
            binding.ivCover.setOnClickListener {

            }
        }
    }
}
