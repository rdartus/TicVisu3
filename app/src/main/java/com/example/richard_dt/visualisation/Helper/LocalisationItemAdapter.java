package com.example.richard_dt.visualisation.Helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.richard_dt.visualisation.R;

import java.util.List;

/**
 * Created by Richard-DT on 10/08/2016.
 */
public class LocalisationItemAdapter extends ArrayAdapter<LocalisationItem>{
    public LocalisationItemAdapter(Context context, int resource, List<LocalisationItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_localisation,parent, false);
        }

        LocalisationItemViewHolder viewHolder = (LocalisationItemViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new LocalisationItemViewHolder();
            viewHolder.nbErrors = (TextView) convertView.findViewById(R.id.tvNbErrors);
            viewHolder.llError = (LinearLayout) convertView.findViewById(R.id.llError);
            viewHolder.localisation = (TextView) convertView.findViewById(R.id.tvLocalisation);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ivStatusList);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<LocalisationItem> LocalisationItems
        LocalisationItem localisationItem = getItem(position);
        String error = Integer.toString(localisationItem.getNbErrors());
        int nbError = Integer.valueOf(error);

//        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_error_outline_white);
        if(nbError == 0 ){
            viewHolder.nbErrors.setVisibility(View.INVISIBLE);
            viewHolder.llError.setBackgroundColor(getContext().getResources().getColor(R.color.event_color_03));
            Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_check_white);
            viewHolder.imageView.setImageDrawable(icon);

        }
        else if(nbError< 999 && nbError>0){
            viewHolder.nbErrors.setVisibility(View.VISIBLE);
            viewHolder.llError.setBackgroundColor(getContext().getResources().getColor(R.color.event_color_02));
            viewHolder.nbErrors.setText(error);
            Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_error_outline_white);
            viewHolder.imageView.setImageDrawable(icon);
        }
        else if(nbError >999 ) {
            viewHolder.llError.setBackgroundColor(getContext().getResources().getColor(R.color.event_color_02));
            viewHolder.nbErrors.setVisibility(View.VISIBLE);
            viewHolder.nbErrors.setText("...");
            Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_error_outline_white);
            viewHolder.imageView.setImageDrawable(icon);
        }


        viewHolder.localisation.setText(localisationItem.getLocalisation());
        if(localisationItem.getSelected())
        {
            viewHolder.localisation.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            viewHolder.localisation.setTextColor(Color.WHITE);
        }
        else {
            viewHolder.localisation.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.localisation.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    public class LocalisationItemViewHolder {
        private LinearLayout llError;
        private TextView nbErrors;
        private TextView localisation;
        private ImageView imageView;
    }
}
