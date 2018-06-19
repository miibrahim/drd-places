package fr.atecna.placesapplication.interactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationInteractor implements LocationListener{
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    Location myLocation;
    boolean isListeningToLocation = false;
    Context context;
    OnLocationChangedListener onLocationChanged;

    public LocationInteractor(Context context) {
        this.context = context;
        initLocationListening();
    }

    public void initLocationListening(){
        locationManager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates() {
        if(isLocationEnabled(context)){
            mGoogleApiClient = new GoogleApiClient.Builder(context)
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
        if (isLocationEnabled(context)) {
            stopListeningToLocation();
            // Use high accuracy
            if(isLocationEnabled(context)) {
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            } else if(isLocationEnabled(context)){
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

    public void setOnLocationChanged(OnLocationChangedListener onLocationChanged) {
        this.onLocationChanged = onLocationChanged;
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        if (onLocationChanged != null)
            onLocationChanged.onLocationChanged(location);
    }

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

    public interface OnLocationChangedListener {
        void onLocationChanged(Location location);
    }
}
