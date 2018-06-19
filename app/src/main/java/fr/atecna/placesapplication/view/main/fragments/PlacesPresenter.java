package fr.atecna.placesapplication.view.main.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import fr.atecna.placesapplication.model.FourSquarePlace;
import fr.atecna.placesapplication.model.Place;
import fr.atecna.placesapplication.view.main.MainActivity;

public class PlacesPresenter {
    Context context;
    List<Place> places = new ArrayList<>();
    WeakReference<PresenterListener> presenterListener;

    public PlacesPresenter(Context context) {
        this.context = context;
    }

    public void initPlaces(List<Place> places){
        if (places != null)
            updatePlaces(places);
    }

    void setPresenterListener(PresenterListener presenterListener) {
        this.presenterListener = new WeakReference<>(presenterListener);
    }

    private void onReceivedBroadcast(Intent intent){
        if (MainActivity.ACTION_GOOGLE_PLACES_RECEIVED.equals( intent.getAction() )){
            ArrayList<Place> newPlaces = (ArrayList<Place>) intent.getSerializableExtra(MainActivity.KEY_LIST);
            updatePlaces(newPlaces);
        } else if (MainActivity.ACTION_FOUR_SQUARE_PLACES_RECEIVED.equals( intent.getAction())){
            String placesJsonString = intent.getStringExtra(MainActivity.KEY_LIST);
            List<FourSquarePlace> newPlaces = parseList(new Gson(), placesJsonString, FourSquarePlace.class);
            updatePlaces(newPlaces);
        }
    }

    private void updatePlaces(List<? extends Place> newPlaces) {
        if (newPlaces == null)return;
        places.addAll(newPlaces);
        if (presenterListener.get() != null)
            presenterListener.get().onPlacesUpdated(places);
    }

    public List<Place> getPlaces() {
        return places;
    }

    void registerReceiver(){
        LocalBroadcastManager.getInstance( context )
                .registerReceiver( receiver, new IntentFilter(MainActivity.ACTION_GOOGLE_PLACES_RECEIVED ) );
        LocalBroadcastManager.getInstance( context ).registerReceiver( receiver
                , new IntentFilter(MainActivity.ACTION_FOUR_SQUARE_PLACES_RECEIVED ));
    }

    void unregisterReceiver(){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public static<T> List<T> parseList(Gson gson, String json, Class<T> tClass){

        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();

        if (jsonArray == null) return null;

        List<T> objectsList = new ArrayList<>(jsonArray.size());

        for (int i =0; i < jsonArray.size(); i++) {
            T t = gson.fromJson(jsonArray.get(i), tClass);
            objectsList.add(t);
        }

        return objectsList;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onReceivedBroadcast(intent);
        }
    };

    interface PresenterListener{
        void onPlacesUpdated(List<Place> places);
    }
}
