package com.example.richard_dt.visualisation.gsApiClass;

public class Country {
    private String country_code ="";
    private String country_name ="";
    private String country_name_ISO="";
    private String country_default_language="";

    public Country(String code, String name, String nameISO,String defaultLanguage)
    {
        this.country_code=code;
        this.country_name=name;
        this.country_name_ISO=nameISO;
        this.country_default_language=defaultLanguage;

    }

    public String getCountry_name() {
        return country_name;
    }

    public String getCountry_code() {
        return country_code;
    }
}
