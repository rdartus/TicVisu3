package com.example.richard_dt.visualisation.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.richard_dt.visualisation.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ErreurItemAdapter extends ArrayAdapter<ErreurItem> {

    private List<ErreurItem> erreurItemList = new ArrayList<ErreurItem>();
    Context context;

    @Override
    public void add(ErreurItem object) {
        erreurItemList.add(object);
        super.add(object);
    }

    public void addAll(Collection<? extends ErreurItem> collection) {
        if(erreurItemList!=null)for(int i=0;i<erreurItemList.size();i++){erreurItemList.clear();}
        erreurItemList.addAll(collection);
        super.addAll(collection);
    }


    public ErreurItemAdapter(Context context, int resource,List<ErreurItem> erreurItemList) {
        super(context, resource);
        this.context=context;
        this.erreurItemList=erreurItemList;
        super.addAll(erreurItemList);
    }
    private class ViewHolder {
        TextView txtClassroomName;
        TextView txtCoursDate;
        TextView txtIntensite;
        TextView txtTypeErreur;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.erreur_template, parent, false);
            holder = new ViewHolder();
            holder.txtClassroomName = (TextView) convertView.findViewById(R.id.tvClassroomName);
            holder.txtCoursDate=(TextView) convertView.findViewById(R.id.tvCoursDate);
            holder.txtIntensite=(TextView) convertView.findViewById(R.id.tvIntensite);
            holder.txtTypeErreur=(TextView)convertView.findViewById(R.id.tvErreurType);



            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }
        ErreurItem item = getItem(position);
        holder.txtClassroomName.setText(item.getClassroom_name());
        holder.txtCoursDate.setText(item.getCours_date());
        holder.txtIntensite.setText(item.getCours_date());
        holder.txtTypeErreur.setText(item.getErreurType());

        return convertView;
    }
}
