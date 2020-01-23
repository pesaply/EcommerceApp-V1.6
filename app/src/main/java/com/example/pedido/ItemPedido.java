
package com.example.pedido;

public class ItemPedido {
    private String cveproducto;
    private Integer piezas;
    private Float precio_inicial;
    private Float precio_final;

    public ItemPedido(String cveproducto, Integer piezas, Float precio_inicial, Float precio_final) {
        this.cveproducto = cveproducto;
        this.piezas = piezas;
        this.precio_inicial = precio_inicial;
        this.precio_final = precio_final;
    }

    @Override
    public String toString() { return "cveproducto: " + cveproducto + ", " + "piezas: " + piezas + ", precio_inicial: " + precio_inicial + ", precio_final: " + precio_final; }

}