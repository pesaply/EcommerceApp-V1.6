package com.example.Escuelas;

public class Universidades {
    public String _id;
    public String nombre_centro_trabajo;

    public Universidades(String _id, String nombre_centro_trabajo) {
        this._id = _id;
        this.nombre_centro_trabajo = nombre_centro_trabajo;
    }

    public String get_id() {
        return _id;
    }

    @Override
    public String toString() {
        return this.nombre_centro_trabajo;
    }
}
