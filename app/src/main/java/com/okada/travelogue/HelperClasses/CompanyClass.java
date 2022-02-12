package com.okada.travelogue.HelperClasses;

public class CompanyClass {
    private String imageUrl;
    private String name, description;

    public CompanyClass(String imageUrl,String name, String description) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
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