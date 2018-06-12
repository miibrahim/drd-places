package fr.atecna.placesapplication.async;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public abstract class BaseAsyncTask<Progress, Result> extends AsyncTask<String, Progress, Result> {
    OnTaskFinished<Result> onTaskFinishedObserver;

    public void setOnTaskFinishedObserver(OnTaskFinished<Result> onTaskFinishedObserver) {
        this.onTaskFinishedObserver = onTaskFinishedObserver;
    }

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
        if (onTaskFinishedObserver != null)
            onTaskFinishedObserver.onTaskFinished(result);
    }
}
