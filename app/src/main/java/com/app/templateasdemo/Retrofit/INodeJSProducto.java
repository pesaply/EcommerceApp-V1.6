package com.app.templateasdemo.Retrofit;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface INodeJSProducto {

    @GET("obtenerProducto/{id_producto}")
    Call<JsonObject> obtenerProducto (@Path("id_producto") String id_producto);
}