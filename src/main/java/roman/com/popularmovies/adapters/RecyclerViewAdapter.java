package roman.com.popularmovies.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import roman.com.popularmovies.R;
import roman.com.popularmovies.dataobjects.Movie;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Movie> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Movie> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_item, null);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(layoutView);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.movieName.setText(itemList.get(position).getName());
        Picasso.with(context)
                .load("http://plusquotes.com/images/quotes-img/cool-pictures-24.jpg")
                .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                .into(holder.movieImage);
    }



    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}