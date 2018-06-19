package fr.atecna.placesapplication.view.main;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.atecna.placesapplication.async.FourSquarePlacesTask;
import fr.atecna.placesapplication.async.GooglePlacesTask;
import fr.atecna.placesapplication.async.OnTaskFinished;
import fr.atecna.placesapplication.interactor.LocationInteractor;
import fr.atecna.placesapplication.model.Consts;
import fr.atecna.placesapplication.model.FourSquarePlace;
import fr.atecna.placesapplication.model.GooglePlace;
import fr.atecna.placesapplication.model.Place;

public class MainPresenter implements LocationInteractor.OnLocationChangedListener {
    LocationInteractor locationInteractor;
    Context context;
    boolean isFirstLocation = true;
    List<FourSquarePlace> fourSquarePlaces = new ArrayList<>();
    List<GooglePlace> googlePlaces = new ArrayList<>();

    List<Place> places = new ArrayList<>();

    public MainPresenter(Context context) {
        this.context = context;
        locationInteractor = new LocationInteractor(context);
        locationInteractor.setOnLocationChanged(this);
    }

    public void requestLocationUpdates() {
        locationInteractor.requestLocationUpdates();
    }

    private void requestFourSquarePlaces(Location location) {
        FourSquarePlacesTask task = new FourSquarePlacesTask();
        task.setOnTaskFinishedObserver(onTaskFinishedFoureSquare);
        task.execute( Consts.generateFourSquareUrl( location ));
    }

    private void requestGooglePlaces(Location location) {
        GooglePlacesTask task = new GooglePlacesTask();
        task.setOnTaskFinishedObserver(onTaskFinishedGoogle);
        task.execute(Consts.generateGoogleUrl(location));
    }

    private void setAllPlaces(){
        places = new ArrayList<>( );
        if (fourSquarePlaces != null)
            places.addAll( fourSquarePlaces );
        if (googlePlaces != null)
            places.addAll( googlePlaces );
    }

    public List<Place> getPlaces() {
        return places;
    }

    OnTaskFinished<List<FourSquarePlace>> onTaskFinishedFoureSquare = new
            OnTaskFinished<List<FourSquarePlace>>() {
                @Override
                public void onTaskFinished(List<FourSquarePlace> fourSquarePlaces) {
                    MainPresenter.this.fourSquarePlaces = fourSquarePlaces;
                    setAllPlaces();
                    String jsonList = new Gson().toJson(fourSquarePlaces);

                    Intent intent = new Intent( MainActivity.ACTION_FOUR_SQUARE_PLACES_RECEIVED );
                    intent.putExtra(MainActivity.KEY_LIST, jsonList);

                    LocalBroadcastManager.getInstance(context)
                            .sendBroadcast(intent);
                }
            };

    OnTaskFinished<ArrayList<GooglePlace>> onTaskFinishedGoogle = new OnTaskFinished<ArrayList<GooglePlace>> () {
        @Override
        public void onTaskFinished(ArrayList<GooglePlace> googlePlaces) {
            MainPresenter.this.googlePlaces = googlePlaces;
            setAllPlaces();
            Intent intent = new Intent( MainActivity.ACTION_GOOGLE_PLACES_RECEIVED );
            intent.putExtra(MainActivity.KEY_LIST, googlePlaces);

            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(intent);
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        if (isFirstLocation){
            isFirstLocation = false;
            requestFourSquarePlaces(location);
            requestGooglePlaces(location);
            locationInteractor.stopListeningToLocation();
        }
    }
}
