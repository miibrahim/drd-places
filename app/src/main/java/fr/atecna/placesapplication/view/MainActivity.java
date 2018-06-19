package fr.atecna.placesapplication.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import fr.atecna.placesapplication.R;
import fr.atecna.placesapplication.async.FourSquarePlacesTask;
import fr.atecna.placesapplication.async.GooglePlacesTask;
import fr.atecna.placesapplication.async.OnTaskFinished;
import fr.atecna.placesapplication.model.Consts;
import fr.atecna.placesapplication.model.FourSquarePlace;
import fr.atecna.placesapplication.model.GooglePlace;
import fr.atecna.placesapplication.model.Place;

public class MainActivity extends AppCompatActivity implements LocationListener{
    public static final String ACTION_GOOGLE_PLACES_RECEIVED = "ACTION_GOOGLE_PLACES_RECEIVED";
    public static final String ACTION_FOUR_SQUARE_PLACES_RECEIVED = "ACTION_FOUR_SQUARE_PLACES_RECEIVED";
    List<FourSquarePlace> fourSquarePlaces = new ArrayList<>();
    List<GooglePlace> googlePlaces = new ArrayList<>();

    List<Place> places = new ArrayList<>();

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    Location myLocation;
    boolean isListeningToLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main );
        openListFragment();

        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE);

        if (checkLocationPermission())
            requestLocationUpdates();
    }

    private void openListFragment() {
        ListFragment fragment = new ListFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layoutFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public List<FourSquarePlace> getFourSquarePlaces() {
        return fourSquarePlaces;
    }

    public List<GooglePlace> getGooglePlaces() {
        return googlePlaces;
    }

    private void requestFourSquarePlaces(Location location) {
        FourSquarePlacesTask task = new FourSquarePlacesTask();
        task.setOnTaskFinishedObserver(onTaskFinishedFoureSquare);
        task.execute( Consts.generateFourSquareUrl( location ));
    }

    private void setAllPlaces(){
        places = new ArrayList<>( );
        if (fourSquarePlaces != null)
            places.addAll( fourSquarePlaces );
        if (googlePlaces != null)
            places.addAll( googlePlaces );
    }

    private void requestGooglePlaces(Location location) {
        GooglePlacesTask task = new GooglePlacesTask();
        task.setOnTaskFinishedObserver(onTaskFinishedGoogle);
        task.execute(Consts.generateGoogleUrl(location));
    }

    OnTaskFinished<List<FourSquarePlace>> onTaskFinishedFoureSquare = new
            OnTaskFinished<List<FourSquarePlace>>() {
        @Override
        public void onTaskFinished(List<FourSquarePlace> fourSquarePlaces) {
            MainActivity.this.fourSquarePlaces = fourSquarePlaces;
            setAllPlaces();
            LocalBroadcastManager.getInstance( MainActivity.this )
                    .sendBroadcast( new Intent( ACTION_FOUR_SQUARE_PLACES_RECEIVED ) );
        }
    };

    OnTaskFinished<List<GooglePlace>> onTaskFinishedGoogle = new OnTaskFinished<List<GooglePlace>> () {
        @Override
        public void onTaskFinished(List<GooglePlace> googlePlaces) {
            MainActivity.this.googlePlaces = googlePlaces;
            setAllPlaces();
            LocalBroadcastManager.getInstance( MainActivity.this )
                    .sendBroadcast( new Intent( ACTION_GOOGLE_PLACES_RECEIVED ) );
        }
    };

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver()
                    , Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        requestLocationUpdates();
                    }
                } else {
                }
                return;
            }

        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        if(isLocationEnabled(this)){


            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi( LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            startLocationUpdates();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.d( "onConnectionSuspended", "onConnectionSuspended: " + i );
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d( "ConnectionFailed", "onConnectionFailed" );
                        }
                    })
                    .build();

            startListeningToLocation( 400 );
        }else{
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (isListeningToLocation)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @SuppressWarnings("MissingPermission")
    private void startListeningToLocation(int milliseconds) {
//        if (mGoogleApiClient != null && (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) && !forceToListen) {
//            return;
//        }
        mLocationRequest = LocationRequest.create();
        if (isLocationEnabled(this)) {
            stopListeningToLocation();
            // Use high accuracy
            if(isLocationEnabled(this)) {
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            } else if(isLocationEnabled(this)){
                mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
            }
            // Set the update interval to 5 seconds
            mLocationRequest.setInterval(3000);
            // Set the fastest update interval to 1 second
            mLocationRequest.setFastestInterval(3000);

            mGoogleApiClient.connect();
            isListeningToLocation = true;

        } else {
        }
    }

    /*
     * Stop listening to location
     */
    public void stopListeningToLocation() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this );
            mGoogleApiClient.disconnect();
        }
        isListeningToLocation = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (myLocation == null){
            myLocation = location;
            //TODO:: request places
            requestGooglePlaces(location);
            requestFourSquarePlaces(location);
        }
    }

    public void openMapsFragment() {
        MapsFragment newMapsFragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layoutFragmentContainer, newMapsFragment );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
