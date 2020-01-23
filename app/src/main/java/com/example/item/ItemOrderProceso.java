package com.example.item;

public class ItemOrderProceso {

     private String OrderProcessTitle;
    private String OrderProcessDate;
    private String OrderProcessNo;
    private String OrderProcessPrice;
    private String OrderProcessStatus;

    public String getOrderProcessTitle() {
        return OrderProcessTitle;
    }
    public void setOrderProcessTitle(String OrderProcessTitle) {
        this.OrderProcessTitle = OrderProcessTitle;
    }

    public String getOrderProcessDate() {
        return OrderProcessDate;
    }
    public void setOrderProcessDate(String OrderProcessDate) {
        this.OrderProcessDate = OrderProcessDate;
    }

    public String getOrderProcessNo() {return OrderProcessNo;}
    public void setOrderProcessNo(String OrderProcessNo) {
        this.OrderProcessNo = OrderProcessNo;
    }

    public String getOrderProcessPrice() {return OrderProcessPrice;}
    public void setOrderProcessPrice(String OrderProcessPrice) {this.OrderProcessPrice = OrderProcessPrice;}

    public String getOrderProcessStatus() {return OrderProcessStatus;}
    public void setOrderProcessStatus(String OrderProcessStatus) {
        this.OrderProcessStatus = OrderProcessStatus;
    }

}
