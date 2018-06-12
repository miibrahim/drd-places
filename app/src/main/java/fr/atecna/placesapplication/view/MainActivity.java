package fr.atecna.placesapplication.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import fr.atecna.placesapplication.R;
import fr.atecna.placesapplication.async.FourSquarePlacesTask;
import fr.atecna.placesapplication.async.GooglePlacesTask;
import fr.atecna.placesapplication.async.OnTaskFinished;
import fr.atecna.placesapplication.model.Consts;
import fr.atecna.placesapplication.model.FourSquarePlace;
import fr.atecna.placesapplication.model.GooglePlace;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PlacesAdapter placesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        requestGooglePlaces();
        requestFourSquarePlaces();

        initRecyclerView();
    }

    private void initRecyclerView() {
        placesAdapter = new PlacesAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(placesAdapter);
    }

    private void requestFourSquarePlaces() {
        FourSquarePlacesTask task = new FourSquarePlacesTask();
        task.setOnTaskFinishedObserver(onTaskFinishedFoureSquare);
        task.execute(Consts.FOUR_SQUARE_URL);
    }

    private void requestGooglePlaces() {
        GooglePlacesTask task = new GooglePlacesTask();
        task.setOnTaskFinishedObserver(onTaskFinishedGoogle);
        task.execute(Consts.GOOGLE_URL);
    }

    OnTaskFinished<List<FourSquarePlace>> onTaskFinishedFoureSquare = new OnTaskFinished<List<FourSquarePlace>>() {
        @Override
        public void onTaskFinished(List<FourSquarePlace> fourSquarePlaces) {
            placesAdapter.addPlaces(fourSquarePlaces);
        }
    };

    OnTaskFinished<List<GooglePlace>> onTaskFinishedGoogle = new OnTaskFinished<List<GooglePlace>>() {
        @Override
        public void onTaskFinished(List<GooglePlace> googlePlaces) {
            placesAdapter.addPlaces(googlePlaces);
        }
    };
}
