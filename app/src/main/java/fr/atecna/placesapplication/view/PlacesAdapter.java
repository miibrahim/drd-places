package fr.atecna.placesapplication.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.atecna.placesapplication.R;
import fr.atecna.placesapplication.model.Place;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    public List<Place> places = new ArrayList<>();

    public static class PlaceViewHolder extends RecyclerView.ViewHolder{
//        TextView textViewId;
        TextView textViewName;
        public PlaceViewHolder(View itemView) {
            super(itemView);
//            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, null);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
//        holder.textViewId.setText(places.get(position).getId());
        holder.textViewName.setText(places.get(position).getName());
    }

    public void addPlaces(List<? extends Place> places){
        this.places.addAll(places);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}