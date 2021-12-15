package br.com.eduardomaxwell.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.eduardomaxwell.netflixremake.databinding.ActivityMainBinding;
import br.com.eduardomaxwell.netflixremake.model.Category;
import br.com.eduardomaxwell.netflixremake.model.Movie;
import br.com.eduardomaxwell.netflixremake.util.CategoryTask;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

    private ActivityMainBinding binding;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<Category> categories = new ArrayList<>();


        mainAdapter = new MainAdapter(categories);
        binding.recyclerMain.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recyclerMain.setAdapter(mainAdapter);

        CategoryTask categoryTask = new CategoryTask(this);
        categoryTask.setCategoryLoader(this);
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");
    }

    @Override
    public void onResult(List<Category> categories) {
        mainAdapter.setCategories(categories);
        mainAdapter.notifyDataSetChanged();
    }


    //    MAIN ADAPTER
    private class MainAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> categories;

        private MainAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = categories.get(position);
            holder.title.setText(category.getName());
            holder.recyclerMovie.setAdapter(new MovieAdapter(category.getMovies()));
            holder.recyclerMovie.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));

        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public void setCategories(List<Category> categories) {
            this.categories.clear();
            this.categories.addAll(categories);
        }
    }

    //    MOVIE ADAPTER
    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
//            holder.imgCover.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }


    private static class CategoryHolder extends RecyclerView.ViewHolder {

        TextView title;
        RecyclerView recyclerMovie;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitleCategory);
            recyclerMovie = itemView.findViewById(R.id.rvMovie);
        }
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView imgCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.ivCover);
        }

    }
}
