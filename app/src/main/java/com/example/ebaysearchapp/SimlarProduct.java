package com.example.ebaysearchapp;

public class SimlarProduct {
    String imageURL;
    String title;
    Double shipping;
    int daysLeft;
    Double price;
    String viewItmURL;

    public SimlarProduct(String imageURL, String title, Double shipping, int daysLeft, Double price, String viewItmURL) {
        this.imageURL = imageURL;
        this.title = title;
        this.shipping = shipping;
        this.daysLeft = daysLeft;
        this.price = price;
        this.viewItmURL = viewItmURL;
    }
}
