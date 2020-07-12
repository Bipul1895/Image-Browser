package org.example.imageviewer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo> > {
    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> photoList = null;
    private String baseURL;
    private String lang;
    private boolean matchAll;

    private MainActivity callBack;

    public GetFlickrJsonData(MainActivity callBack, String baseURL, String lang, boolean matchAll) {
        Log.d(TAG, "GetFlickrJsonData: Constructor called");
        this.callBack = callBack;
        this.baseURL = baseURL;
        this.lang = lang;
        this.matchAll = matchAll;

        photoList = new ArrayList<>();

    }

    /*
    public void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: starts");

        String destinationUri = createUri(searchCriteria, lang, matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);

        Log.d(TAG, "executeOnSameThread: ends");
    }
     */

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");

        if(callBack != null){
            callBack.onDataAvailable(photoList, DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0], lang, matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runOnSameThread(destinationUri);

        Log.d(TAG, "doInBackground: ends");
        return photoList;
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll) {
        Log.d(TAG, "createUri: starts");

        Uri uri = Uri.parse(baseURL);
        Uri.Builder builder = uri.buildUpon();
        builder = builder.appendQueryParameter("tags", searchCriteria);
        builder = builder.appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY");
        builder = builder.appendQueryParameter("lang", lang);
        builder = builder.appendQueryParameter("format", "json");
        builder = builder.appendQueryParameter("nojsoncallback", "1");

        uri = builder.build();

        return uri.toString();
    }

    //It is called from GetRawData's onPostExecute() method because it has callback of this class
    //It is run on background thread because onPostExecute of GetRawData is running on background
    //thread
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts with status " + status);

        if(Looper.myLooper() != Looper.getMainLooper()){
            Log.d(TAG, "onDownloadComplete: We are on background thread");
        }
        else{
            Log.d(TAG, "onDownloadComplete: We are on main thread");
        }

        if (status == DownloadStatus.OK) {

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);

                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");//smaller image

                    String link = photoUrl.replaceFirst("_m.", "_b.");//bigger version of the image

                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);
                    photoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete: ends, photo object : " + photoObject.toString());
                }

            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data" + jsone.getMessage());
                status = DownloadStatus.FAILED;
            }

        }

        Log.d(TAG, "onDownloadComplete: ends");
    }

}
