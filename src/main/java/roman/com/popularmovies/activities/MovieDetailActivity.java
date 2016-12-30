package roman.com.popularmovies.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import roman.com.popularmovies.R;
import roman.com.popularmovies.fragments.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String KEY_MOVIE_DETAIL_FRAGMENT = "MOVIE_DETAIL_FRAGMENT";
    private MovieDetailFragment mMovieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

//        set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // get the fragment if it already exists
        mMovieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(KEY_MOVIE_DETAIL_FRAGMENT);

        // add the fragment if not yet added
        if (mMovieDetailFragment == null) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MovieDetailFragment detailFragment = new MovieDetailFragment();
            fragmentTransaction.replace(R.id.detail_fragment_container, detailFragment, KEY_MOVIE_DETAIL_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
