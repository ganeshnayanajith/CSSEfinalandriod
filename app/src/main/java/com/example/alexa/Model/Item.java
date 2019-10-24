package com.example.alexa.Model;

public class Item {

    String itemCode;
    String ItemName;
    boolean checked;
    Double quantity;
    String itemUnit;
    String itemUnitPrice;
    int index;


    public Item() {

    }

    public Item(String itemCode, String itemName, boolean checked, Double quantity, String itemUnit, String  itemUnitPrice, int index) {
        this.itemCode = itemCode;
        ItemName = itemName;
        this.checked = checked;
        this.quantity = quantity;
        this.itemUnit = itemUnit;
        this.itemUnitPrice = itemUnitPrice;
        this.index = index;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String  getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(String itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
