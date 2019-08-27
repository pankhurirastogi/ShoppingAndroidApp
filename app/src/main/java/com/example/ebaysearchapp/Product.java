package com.example.ebaysearchapp;

public class Product {
    String imageURL;
    String title;
    String itemid;
    String shipping;
    Double price;
    String zip;
    String sellerName;
    String sellerInfo;
    String shippingInfo;
    String condition;


    public Product(String imageURL, String title, String itemid, String shipping, Double price, String zip, String sellerName, String sellerInfo, String shippingInfo,String condition) {
        this.imageURL = imageURL;
        this.title = title;
        this.itemid = itemid;
        this.shipping = shipping;
        this.price = price;
        this.zip = zip;
        this.sellerName = sellerName;
        this.sellerInfo = sellerInfo;
        this.shippingInfo = shippingInfo;
        this.condition = condition;
    }
}
