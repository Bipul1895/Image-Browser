package org.example.imageviewer;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener{

    private android.widget.SearchView searchView;

    public static final String MY_PREF_NAME = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activateToolbar(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);

        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(this);//important

        return true;
    }

    //This method provides search tags to MainActivity, which uses tags to alter the URL
    //Thus we get the images that match our query as we are actually changing the requested URL.
    @Override
    public boolean onQueryTextSubmit(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putString(FLICKR_QUERY, s).apply();
        finish();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
