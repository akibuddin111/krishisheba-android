package com.krishisheba.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "https://api.open-meteo.com/v1/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {

        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}