package com.example.richard_dt.visualisation.Helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.richard_dt.visualisation.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private List<Item> itemList = new ArrayList<Item>();
    Context context;

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public void add(Item object) {
        itemList.add(object);
        super.add(object);
    }

    public void addAll(Collection<? extends Item> collection) {
        itemList.clear();
        if (itemList != null) {
            itemList.addAll(collection);
        }
    }


    @Override
    public void clear() {
        itemList.clear();
        super.clear();
    }

    public ItemAdapter(List<Item> rList, Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.itemList = rList;
        this.context=context;
    }

    private class ViewHolder {
        TextView txtItemName;
    }

    @Override
    public int getCount() {
        return this.itemList.size();
    }

    @Override
    public Item getItem(int index) {
        return this.itemList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            //LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.location_template, parent, false);
            holder = new ViewHolder();
            holder.txtItemName = (TextView) convertView.findViewById(R.id.tvItem);
            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }
            Item item = getItem(position);
            holder.txtItemName.setText(item.getItemName());

            return convertView;
        }
    }




