package com.example.richard_dt.visualisation.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Richard-DT on 06/05/2016.
 */
public class Item {
        private String name = "";
        private Object id = null;
        public Item() {
        }

        public Item(String name,Object id){
            this.name = name;
            this.id=id;
        }
    public Object getId(){
        return id;
    }

        public String getItemName() {
            return name;
        }

        public void setItemName(String name) {
            this.name = name;
        }

    public String getStrId() {
        return (String)id;
    }
    public Calendar getCalendar() {
        return (Calendar)id;
    }

    public void setId(String id) {
        this.id = id;
    }

    static public ArrayList getItemList(ArrayList<String> nameList, ArrayList<String> idList){
            ArrayList al = new ArrayList();

            if(nameList.size() == idList.size()){
                for(int i=0;i<idList.size();i++){
                    al.add(new Item(nameList.get(i),idList.get(i)));
                }
            }

            return al;
        }
    }
