package com.example.itemCarrito;

public class ItemCarrito {
    private String _id;
    private String cedis;
    private int piezas;


    public ItemCarrito(final String _id, final String cedis, final int piezas) {

        this._id = _id;
        this.cedis = cedis;
        this.piezas = piezas;


    }

    @Override
    public String toString() { return "_id: " + _id + ", cedis: " + cedis + ", piezas: " + piezas; }

}
