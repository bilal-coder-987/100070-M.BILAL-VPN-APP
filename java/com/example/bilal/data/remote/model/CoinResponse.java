package com.example.bilal.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class CoinResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("symbol")
    public String symbol;

    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    @SerializedName("current_price")
    public double currentPrice;

    @SerializedName("price_change_percentage_24h")
    public double priceChangePercentage24h;
}