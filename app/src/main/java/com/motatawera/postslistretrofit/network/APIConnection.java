package com.motatawera.postslistretrofit.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class APIConnection {
    private static final String SERVER_BASE_URL = "https://jsonplaceholder.typicode.com/";

    public static Retrofit getRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }




}
