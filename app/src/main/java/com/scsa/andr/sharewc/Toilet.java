package com.scsa.andr.sharewc;

import androidx.annotation.NonNull;

public class Toilet {
    private String name;
    private String location;
    private int price;
    private boolean availability;

    private String address;

    private String id;

    private String key;

    public Toilet(){

    }

    public Toilet(String name, double latitude, double longitude, String address, int price, String id) {
        // Default constructor required for Firebase
        this.name = name;
        this.price = price;
        this.address = address;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @NonNull
    @Override
    public String toString() {
        return address;
    }
}


