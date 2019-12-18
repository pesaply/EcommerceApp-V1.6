package com.app.templateasdemo;

public class Escuela {

    private String pk_id_escuela;
    private String status;
    private String nombre_centro_trabajo;

    public Escuela(String pk_id_escuela, String status, String nombre_centro_trabajo) {
        this.pk_id_escuela = pk_id_escuela;
        this.status = status;
        this.nombre_centro_trabajo = nombre_centro_trabajo;
    }

    public String getPk_id_escuela(){
        return pk_id_escuela;
    }

    public String getStatus() {
        return status;
    }

    public String getNombre_centro_trabajo() {
        return nombre_centro_trabajo;
    }
}
