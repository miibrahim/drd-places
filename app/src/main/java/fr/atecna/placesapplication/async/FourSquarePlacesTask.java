package fr.atecna.placesapplication.async;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.atecna.placesapplication.model.FourSquarePlace;

public class FourSquarePlacesTask extends BaseAsyncTask<Object, List<FourSquarePlace>> {

    @Override
    protected List<FourSquarePlace> parseResult(String jsonResult) throws JSONException {
        Log.d(getClass().getSimpleName(), "parseResult");
        JSONObject jsonObject = new JSONObject(jsonResult);
        JSONArray resultsArr = jsonObject.getJSONObject("response").getJSONArray("venues");
        List<FourSquarePlace> fourSquarePlaces = new ArrayList<>();

        for (int i = 0; i < resultsArr.length(); i++) {
            JSONObject placeJson = resultsArr.getJSONObject(i);
            fourSquarePlaces.add(new FourSquarePlace(placeJson.getString("id")
                    , placeJson.getString("name")
                    , placeJson.getJSONObject( "location" ).getDouble( "lat" )
                    , placeJson.getJSONObject( "location" ).getDouble( "lng" )));
        }
        return fourSquarePlaces;
    }
}
