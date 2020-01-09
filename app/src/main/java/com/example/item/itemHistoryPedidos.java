package com.example.item;

public class itemHistoryPedidos {

    private int imgfoto;
    private String titulo;
    private String contenido;

    public itemHistoryPedidos (int imgfoto, String titulo, String contenido){

        this.imgfoto = imgfoto;
        this.titulo = titulo;
        this.contenido = contenido;

    }
    public int getImg() { return imgfoto; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
}
