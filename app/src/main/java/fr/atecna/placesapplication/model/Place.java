package fr.atecna.placesapplication.model;

import java.io.Serializable;

public class Place implements Serializable {
    public   String id;
    public double lat;
    public String name;
    public double lng;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() { return lat;  }

    public double getLng() { return lng; }
}
