package roman.com.popularmovies.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import roman.com.popularmovies.R;
import roman.com.popularmovies.dataobjects.movies.Movie;
import roman.com.popularmovies.listeners.OnRecyclerItemClickListener;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private List<Movie> mMovieList;
    private Context mContext;
    private OnRecyclerItemClickListener mRecyclerItemClickListener;

    public RecyclerViewAdapter(Context context, List<Movie> movieList, OnRecyclerItemClickListener clickListener) {
        mMovieList = movieList;
        mContext = context;
        mRecyclerItemClickListener = clickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_item, null);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(layoutView);
        recyclerViewHolder.setRecyclerItemClickListener(mRecyclerItemClickListener);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.movieName.setText(mMovieList.get(position).getTitle());
        holder.movieRating.setText(mMovieList.get(position).getVoteAverage().toString());
        Picasso.with(mContext)
                .load(mMovieList.get(position).getPosterPath())
                .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return this.mMovieList.size();
    }

    /**
     * replace the list in the adapter and call notifydatasetchanged
     * @param notesList
     */
    public void replaceData(@NonNull List<Movie> notesList) {
        mMovieList = checkNotNull(notesList);
        notifyDataSetChanged();
    }

    public Movie getItemByPosition(int position) {
        return mMovieList.get(position);
    }
}