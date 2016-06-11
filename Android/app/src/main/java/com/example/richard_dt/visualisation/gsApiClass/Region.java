package com.example.richard_dt.visualisation.gsApiClass;

import java.util.ArrayList;

/**
 * Created by thibault on 05/05/2016.
 */
public class Region {

    private int region_id=0;
    private String region_name="";

    private String region_country_code="";
    private String[] location_list;

    private String region_gps_lat="";
    private String region_gps_lon="";
    private String region_map_zoom="";



    public ArrayList getClassroom_list() {
        return classroom_list;
    }

    private ArrayList classroom_list=new ArrayList();

    public String getRegion_name() {
        return region_name;
    }

    public String getRegion_id() {
        return region_id+"";
    }
    public String getRegion_country_code(){return region_country_code;}


}
