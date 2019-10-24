package com.example.alexa.Model;

public class SelectedItems {

    String ItemId;
    String ItemName;
    Double quantity;
    Double total;

    public SelectedItems() {
    }

    public SelectedItems(String itemId, String itemName, Double quantity, Double total) {
        ItemId = itemId;
        ItemName = itemName;
        this.quantity = quantity;
        this.total = total;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
