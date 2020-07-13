package org.example.imageviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FlickrListViewAdapter extends ArrayAdapter {

    private List<Photo> photoList;
    private Context context;

    private final LayoutInflater layoutInflater;
    private final int layoutResource;

    public FlickrListViewAdapter(Context context, int resource, List<Photo> photoList) {
        super(context, resource);
        this.context = context;
        this.layoutResource = resource;
        this.photoList = photoList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void loadNewData(List<Photo> newPhotoList) {
        this.photoList = newPhotoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (photoList!=null) ? photoList.size() : 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
        }

        ImageView thumbnail = convertView.findViewById(R.id.thumbnail);
        TextView title = convertView.findViewById(R.id.title);

        Photo currentPhoto = photoList.get(position);

        Picasso.get().load(currentPhoto.getImage())
                .error(R.drawable.broken_image_placeholder)
                .placeholder(R.drawable.placeholder)
                .into(thumbnail);

        title.setText(currentPhoto.getTitle());

        return convertView;
    }
}
