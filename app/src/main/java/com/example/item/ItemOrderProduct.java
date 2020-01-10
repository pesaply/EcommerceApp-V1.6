package com.example.item;

public class ItemOrderProduct {

    private String OrderId;
    private String OrderName;
    private String OrderImage;
    private String OrderSeller;
    private Float OrderPrice;
    private int OrderOfferPercentage;
    private Float OrderDiscountPrice;
    private String OrderDate;
    private int OrderQuantity;

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    private String idproducto;

    public String getOrderName() {
        return OrderName;
    }
    public void setOrderName(String OrderName) {
        this.OrderName = OrderName;
    }

    public String getOrderSeller() {
        return OrderSeller;
    }
    public void setOrderSeller(String OrderSeller) {
        this.OrderSeller = OrderSeller;
    }

    public String getOrderImage() {return OrderImage;}
    public void setOrderImage(String OrderImage) {
        this.OrderImage = OrderImage;
    }

    public Float getOrderPrice() {return OrderPrice;}
    public void setOrderPrice(Float OrderPrice) {this.OrderPrice = OrderPrice;}

    public int getOrderOfferPercentage() {return OrderOfferPercentage;}
    public void setOrderOfferPercentage(int OrderOfferPercentage) {
        this.OrderOfferPercentage = OrderOfferPercentage;
    }

    public Float getOrderDiscountPrice() {return OrderDiscountPrice;}
    public void setOrderDiscountPrice(Float OrderDiscountPrice) {
        this.OrderDiscountPrice = OrderDiscountPrice;
    }

    public String getOrderDate() {return OrderDate;}
    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    public int getOrderQuantity() {return OrderQuantity;}
    public void setOrderQuantity(int OrderQuantity) {
        this.OrderQuantity = OrderQuantity;
    }


}