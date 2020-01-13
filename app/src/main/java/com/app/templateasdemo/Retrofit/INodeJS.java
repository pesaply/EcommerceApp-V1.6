package com.app.templateasdemo.Retrofit;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface INodeJS {
    @POST("registerApp")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("nombre") String nombre,
                                    @Field("apellidos") String apellidos,
                                    @Field("correo") String correo,
                                    @Field("fecha_nacimiento") String fecha_nacimiento,
                                    @Field("telefono") String telefono,
                                    @Field("escuela") String escuela,
                                    @Field("sucursal") String sucursal,
                                    @Field("password") String password,
                                    @Field("confirm_password") String confirm_password);

    @POST ("loginApp")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("correo") String correo,
                                 @Field("password") String password);

    @GET("consultarUsuarioCorreo/{correo}")
    Call<JsonObject> consultarUsuarioCorreo (@Path("correo") String correo);

    @GET("consultarUsuarioId/{id_usuario}")
    Call<JsonObject> consultarUsuarioId (@Path("id_usuario") String id_usuario);

}