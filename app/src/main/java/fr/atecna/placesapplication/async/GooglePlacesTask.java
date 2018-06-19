package fr.atecna.placesapplication.async;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.atecna.placesapplication.model.GooglePlace;

public class GooglePlacesTask extends BaseAsyncTask<Object, ArrayList<GooglePlace>> {

    @Override
    protected ArrayList<GooglePlace> parseResult(String jsonResult) throws JSONException {
        Log.d(getClass().getSimpleName(), "parseResult");
        JSONObject jsonObject = new JSONObject(jsonResult);
        JSONArray resultsArr = jsonObject.getJSONArray("results");
        ArrayList<GooglePlace> googlePlaces = new ArrayList<>();

        for (int i = 0; i < resultsArr.length(); i++) {
            JSONObject placeJson = resultsArr.getJSONObject(i);
            googlePlaces.add(new GooglePlace(placeJson.getString("id")
                    , placeJson.getString("name")
                    , placeJson.getJSONObject("location").getDouble("lat")
                    , placeJson.getJSONObject("location").getDouble( "lng" )));
        }
        return googlePlaces;
    }
}
