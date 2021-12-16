package br.com.eduardomaxwell.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import br.com.eduardomaxwell.netflixremake.databinding.ActivityMovieBinding;
import br.com.eduardomaxwell.netflixremake.model.Movie;
import br.com.eduardomaxwell.netflixremake.model.MovieDetail;
import br.com.eduardomaxwell.netflixremake.util.ImageDownloaderTask;
import br.com.eduardomaxwell.netflixremake.util.MovieDetailTask;

public class MovieActivity extends AppCompatActivity implements MovieDetailTask.MovieDetailLoader {

    private ActivityMovieBinding binding;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setTitle(null);
        }

        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);
        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4);
            drawable.setDrawableByLayerId(R.id.cover_drawable, movieCover);
            ((ImageView) findViewById(R.id.ivCover)).setImageDrawable(drawable);
        }

        List<Movie> movies = new ArrayList<>();

        setupRecycler(movies);

    }

    private void setupRecycler(List<Movie> movies) {
        adapter = new MovieAdapter(movies);
        binding.rvSimilar.setAdapter(adapter);
        binding.rvSimilar.setLayoutManager(new GridLayoutManager(this, 3));


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");

            MovieDetailTask movieDetailTask = new MovieDetailTask(this);
            movieDetailTask.setMovieDetailLoader(this);
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }
    }

    @Override
    public void onResult(MovieDetail movieDetail) {
        binding.txtMovieTitle.setText(movieDetail.getMovie().getTitle());
        binding.txtDescMovie.setText(movieDetail.getMovie().getDesc());
        binding.txtCastMovie.setText(movieDetail.getMovie().getCast());

        adapter.setMovies(movieDetail.getMoviesSimilar());
        adapter.notifyDataSetChanged();
        Log.d("Teste", movieDetail.toString());
    }

    //    MOVIE ADAPTER
    private class MovieAdapter extends RecyclerView.Adapter<MovieActivity.MovieHolder> {

        private List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        public void setMovies(List<Movie> movies) {
            this.movies.clear();
            this.movies.addAll(movies);
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieActivity.MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieActivity.MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            new ImageDownloaderTask(holder.imgCover).execute(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
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