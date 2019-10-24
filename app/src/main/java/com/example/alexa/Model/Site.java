package com.example.alexa.Model;

public class Site {

    String name;
    String location;
    String contact;
    String id;

    public Site() {
    }

    public Site(String name, String location, String contact, String id) {
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
