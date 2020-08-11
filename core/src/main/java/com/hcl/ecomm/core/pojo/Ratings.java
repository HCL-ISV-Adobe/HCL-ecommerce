package com.hcl.ecomm.core.pojo;

public class Ratings {


    private String name;
     private float rating;
    private String sku;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
                '}';
    }


}
