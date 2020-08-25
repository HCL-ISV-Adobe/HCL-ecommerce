package com.hcl.ecomm.core.pojo;

public class Ratings {


    private String name;
    private float rating;
    private String sku;
    private String title;
    private String description;
    private String customer;
    //private float avgRating;


   /* public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }*/

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }



    @Override
    public String toString() {
        return "Ratings{" +
                "name='" + name + '\'' +
                ", rating=" + rating +
                ", sku='" + sku + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", customer='" + customer + '\'' +
                '}';
    }



}
