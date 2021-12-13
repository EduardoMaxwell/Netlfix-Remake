package br.com.eduardomaxwell.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import br.com.eduardomaxwell.netflixremake.databinding.ActivityMovieBinding;
import br.com.eduardomaxwell.netflixremake.model.Movie;

public class MovieActivity extends AppCompatActivity {

    private ActivityMovieBinding binding;

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


        binding.txtMovieTitle.setText(R.string.movie_title);
        binding.txtDescMovie.setText(R.string.movie_desc);
        binding.txtCastMovie.setText(getString(R.string.cast, "Christian Bale " + ",Cillian Murphy" + ",Gary Oldman" + ",Katie Holmes" + ",Liam Neeson" + ",Morgan Freeman" + ",Tom Wilkinson" + ",Michael Caine"));


        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movies.add(movie);
        }
        setupRecycler(movies);

    }

    private void setupRecycler(List<Movie> movies) {
        binding.rvSimilar.setAdapter(new MovieAdapter(movies));
        binding.rvSimilar.setLayoutManager(new GridLayoutManager(this, 3));
    }

    //    MOVIE ADAPTER
    private class MovieAdapter extends RecyclerView.Adapter<MovieActivity.MovieHolder> {

        private final List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieActivity.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieActivity.MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieActivity.MovieHolder holder, int position) {
            Movie movie = movies.get(position);
//            holder.imgCover.setImageResource(movie.getCoverUrl());
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