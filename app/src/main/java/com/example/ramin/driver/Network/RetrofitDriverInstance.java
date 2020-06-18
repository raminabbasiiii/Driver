package com.example.ramin.driver.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitDriverInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://ramin-abbasi.ir/taxiApi/driver/";

    private RetrofitDriverInstance() {
    }

    public static Retrofit getRetrofitDriver() {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
