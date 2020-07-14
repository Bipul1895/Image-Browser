package org.example.imageviewer;

import android.content.Intent;
import android.content.res.Resources;
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
            Resources resources = getResources();
            String title_text = resources.getString(R.string.photo_title_text, photoData.getTitle());
            photoTitle.setText(title_text);

            TextView photoTags = findViewById(R.id.photo_tags);
            photoTags.setText(resources.getString(R.string.photo_tags_text, photoData.getTags()));

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
