package com.sscr.bcash;

public class ModelItems {
    String ItemName;
    String ItemQuantity;
    String ItemPrice;

    public ModelItems(String itemName, String itemQuantity, String itemPrice) {
        ItemName = itemName;
        ItemQuantity = itemQuantity;
        ItemPrice = itemPrice;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemQuantity() {
        return ItemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        ItemQuantity = itemQuantity;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }
}
