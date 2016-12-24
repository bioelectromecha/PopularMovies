package roman.com.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import roman.com.popularmovies.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView movieName;
    public ImageView movieImage;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
        movieName = (TextView) itemView.findViewById(R.id.movie_name);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}