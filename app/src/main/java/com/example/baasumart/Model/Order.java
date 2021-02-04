package com.example.baasumart.Model;

public class Order {
    private String prodId, orderId, userId, date;
    private boolean isPlaced;
    public Order() {}

    public Order(String prodId, String orderId, String userId, String date, boolean isPlaced) {
        this.prodId = prodId;
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.isPlaced = isPlaced;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }
}
