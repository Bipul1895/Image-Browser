package org.example.imageviewer;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "MainActivity";
    private static final String MY_PREF_NAME = "MainActivity";

    FlickrListViewAdapter flickrListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar(false);

        ListView listView = findViewById(R.id.list_view);

        flickrListViewAdapter = new FlickrListViewAdapter(this, R.layout.recy_view_support, new ArrayList<Photo>());

        listView.setAdapter(flickrListViewAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(SearchActivity.MY_PREF_NAME, MODE_PRIVATE);
        String query = sharedPreferences.getString(FLICKR_QUERY, "");
        //in case there is no string in shared Pref, we will get back "", which is the 2nd param

        if(query.length() != 0) {
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData(this, "https://www.flickr.com/services/feeds/photos_public.gne\n", "en-us", true);
            getFlickrJsonData.execute(query);
        }

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //This method provides list of Photo objects to FlickrRecyclerViewAdapter
    public void onDataAvailable(List<Photo> photoList, DownloadStatus status){
        if(status == DownloadStatus.OK){
            Log.d(TAG, "onDataAvailable: data is " + photoList);
            //send data to the recycler view adapter
            flickrListViewAdapter.loadNewData(photoList);
        }
        else {
            Log.e(TAG, "onDataAvailable: failed with status " + status);
        }

        Log.d(TAG, "onDataAvailable: ends");
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(this, "Simple Click : " + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemLongClick: starts");

        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, flickrListViewAdapter.getPhoto(i));
        //We can pass primitive types easily using putExtra
        //Passing complex object using putExtra is also possible but the object should meet one
        //condition, it should be Serializable
        //For this the object must implement Serializable interface
        startActivity(intent);

        return true;
        //return false will call onItemClick() method after calling this method.
    }
}
