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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.atecna.placesapplication.R;

public class ListFragment extends Fragment {
    RecyclerView recyclerView;
    PlacesAdapter placesAdapter;

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

        recyclerView = view.findViewById(R.id.recyclerView);
        initRecyclerView();

        placesAdapter.addPlaces(((MainActivity)getActivity()).places);
    }

    private void initRecyclerView() {
        placesAdapter = new PlacesAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(placesAdapter);
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
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver( receiver );
    }

    private void onReceivedBroadcast(Intent intent){
        if (MainActivity.ACTION_GOOGLE_PLACES_RECEIVED.equals( intent.getAction() )
                &&  ((MainActivity)getActivity()).getGooglePlaces() != null)
            placesAdapter.addPlaces( ((MainActivity)getActivity()).getGooglePlaces() );
        else if (MainActivity.ACTION_FOUR_SQUARE_PLACES_RECEIVED.equals( intent.getAction() )
                &&  ((MainActivity)getActivity()).getFourSquarePlaces() != null)
            placesAdapter.addPlaces( ((MainActivity)getActivity()).getFourSquarePlaces() );
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onReceivedBroadcast(intent);
        }
    };

}
