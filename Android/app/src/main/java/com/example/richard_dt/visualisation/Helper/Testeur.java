package com.example.richard_dt.visualisation.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.ListView;
import android.widget.TextView;

import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import java.util.ArrayList;
import java.util.List;


public class Testeur {
    public static String[] test(Cours c) {
        if (c.hasIssue()) {
            int level = c.getClass_level();

            String returned[] = new String[3];
            returned[0] = "";
            returned[1] = c.getClass_id() + "";
            returned[2] = "";


            c.getTeacher_list();
            c.getHost_list();

            if (level < 31 || level == 50 || level == 60 || level == 65) {
                if (c.getTeacher_list().size() == 0)  {
                    returned[2] = " | Critique | ";
                    returned[0] = returned[0] + returned[2] + " Il y a 0 professeur sur " + 1 + " pour ce cours ";
                }
                if (c.getHost_list().size() == 0) {
                    returned[2] = " | Critique | ";
                    returned[0] = returned[0] + returned[2] + " Il y a 0 animateur sur " + 1 + " pour ce cours ";
                }
            } else if (level > 66) {
                if (c.getTeacher_list().size() != 0) {
                    returned[0] = returned[0] + " Il y a "+c.getTeacher_list().size()+" professeur sur " + 2 + " pour ce cours ";

                } else {
                    returned[2] = " | Critique | ";
                    returned[0] = returned[0] + returned[2] + " Il y a "+c.getTeacher_list().size()+" professeur sur " + 2 + " pour ce cours ";
                }
                if (c.getHost_list().size() != 0) {
                    returned[0] = returned[0] + " Il y a "+c.getHost_list().size()+" animateur sur " + 2 + " pour ce cours ";

                } else {
                    returned[2] = " | Critique | ";
                    returned[0] = returned[0] + returned[2] + " Il y a "+c.getHost_list().size()+" animateur sur " + 2 + " pour ce cours ";
                }
            } else if (level > 44 && level < 50) {
                if (c.getTeacher_list() != null) {
                    returned[0] = returned[0] + " Il y a "+c.getTeacher_list().size()+" professeur sur " + 2 + " pour ce cours ";

                } else {
                    returned[2] = " | Critique | ";
                    returned[0] = returned[0] + returned[2] + " Il y a "+c.getTeacher_list().size()+" professeur sur " + 2 + "pour ce cours ";
                }
                if (c.getHost_list().size() != 0) {
                    returned[0] = returned[0] + " Il y a "+c.getHost_list().size()+" animateur sur " + 3 + " pour ce cours ";

                } else {
                    returned[2] = " | Critique  | ";
                    returned[0] = returned[0] + returned[2] + " Il y a "+c.getHost_list().size()+" animateur sur " + 3 + " pour ce cours ";
                }

            }
            returned[2] = c.getClassroom_id() + "";
            return returned;


        }

         return null;
    }


    public static String testProf(Staff prof){

        String Error="OK";
                if(prof.getStaff_issue_type()!=0)
                {

                    if(prof.getStaff_issue_type()==1)
                    {
                        Error="Ce professeur fait trop d'heures de cours d'affilées";
                    }else if(prof.getStaff_issue_type()==2)
                    {Error ="pas encore trouvé cet indicateur";}

                    return Error;

                }
                else{return Error;}
    }

    public static void ColorListViewSolo(int position, ListView listView, ItemAdapter adapter, Context context){
        int indexFirstVisibleItem=0;
        while (!((TextView) listView.getChildAt(0).findViewById(R.id.tvItem)).getText().equals(adapter.getItem(indexFirstVisibleItem).getItemName())
                && !((TextView) listView.getChildAt(0).findViewById(R.id.tvItem)).getText().equals(adapter.getItem(indexFirstVisibleItem).getStrId())){
            indexFirstVisibleItem++;
        }
        for (int child = 0; child < listView.getChildCount(); child++) {
            int index = child+indexFirstVisibleItem;
            if (child == position - indexFirstVisibleItem) {
                listView.getChildAt(child).setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

                ((TextView) listView.getChildAt(child).findViewById(R.id.tvItem)).setTextColor(context.getResources().getColor(R.color.toolbar_text));
            } else {
                listView.getChildAt(child).setBackgroundColor(Color.TRANSPARENT);

                ((TextView) listView.getChildAt(child).findViewById(R.id.tvItem)).setTextColor(Color.BLACK);
            }
        }
    }

    public static void ColorListViewMulti(ListView listView, ItemAdapter adapter, Context context , List container){
        int indexFirstVisibleItem=0;
        while (!((TextView) listView.getChildAt(0).findViewById(R.id.tvItem)).getText().equals(adapter.getItem(indexFirstVisibleItem).getItemName())
                && !((TextView) listView.getChildAt(0).findViewById(R.id.tvItem)).getText().equals(adapter.getItem(indexFirstVisibleItem).getStrId())){
            indexFirstVisibleItem++;
        }
        for (int child = 0; child < listView.getChildCount(); child++) {
            if (container.contains(adapter.getItem(child+indexFirstVisibleItem).getStrId())){
                listView.getChildAt(child).setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                ((TextView) listView.getChildAt(child).findViewById(R.id.tvItem)).setTextColor(Color.WHITE);
            } else {
                listView.getChildAt(child).setBackgroundColor(Color.TRANSPARENT);
                ((TextView) listView.getChildAt(child).findViewById(R.id.tvItem)).setTextColor(Color.BLACK);

            }
        }
    }
}
