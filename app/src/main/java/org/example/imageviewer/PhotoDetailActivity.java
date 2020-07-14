package org.example.imageviewer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        Intent intent = getIntent();
        Photo photoData = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if(photoData != null){
            TextView photoTitle = findViewById(R.id.photo_title);
            photoTitle.setText("Title: " + photoData.getTitle());

            TextView photoTags = findViewById(R.id.photo_tags);
            photoTags.setText("Tags: " + photoData.getTags());

            TextView photoAuthor = findViewById(R.id.photo_author);
            photoAuthor.setText(photoData.getAuthor());

            ImageView photoImage = findViewById(R.id.photo_image);

            Picasso.get().load(photoData.getLink())
                    .error(R.drawable.broken_image_placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }

    }

}
