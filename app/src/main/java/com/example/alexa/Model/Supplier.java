package com.example.alexa.Model;

import java.util.ArrayList;

public class Supplier {
    String supplierId;
    String supplierName;
    String supplierContact;
    String supplierEmail;
    ArrayList<String> supplierCategories;


    public Supplier() {
    }

    public Supplier(String supplierId, String supplierName, String supplierContact, String supplierEmail, ArrayList<String> supplierCategories) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.supplierEmail = supplierEmail;
        this.supplierCategories = supplierCategories;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public ArrayList<String> getSupplierCategories() {
        return supplierCategories;
    }

    public void setSupplierCategories(ArrayList<String> supplierCategories) {
        this.supplierCategories = supplierCategories;
    }
}
