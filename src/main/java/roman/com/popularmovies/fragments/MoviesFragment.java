package roman.com.popularmovies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import roman.com.popularmovies.R;
import roman.com.popularmovies.adapters.RecyclerViewAdapter;
import roman.com.popularmovies.dataobjects.Movie;
import roman.com.popularmovies.network.ApiCallback;
import roman.com.popularmovies.network.ApiManager;
import roman.com.popularmovies.utils.Constants;


public class MoviesFragment extends Fragment implements ApiCallback {
    private GridLayoutManager mGridLayoutManager;
    private ApiManager mApiManager = ApiManager.getInstance();

    private OnFragmentInteractionListener mListener;
    private static final int NUM_OF_COLUMNS = 2;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridLayoutManager = new GridLayoutManager(getContext(), NUM_OF_COLUMNS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        List<Movie> itemList = getAllItemList();
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mGridLayoutManager);

        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(getContext(), itemList);
        recyclerView.setAdapter(recyclerAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mApiManager.getMovies(new WeakReference<ApiCallback>(this), Constants.KEY_MOST_POPULAR);
    }

    @Override
    public void onFailure(Throwable error) {
        LogUtils.d("Failure");
    }

    @Override
    public void onSuccess(Response response) {
        LogUtils.d("Success");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private List<Movie> getAllItemList(){

        List<Movie> allItems = new ArrayList<>();
        allItems.add(new Movie("United States", R.mipmap.ic_launcher));
        allItems.add(new Movie("Canada", R.mipmap.ic_launcher));
        allItems.add(new Movie("United Kingdom", R.mipmap.ic_launcher));
        allItems.add(new Movie("Germany", R.mipmap.ic_launcher));
        allItems.add(new Movie("Sweden", R.mipmap.ic_launcher));
        allItems.add(new Movie("United Kingdom", R.mipmap.ic_launcher));
        allItems.add(new Movie("Germany", R.mipmap.ic_launcher));
        allItems.add(new Movie("Sweden", R.mipmap.ic_launcher));
        allItems.add(new Movie("United States", R.mipmap.ic_launcher));
        allItems.add(new Movie("Canada",R.mipmap.ic_launcher));
        allItems.add(new Movie("United Kingdom", R.mipmap.ic_launcher));
        allItems.add(new Movie("Germany", R.mipmap.ic_launcher));
        allItems.add(new Movie("Sweden", R.mipmap.ic_launcher));
        allItems.add(new Movie("United Kingdom", R.mipmap.ic_launcher));
        allItems.add(new Movie("Germany", R.mipmap.ic_launcher));
        allItems.add(new Movie("Sweden", R.mipmap.ic_launcher));

        return allItems;
    }
}
