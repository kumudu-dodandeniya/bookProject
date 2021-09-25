package com.example.bookproject;

public class MainModel {

    String name,description,price,burl;

    MainModel(){

    }

    public MainModel(String name, String description, String price, String burl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.burl = burl;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBurl() {
        return burl;
    }

    public void setBurl(String burl) {
        this.burl = burl;
    }
}
