package org.example.imageviewer;

import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    protected final String FLICKR_QUERY = "FLICKR_QUERY";
    protected final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    void activateToolbar (boolean enableHome){
        Log.d(TAG, "activateToolvar: starts");

        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(toolbar != null){
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
        }

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }

    }

}
