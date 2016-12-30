package roman.com.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import roman.com.popularmovies.R;
import roman.com.popularmovies.dataobjects.Result;
import roman.com.popularmovies.utils.Constants;


public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Result movie = getActivity().getIntent().getExtras().getParcelable(Constants.KEY_MOVIE_PARCEL);

        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_movie_image);
        //put the poster inside the imageview
        Picasso.with(getContext())
                .load(movie.getPosterPath())
                .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                .into(imageView);

        TextView textview = (TextView) view.findViewById(R.id.movie_title);
        textview.setText(movie.getTitle());

        textview = (TextView) view.findViewById(R.id.movie_date);
        textview.setText(movie.getReleaseDate());

        textview = (TextView) view.findViewById(R.id.movie_rating);
        textview.setText(String.valueOf(movie.getVoteAverage()));

        textview = (TextView) view.findViewById(R.id.movie_description);
        textview.setText(movie.getOverview());

        getActivity().setTitle(movie.getTitle());

    }
}
