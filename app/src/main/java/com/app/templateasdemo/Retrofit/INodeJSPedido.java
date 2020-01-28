package com.app.templateasdemo.Retrofit;

import com.example.pedido.Pedido;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface INodeJSPedido {
    @POST("insertarPedido")
    Call<JsonObject> insertarPedido(@Body Pedido Pedido);


    @GET("consultarPedido/{id_usuario}")
    Call<JsonObject> consultarPedido (@Path("id_usuario") String id_usuario);

    @GET("consultarPedidoProceso/{id_usuario}")
    Call<JsonObject> consultarPedidoProceso (@Path("id_usuario") String id_usuario);

    @GET("consultarPedidoHistorial/{id_usuario}")
    Call<JsonObject> consultarPedidoHistorial (@Path("id_usuario") String id_usuario);
}

