package com.example.richard_dt.visualisation.Helper.Test_swipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.richard_dt.visualisation.Activities.MainFrag;
import com.example.richard_dt.visualisation.Helper.ClassParameters;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard-DT on 19/08/2016.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<ClassParameters> classParametersArrayList;
    private Context context;


    public DataAdapter(ArrayList<ClassParameters> nClassParametersArrayList,Context mcContext) {
        this.classParametersArrayList = nClassParametersArrayList;
        context = mcContext;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ClassParameters classParameters = classParametersArrayList.get(i);
        viewHolder.tvLocalisationSelected.setText(classParameters.getClassroom().getClassroom_name());
        String staffListToString="";
        ArrayList<String> listStaff = classParameters.getStaffSelected();

        if(listStaff == null){

        }
        else {
            for (int j=0;j<listStaff.size();j++){
                if(j!=listStaff.size()-1) {
                    staffListToString += MainFrag.getStaff(listStaff.get(j),context).get(0).getNomComplet() + ",";
                }
                else {
                    staffListToString += MainFrag.getStaff(listStaff.get(j),context).get(0).getNomComplet();
                }
            }
        }
        viewHolder.tvStaffSelected.setText(staffListToString);

        String intensityListToString="";
        ArrayList<String> listIntensity = classParameters.getIntensitySelected();
        if(listIntensity == null){

        }
        else {
            for (int j=0;j<listIntensity .size();j++){
                if(j!=listIntensity .size()-1) {
                    intensityListToString += Cours.getIntensity().get(listIntensity.get(j)) + ",";
                }
                else {
                    intensityListToString += Cours.getIntensity().get(listIntensity.get(j));
                }
            }
        }
        viewHolder.tvIntensitySelected.setText(intensityListToString);
    }

    @Override
    public int getItemCount() {
        return classParametersArrayList.size();
    }





public void removeItem(int position) {
        classParametersArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, classParametersArrayList.size());
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvLocalisationSelected;
        TextView tvIntensitySelected;
        TextView tvStaffSelected;
//        ImageButton buttonDelete;
        public ViewHolder(View view) {
            super(view);
            tvLocalisationSelected = (TextView) itemView.findViewById(R.id.tvLocalisationSelected);
            tvIntensitySelected = (TextView) itemView.findViewById(R.id.tvIntensitySelected);
            tvStaffSelected = (TextView) itemView.findViewById(R.id.tvStaffSelected);

        }
    }
}