package fr.atecna.placesapplication.model;

import java.io.Serializable;

public class FourSquarePlace extends Place implements Serializable{

    public FourSquarePlace(String id, String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

}
