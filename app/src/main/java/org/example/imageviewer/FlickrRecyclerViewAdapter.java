package org.example.imageviewer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";

    private List<Photo> photoList;
    private Context context;//For picasso

    public FlickrRecyclerViewAdapter(List<Photo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");

        View view = LayoutInflater.from(context).inflate(R.layout.recy_view_support, parent, false);

        return new FlickrImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        //Called by the layout manager when it wants new data in an existing row
        Photo photoData = photoList.get(position);

        holder.title.setText(photoData.getTitle());

//        Picasso.get().load(photoData.getImage()).into(holder.thumbnail);
        Picasso.get().load(photoData.getImage())
                .error(R.drawable.placeholder) //if there is an error downloading the image from url
                .placeholder(R.drawable.placeholder) // while image is getting downloaded
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return (photoList != null) ? photoList.size():0;
    }

    public void loadNewData(List<Photo> newPhotoList) {
        this.photoList = newPhotoList;
        notifyDataSetChanged();
    }

    //This is used to hold views so that we do not have to call findViewById() again and again
    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;


        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
        }
    }

}
