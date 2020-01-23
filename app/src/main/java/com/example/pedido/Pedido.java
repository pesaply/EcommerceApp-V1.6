package com.example.pedido;

public class Pedido {
    private String folio;
    private String estatus;
    private String usuario;
    private String cedis;
    private ItemPedido[] items;
    private Float subtotal;
    private Float descuento;
    private Float total;

    public Pedido(String folio, String estatus, String usuario, String cedis, ItemPedido[] items, Float subtotal, Float descuento, Float total) {
        this.folio = folio;
        this.estatus = estatus;
        this.usuario = usuario;
        this.cedis = cedis;
        this.items = items;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
    }

    @Override
    public String toString() { return "folio: " + folio + ", estatus: " + estatus + ", usuario: " + usuario + ", cedis: " + cedis + ", items: " + items + ", subtotal: " + subtotal + ", descuento: " + descuento + ", total: " + total; }

}