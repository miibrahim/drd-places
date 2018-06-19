package fr.atecna.placesapplication.view.main.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import fr.atecna.placesapplication.R;
import fr.atecna.placesapplication.model.Place;
import fr.atecna.placesapplication.view.main.MainActivity;

public class MapsFragment extends Fragment implements OnMapReadyCallback, PlacesPresenter.PresenterListener {
    GoogleMap map;
    PlacesPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
                                    ,@Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_maps, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById( R.id.mapsFragment );
        mapFragment.getMapAsync( this );
        presenter = new PlacesPresenter(getContext().getApplicationContext());
        presenter.setPresenterListener(this);
        presenter.initPlaces(((MainActivity)getActivity()).getPlaces());
    }

    public void onSaveInstanceState (Bundle state){
        super.onSaveInstanceState(state);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.registerReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unregisterReceiver();
    }

    public void onStop () {
        super.onStop();
    }

    public void addAllMarkers() {
        if(map == null || presenter.getPlaces() == null)
            return;
        map.clear();
        for (int i=0; i<presenter.getPlaces().size(); i++){
            Place place = presenter.getPlaces().get( i );
            map.addMarker( new MarkerOptions()
                    .position( new LatLng( place.getLat(), place.getLng() ) ).title( place.name ) );
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        addAllMarkers();
    }

    @Override
    public void onPlacesUpdated(List<Place> places) {
        addAllMarkers();
    }
}
