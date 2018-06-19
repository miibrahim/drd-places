package fr.atecna.placesapplication.view;

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

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    List<Place> places;
    GoogleMap map;

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
        places = ((MainActivity)getActivity()).places;
    }

    public void onSaveInstanceState (Bundle state){
        super.onSaveInstanceState(state);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance( getContext() )
                .registerReceiver( receiver, new IntentFilter(MainActivity.ACTION_GOOGLE_PLACES_RECEIVED ) );
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( receiver
                , new IntentFilter(MainActivity.ACTION_FOUR_SQUARE_PLACES_RECEIVED ));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public void onStop () {
        super.onStop();
    }

    public void addAllMarkers() {
        map.clear();
        for (int i=0; i<places.size(); i++){
            Place place = places.get( i );
            map.addMarker( new MarkerOptions()
                    .position( new LatLng( place.getLat(), place.getLng() ) ).title( place.name ) );
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        addAllMarkers();
    }

    private void onReceivedBroadcast(Intent intent){
        this.places = ((MainActivity)getActivity()).places;

        if (MainActivity.ACTION_GOOGLE_PLACES_RECEIVED.equals( intent.getAction() )
                &&  ((MainActivity)getActivity()).getGooglePlaces() != null)
            addAllMarkers();
        else if (MainActivity.ACTION_FOUR_SQUARE_PLACES_RECEIVED.equals( intent.getAction() )
                &&  ((MainActivity)getActivity()).getFourSquarePlaces() != null)
            addAllMarkers();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onReceivedBroadcast(intent);
        }
    };
}
