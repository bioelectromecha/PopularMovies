package roman.com.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import roman.com.popularmovies.R;
import roman.com.popularmovies.listeners.OnRecyclerItemClickListener;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnRecyclerItemClickListener mRecyclerItemClickListener = null;
    public TextView movieName;
    public TextView movieRating;
    public ImageView movieImage;


    public RecyclerViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
        movieName = (TextView) itemView.findViewById(R.id.movie_name);
        movieRating = (TextView) itemView.findViewById(R.id.movie_rating);
    }

    public void setRecyclerItemClickListener(OnRecyclerItemClickListener clickListener) {
        mRecyclerItemClickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        if (mRecyclerItemClickListener != null) {
            mRecyclerItemClickListener.onClickRecyclerItem(view, getAdapterPosition());
        }
    }
}