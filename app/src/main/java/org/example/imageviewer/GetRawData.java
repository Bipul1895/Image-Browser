package org.example.imageviewer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED, OK}

public class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    private DownloadStatus downloadStatus;
    private GetFlickrJsonData callBack;


    public GetRawData(GetFlickrJsonData callBack) {
        downloadStatus = DownloadStatus.IDLE;
        this.callBack = callBack;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter : " + s);

        if(callBack != null){
            callBack.onDownloadComplete(s, downloadStatus);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(strings == null){
            downloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {

            downloadStatus = DownloadStatus.PROCESSING;

            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "doInBackground: Response code : " + responseCode);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line=reader.readLine()) != null){
                //When we use readLine() method, it reads a complete line at time and strips off
                //the newline character so we have to add it manually.
                result.append(line).append('\n');
            }

            downloadStatus = DownloadStatus.OK;

            if(connection != null){
                connection.disconnect();
            }
            if(reader != null){
                reader.close();
                //close() method also throws IO Exception
            }

            return result.toString();

        }
        catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL : " + e.getMessage());
        }
        catch (IOException e){
            Log.e(TAG, "doInBackground: IO Exception trying to read data : " + e.getMessage());
        }
        catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception, Needs permission? : " + e.getMessage());
        }

        downloadStatus = DownloadStatus.FAILED;

        return null;

    }


}
