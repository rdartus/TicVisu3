package com.example.richard_dt.visualisation.Helper.Test_swipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.richard_dt.visualisation.Activities.MainFrag;
import com.example.richard_dt.visualisation.Helper.ClassParameters;
import com.example.richard_dt.visualisation.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView tvLocalisationSelected;
        TextView tvIntensitySelected;
        TextView tvStaffSelected;
        ImageButton buttonDelete;

        public SimpleViewHolder(View itemView) {
            super(itemView);
//            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            tvLocalisationSelected = (TextView) itemView.findViewById(R.id.tvLocalisationSelected);
            tvIntensitySelected = (TextView) itemView.findViewById(R.id.tvIntensitySelected);
            tvStaffSelected = (TextView) itemView.findViewById(R.id.tvStaffSelected);
//            buttonDelete = (ImageButton) itemView.findViewById(R.id.trash);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + tvLocalisationSelected.getText().toString());
                    Toast.makeText(view.getContext(), "onItemSelected: " + tvLocalisationSelected.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Context mContext;
    private ArrayList<ClassParameters> mDataset;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public RecyclerViewAdapter(Context context, ArrayList<ClassParameters> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        ClassParameters classParameters = mDataset.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());
                mItemManger.closeAllItems();
                MainFrag.refreshList();
//                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.tvLocalisationSelected.setText(classParameters.getClassroom().getClassroom_name());
        String staffListToString="";
        ArrayList listStaff = classParameters.getStaffSelected();
        if(listStaff == null){

        }
        else {
            for (int i=0;i<listStaff.size();i++){
                if(i!=listStaff.size()-1) {
                    staffListToString += listStaff.get(i) + ",";
                }
                else {
                    staffListToString += listStaff.get(i);
                }
            }
        }
        viewHolder.tvStaffSelected.setText(staffListToString);

        String intensityListToString="";
        ArrayList listIntensity = classParameters.getIntensitySelected();
        if(listIntensity == null){

        }
        else {
            for (int i=0;i<listIntensity .size();i++){
                if(i!=listIntensity .size()-1) {
                    intensityListToString += listIntensity .get(i) + ",";
                }
                else {
                    intensityListToString += listIntensity .get(i);
                }
            }
        }
        viewHolder.tvIntensitySelected.setText(intensityListToString);

        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
//        return R.id.swipe;
        return 0;
    }
}
