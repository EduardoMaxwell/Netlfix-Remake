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
import br.com.eduardomaxwell.netflixremake.model.Category
import br.com.eduardomaxwell.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categories = arrayListOf<Category>()
        val mainAdapter = MainAdapter(categories)
        binding.recyclerMain.adapter = mainAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this)

        val categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader { categories1 ->
            categories1.size
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    private inner class MainAdapter(val categories: List<Category>) :
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

    private class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CategoryItemBinding.bind(itemView)
        fun bind(category: Category) {
            binding.txtTitleCategory.text = category.name
        }
    }
}