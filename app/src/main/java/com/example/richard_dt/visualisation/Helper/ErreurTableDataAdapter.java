package com.example.richard_dt.visualisation.Helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ErreurTableDataAdapter extends TableDataAdapter<ErreurItem> {

    private static final int TEXT_SIZE = 16;
    public ErreurTableDataAdapter(final Context context,final List<ErreurItem> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ErreurItem eI = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderString(eI.getClassroom_name());
                return renderedView;
            case 1:
                renderedView = renderStringDate(eI.getCours_date());
                return renderedView;
            case 2:
                renderedView = renderStringHorraire(eI.getCours_date());
                return renderedView;
            case 3:
                renderedView = renderStringErreur(eI.getIntensite());
                return renderedView;
            case 4:
                renderedView = renderStringErreur(eI.getErreurType());
                return renderedView;

        }
     return renderedView;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(50, 10, 50, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }
    private View renderStringErreur(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(10, 20, 10, 20);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }
    private View renderStringDate(final String value) {
        final TextView textView = new TextView(getContext());
        String[] test=value.split("//");
        textView.setText(test[1]);
        textView.setPadding(0, 0, 0, 0);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }
    private View renderStringHorraire(final String value) {
        final TextView textView = new TextView(getContext());
        String[] test=value.split("//");
        textView.setText(test[0]);
        textView.setPadding(0, 0, 0, 0);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

}
