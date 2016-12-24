package roman.com.popularmovies.activities;

import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import roman.com.popularmovies.fragments.MoviesFragment;
import roman.com.popularmovies.R;

public class MoviesActivity extends AppCompatActivity implements MoviesFragment.OnFragmentInteractionListener {

    private static final String KEY_MOVIES_FRAGMENT = "MOVIES_FRAGMENT";
    private MoviesFragment mMoviesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

//        set the toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);



        // get the fragment if it already exists
        mMoviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(KEY_MOVIES_FRAGMENT);

        // add the fragment if not yet added
        if (mMoviesFragment == null) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MoviesFragment moviesFragment = new MoviesFragment();
            fragmentTransaction.replace(R.id.fragment_container, moviesFragment, KEY_MOVIES_FRAGMENT);
            fragmentTransaction.commit();
        }



    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Fragment Interaction Registered On Activity", Toast.LENGTH_SHORT).show();
    }
}
