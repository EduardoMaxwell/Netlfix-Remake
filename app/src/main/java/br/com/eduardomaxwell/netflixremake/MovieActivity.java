package br.com.eduardomaxwell.netflixremake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import br.com.eduardomaxwell.netflixremake.databinding.ActivityMovieBinding;

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
        }

        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);
        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4);
            drawable.setDrawableByLayerId(R.id.cover_drawable, movieCover);
            ((ImageView) findViewById(R.id.ivCover)).setImageDrawable(drawable);
        }

    }
}