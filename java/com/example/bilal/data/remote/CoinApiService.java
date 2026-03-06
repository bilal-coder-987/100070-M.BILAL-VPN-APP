package com.example.bilal.data.remote;

import com.example.bilal.data.remote.model.CoinResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinApiService {
    @GET("api/v3/coins/markets")
    Call<List<CoinResponse>> getMarkets(
            @Query("vs_currency") String vsCurrency,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("page") int page,
            @Query("sparkline") boolean sparkline
    );
}