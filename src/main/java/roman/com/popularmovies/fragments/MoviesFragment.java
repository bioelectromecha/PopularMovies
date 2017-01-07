package roman.com.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import roman.com.popularmovies.MyApplication;
import roman.com.popularmovies.R;
import roman.com.popularmovies.activities.MovieDetailActivity;
import roman.com.popularmovies.adapters.RecyclerViewAdapter;
import roman.com.popularmovies.dataobjects.movies.Movie;
import roman.com.popularmovies.dataobjects.movies.MoviesHolder;
import roman.com.popularmovies.dataobjects.reviews.ReviewsHolder;
import roman.com.popularmovies.dataobjects.trailers.TrailersHolder;
import roman.com.popularmovies.listeners.OnRecyclerItemClickListener;
import roman.com.popularmovies.network.ApiCallback;
import roman.com.popularmovies.network.ApiManager;
import roman.com.popularmovies.utils.Constants;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class MoviesFragment extends Fragment implements ApiCallback, OnRecyclerItemClickListener {
    private static final int NUM_OF_COLUMNS = 2;
    private ApiManager mApiManager = ApiManager.getInstance();
    private GridLayoutManager mGridLayoutManager;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    //this progress bar will be displayed while data is loading from the internet
    private ProgressBar mProgressBar;
    //this textview will display a message when no internet connection is available
    private TextView mNoInternetTextView;
    private String mDisplayMode = Constants.KEY_MOST_POPULAR;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridLayoutManager = new GridLayoutManager(getContext(), NUM_OF_COLUMNS);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.circular_progress_bar);
        mNoInternetTextView = (TextView) view.findViewById(R.id.no_internet_placeholder);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecyclerViewAdapter = new RecyclerViewAdapter(getContext(), new ArrayList<Movie>(0), this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.movies_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.movies_sort_by_most_popular) {
            mDisplayMode = Constants.KEY_MOST_POPULAR;
            loadData();
        }
        if (item.getItemId() == R.id.movies_sort_by_highest_rated) {
            mDisplayMode = Constants.KEY_HIGHEST_RATED;
            loadData();
        }
        if (item.getItemId() == R.id.movies_show_favorites) {
            mDisplayMode = Constants.KEY_FAVORITES;
            loadData();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        //this also handles some view changes
        loadData();

    }

    @Override
    public void onFailure(Throwable error) {
        showNoConnectionMessage();
        LogUtils.d("Failure");
    }

    @Override
    public void onMoviesFetchSuccess(MoviesHolder moviesHolder) {
        checkNotNull(moviesHolder);
        onMoviesLoaded(moviesHolder.getMovies());
    }

    private void onMoviesLoaded(List<Movie> movies) {
        checkNotNull(movies);
        hideLoadingIndicator();
        mRecyclerViewAdapter.replaceData(movies);
    }

    @Override
    public void onReviewsFetchSuccess(ReviewsHolder reviewsHolder) {
        // reviews were fetched, do nothing
    }

    @Override
    public void onTrailersFetchSuccess(TrailersHolder trailersHolder) {
        // trailers were fetched, do nothing
    }

    /**
     * check if we're connected to the internet
     *
     * @return
     */
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * hide the recyclerview and spinner and show a 'no internet' message
     */
    private void showNoConnectionMessage() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mNoInternetTextView.setVisibility(View.VISIBLE);
    }

    /**
     * show a loader/spinner while data is loading from the internet
     */
    private void showLoadingIndicator() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mNoInternetTextView.setVisibility(View.GONE);
    }

    /**
     * hide the spinner/loader after the data has been loaded
     */
    private void hideLoadingIndicator() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mNoInternetTextView.setVisibility(View.GONE);
    }

    /**
     * load the data and show relevant view changes
     */
    private void loadData() {

        //if there's not internet connection - show the no connection message. UNLESS showing favorites, which are loaded from local storage
        if (!isConnectedToInternet() && !mDisplayMode.equals(Constants.KEY_FAVORITES)) {
            showNoConnectionMessage();
            return;
        }
        showLoadingIndicator();
        //load from local storage if displaying favorites, otherwise load from network
        if (mDisplayMode.equals(Constants.KEY_FAVORITES)) {
            List<Movie> movies = MyApplication.getMoviesDbStore().getAllValues();
            mRecyclerViewAdapter.setLoadFromLocalStorage(true);
            onMoviesLoaded(movies);
        } else {
            mRecyclerViewAdapter.setLoadFromLocalStorage(false);
            //load from network if it's not the favorites view
            mApiManager.getMovies(new WeakReference<ApiCallback>(this), mDisplayMode);
        }
    }

    @Override
    public void onClickRecyclerItem(View view, int position) {
        launchDetailsActivity(mRecyclerViewAdapter.getItemByPosition(position));
    }

    private void launchDetailsActivity(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(Constants.KEY_MOVIE_PARCEL, movie);
        startActivity(intent);
    }
}
