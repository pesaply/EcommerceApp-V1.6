package com.app.templateasdemo.Retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface INodeJSchools {

        @GET("consultarUniversidadesActivas")
        Call<JsonObject> obtenerUniversidadesActivas();

}
