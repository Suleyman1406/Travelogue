package com.okada.travelogue.HelperClasses;

public class CityClass {
    private String imageUrl;
    private float rate;
    private String name, description;

    public CityClass(String imageUrl, float rate, String name, String description) {
        this.imageUrl = imageUrl;
        this.rate = rate;
        this.name = name;
        this.description = description;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
