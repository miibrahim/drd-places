package fr.atecna.placesapplication.model;

public class GooglePlace {
    private String id;
    private String name;

    public GooglePlace(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
