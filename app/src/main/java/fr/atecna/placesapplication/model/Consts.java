package fr.atecna.placesapplication.model;

import android.location.Location;

public abstract class Consts {

    public static String generateGoogleUrl(Location location){
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + location.getLatitude()+","+location.getLongitude()
                + "&radius=250&type=restaurant&key=AIzaSyC4PG-njVsWx8eSMBGb1j6wiGuzPZhhP9c";
    }

    public static String generateFourSquareUrl(Location location){
        return "https://api.foursquare.com/v2/venues/search?ll=" + location.getLatitude() + ","
                + location.getLongitude()
                + "&radius=250&client_id=BIHDYHSOXJF43D35IC11HUQBAGDN0KVMAG3KUCBTGORM1144&client_secret=02HLFAN5G3RILDCRK0HNYZPGSI5LHU25D5M1CW0KNMDHR5UX&v=20120609&category=food" ;
    }
}
