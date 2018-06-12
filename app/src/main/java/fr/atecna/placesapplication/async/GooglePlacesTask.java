package fr.atecna.placesapplication.async;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atecna.placesapplication.model.GooglePlace;

public class GooglePlacesTask extends BaseAsyncTask<Object, List<GooglePlace>> {
    @Override
    protected List<GooglePlace> doInBackground(String... urls) {
        try {
            String jsonResult = getApi(urls[0]);
            Log.d(getClass().getSimpleName(), "result: " + jsonResult);
            List<GooglePlace> result = parseResult(jsonResult);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<GooglePlace> parseResult(String jsonResult) throws JSONException {
        Log.d(getClass().getSimpleName(), "parseResult");
        JSONObject jsonObject = new JSONObject(jsonResult);
        JSONArray resultsArr = jsonObject.getJSONArray("results");
        List<GooglePlace> googlePlaces = new ArrayList<>();

        for (int i = 0; i < resultsArr.length(); i++) {
            JSONObject placeJson = resultsArr.getJSONObject(i);
            googlePlaces.add(new GooglePlace(placeJson.getString("id")
                    , placeJson.getString("name")));
        }
        return googlePlaces;
    }
}
