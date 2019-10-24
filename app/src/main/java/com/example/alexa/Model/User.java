package com.example.alexa.Model;

public class User {

    String name;
    String email;
    String address;
    String phoneNo;
    String role;
    String site;
    String siteName;
    String id;
    String image;

    public User() {
    }

    public User(String name, String email, String address, String phoneNo, String role, String site, String siteName) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNo = phoneNo;
        this.role = role;
        this.site = site;
        this.siteName = siteName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
