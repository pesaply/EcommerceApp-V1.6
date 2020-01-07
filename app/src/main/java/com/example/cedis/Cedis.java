package com.example.cedis;

public class Cedis {
    public String _id;
    public String cedis;

    public Cedis(String _id, String cedis) {
        this._id = _id;
        this.cedis = cedis;
    }

    public String get_id() {
        return _id;
    }

    @Override
    public String toString() {
        return this.cedis;
    }
}


