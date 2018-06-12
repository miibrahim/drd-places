package fr.atecna.placesapplication.async;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import fr.atecna.placesapplication.model.FourSquarePlace;

public abstract class BaseAsyncTask<Progress, Result> extends AsyncTask<String, Progress, Result> {
  WeakReference<OnTaskFinished<Result>> onTaskFinishedObserverReference;

    public void setOnTaskFinishedObserver(OnTaskFinished<Result> onTaskFinishedObserver) {
        this.onTaskFinishedObserverReference = new WeakReference<>(onTaskFinishedObserver);
    }

    @Override
    protected Result doInBackground(String... urls) {
        try {
            Thread.sleep(5000);
            String jsonResult = getApi(urls[0]);
            Log.d(getClass().getSimpleName(), "result: " + jsonResult);
            Result result = parseResult(jsonResult);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract Result parseResult(String jsonResult) throws JSONException;

    protected String getApi(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection ();

// This does a GET; to do a POST, add conn.setDoOutput(true);
        conn.setDoInput ( true );
        conn.setAllowUserInteraction ( true );
        conn.connect ();

// To do a POST, you'd write to conn.getOutputStream();
        StringBuilder sb = new StringBuilder ();
        BufferedReader in =  new BufferedReader ( new InputStreamReader(conn.getInputStream ()));
        String line;
        while ((line = in.readLine  ()) != null ) {
            sb.append (line);
        }
        in.close ();
        return sb.toString ();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (onTaskFinishedObserverReference != null && onTaskFinishedObserverReference.get() != null)
            onTaskFinishedObserverReference.get().onTaskFinished(result);
    }
}
