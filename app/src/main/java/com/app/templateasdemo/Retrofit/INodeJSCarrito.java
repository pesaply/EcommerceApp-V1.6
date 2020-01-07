package com.app.templateasdemo.Retrofit;

import com.example.itemCarrito.ItemCarrito;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface INodeJSCarrito {
    @POST("insertarCarrito/{id_usuario}")
    Call<ItemCarrito> insertarCarrito (@Path("id_usuario") String id_usuario ,
                                        @Body ItemCarrito productoCarrito);


    @GET("consultarCarrito/{id_usuario}")
    Call<JsonObject> consultarCarrito (@Path("id_usuario") String id_usuario);

    @PUT("agregarCarrito/{id_usuario}")
    Call<ItemCarrito>  agregarCarrito (@Path("id_usuario") String id_usuario ,
                                       @Body ItemCarrito productoCarrito);

    @PUT("quitarCarrito/{id_usuario}")
    Call<ItemCarrito>  quitarCarrito (@Path("id_usuario") String id_usuario ,
                                      @Body ItemCarrito productoCarrito);


}
