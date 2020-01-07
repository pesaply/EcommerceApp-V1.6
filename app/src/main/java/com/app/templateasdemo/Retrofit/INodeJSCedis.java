package com.app.templateasdemo.Retrofit;

import com.example.itemCarrito.ItemCarrito;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface INodeJSCedis {

        @GET("obtenerTodosCedis")
        Call<JsonObject> obtenerTodosCedis();

}
