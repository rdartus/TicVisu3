package com.example.richard_dt.visualisation.Helper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by thibault on 07/06/2016.
 */
public class serialArrayArray implements Serializable {

    private ArrayList<ArrayList> floors;

    public serialArrayArray(ArrayList<ArrayList> floors) {
        this.floors = floors;
    }

    public ArrayList<ArrayList> getList() {
        return this.floors;
    }
}