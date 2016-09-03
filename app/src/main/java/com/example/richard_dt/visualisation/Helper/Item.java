package com.example.richard_dt.visualisation.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Richard-DT on 06/05/2016.
 */
public class Item<U> {
        private String name = "";
        private U id = null;
        private boolean isSelected;

    public Item() {
        }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Item(String name,U id){
            this.name = name;
            this.id=id;
        }
    public U getId(){
        return id;
    }

        public String getItemName() {
            return name;
        }

        public void setItemName(String name) {
            this.name = name;
        }
//
//    public String getStrId() {
//        return (String)id;
//    }
//    public Calendar getCalendar() {
//        return (Calendar)id;
//    }

//    public void setId(U id) {
//        this.id = id;
//    }

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
