package com.example.bilal.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class FoodResponse {
    @SerializedName("status")
    public int status;

    @SerializedName("product")
    public Product product;

    public static class Product {
        @SerializedName("product_name")
        public String productName;

        @SerializedName("nutriments")
        public Nutriments nutriments;
    }

    public static class Nutriments {
        @SerializedName("proteins_100g")
        public float proteins;

        @SerializedName("carbohydrates_100g")
        public float carbohydrates;

        @SerializedName("fat_100g")
        public float fat;

        @SerializedName("energy-kcal_100g")
        public float calories;
    }
}