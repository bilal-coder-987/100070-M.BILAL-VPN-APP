package com.example.bilal.data.model;

public class Server {
    private String country;
    private String ip;
    private String flagUrl;
    private String countryCode;

    public Server(String country, String ip, String flagUrl, String countryCode) {
        this.country = country;
        this.ip = ip;
        this.flagUrl = flagUrl;
        this.countryCode = countryCode;
    }

    public String getCountry() { return country; }
    public String getIp() { return ip; }
    public String getFlagUrl() { return flagUrl; }
    public String getCountryCode() { return countryCode; }
}