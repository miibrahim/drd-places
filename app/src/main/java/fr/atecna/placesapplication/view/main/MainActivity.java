package fr.atecna.placesapplication.view.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import fr.atecna.placesapplication.R;
import fr.atecna.placesapplication.model.Place;
import fr.atecna.placesapplication.view.main.fragments.ListFragment;
import fr.atecna.placesapplication.view.main.fragments.MapsFragment;

public class MainActivity extends AppCompatActivity{
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static final String ACTION_GOOGLE_PLACES_RECEIVED = "ACTION_GOOGLE_PLACES_RECEIVED";
    public static final String ACTION_FOUR_SQUARE_PLACES_RECEIVED = "ACTION_FOUR_SQUARE_PLACES_RECEIVED";
    public static final String KEY_LIST = "KEY_LIST";

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main );
        presenter = new MainPresenter(getApplicationContext());
        openListFragment();

        if (checkLocationPermission())
            presenter.requestLocationUpdates();
    }

    private void openListFragment() {
        ListFragment fragment = new ListFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layoutFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

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
                        presenter.requestLocationUpdates();
                    }
                } else {
                }
                return;
            }

        }
    }

    public void openMapsFragment() {
        MapsFragment newMapsFragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layoutFragmentContainer, newMapsFragment );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public List<Place> getPlaces(){
        return presenter.getPlaces();
    }
}
