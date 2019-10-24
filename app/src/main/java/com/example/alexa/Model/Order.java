package com.example.alexa.Model;

import java.util.ArrayList;

public class Order {

    String OrderReference;
    String OrderStatus;
    ArrayList<SelectedItems> ItemList;
    String CompanyCode;
    String SupplierCode;
    String SupplierName;
    String DeliveryAddress;
    String DeliveryDate;
    double EstimatedValue;
    String managerId;


    public Order() {
    }

    public Order(String orderReference, String orderStatus, ArrayList<SelectedItems> itemList, String companyCode, String supplierCode, String supplierName, String deliveryAddress, String deliveryDate, Double edstimatedValue) {
        OrderReference = orderReference;
        OrderStatus = orderStatus;
        ItemList = itemList;
        CompanyCode = companyCode;
        SupplierCode = supplierCode;
        SupplierName = supplierName;
        DeliveryAddress = deliveryAddress;
        DeliveryDate = deliveryDate;
        EstimatedValue = edstimatedValue;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getOrderReference() {
        return OrderReference;
    }

    public void setOrderReference(String orderReference) {
        OrderReference = orderReference;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String  orderStatus) {
        OrderStatus = orderStatus;
    }

    public ArrayList<SelectedItems> getItemList() {
        return ItemList;
    }

    public void setItemList(ArrayList<SelectedItems> itemList) {
        ItemList = itemList;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }



    public String getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        SupplierCode = supplierCode;
    }



    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public double getEstimatedValue() {
        return EstimatedValue;
    }

    public void setEstimatedValue(double estimatedValue) {
        EstimatedValue = estimatedValue;
    }
}
