package fr.atecna.placesapplication.view.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import fr.atecna.placesapplication.R;
import fr.atecna.placesapplication.model.Place;
import fr.atecna.placesapplication.view.PlacesAdapter;
import fr.atecna.placesapplication.view.main.MainActivity;

public class ListFragment extends Fragment implements PlacesPresenter.PresenterListener {
    RecyclerView recyclerView;
    PlacesAdapter placesAdapter;
    PlacesPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.frag_list, null );

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        } );
        return view;
    }

    public void swapFragment() {
        ((MainActivity)getActivity()).openMapsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        presenter = new PlacesPresenter(getContext().getApplicationContext());
        presenter.setPresenterListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecyclerView();

        presenter.initPlaces(((MainActivity)getActivity()).getPlaces());
    }

    private void initRecyclerView() {
        placesAdapter = new PlacesAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(placesAdapter);
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

    @Override
    public void onPlacesUpdated(List<Place> places) {
        placesAdapter.setPlaces(places);
    }
}
