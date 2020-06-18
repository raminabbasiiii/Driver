package com.example.ramin.driver.Network;

import com.example.ramin.driver.Model.DirectionModel.NeshanDirection;
import com.example.ramin.driver.Model.SearchMap.NeshanSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface GetDataService {

        @Headers("Api-Key: service.eS1buDPxSE0AbOh7d1bFPovw82KFpe1D72PBGE9y")
        @GET
        Call<NeshanSearch> getNeshanSearch(@Url String url);

        @Headers("Api-Key: service.3tdRoCbfGcTKYqgjBvkbxF8dgCBw2jg5MuXb68qM")
        @GET
        Call<NeshanDirection> getNeshanDirection(@Url String url);
}
