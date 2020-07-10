package org.example.imageviewer;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED, OK}

public class GetRawData {
    private static final String TAG = "GetRawData";

    private DownloadStatus downloadStatus;
    private GetFlickrJsonData callBack;


    public GetRawData(GetFlickrJsonData callBack) {
        downloadStatus = DownloadStatus.IDLE;
        this.callBack = callBack;
    }

    //Called by doInBackground of GetFlickrJsonData
    //So it is running on background thread
    //Thus, sendBackToCaller() and downloadJsonData() are also running on background thread
    void runOnSameThread(String destinationUri) {
        Log.d(TAG, "runOnSameThread: starts");
//        if(Looper.myLooper() != Looper.getMainLooper()) {
//            Log.d(TAG, "runOnSameThread: we are on background thread");
//        }
//        else{
//            Log.d(TAG, "runOnSameThread: we are on main thread");
//        }

        sendBackToCaller(downloadJsonData(destinationUri));

        Log.d(TAG, "runOnSameThread: ends");

    }

    private void sendBackToCaller(String s) {
        Log.d(TAG, "onPostExecute: parameter : " + s);

//        if(Looper.myLooper() == Looper.getMainLooper()) {
//            Log.d(TAG, "onPostExecute: We are on main thread");
//        }
//        else{
//            Log.d(TAG, "onPostExecute: we are on background thread");
//        }

        if(callBack != null){
            callBack.onDownloadComplete(s, downloadStatus);
        }

        Log.d(TAG, "onPostExecute: ends");
    }


    private String downloadJsonData(String uri) {

//        if(Looper.myLooper() != Looper.getMainLooper()) {
//            Log.d(TAG, "doInBackground: We are on background thread");
//        }
//        else{
//            Log.d(TAG, "doInBackground: we are on main thread");
//        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(uri == null){
            downloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {

            downloadStatus = DownloadStatus.PROCESSING;

            URL url = new URL(uri);
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
