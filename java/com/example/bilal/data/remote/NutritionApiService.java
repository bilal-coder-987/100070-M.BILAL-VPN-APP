package com.example.bilal.data.remote;

import com.example.bilal.data.remote.model.FoodResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NutritionApiService {
    // Using OpenFoodFacts API (Free)
    @GET("api/v0/product/{barcode}.json")
    Call<FoodResponse> getProductInfo(@retrofit2.http.Path("barcode") String barcode);
}