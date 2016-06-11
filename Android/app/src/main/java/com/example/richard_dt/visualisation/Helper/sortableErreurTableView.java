package com.example.richard_dt.visualisation.Helper;


import android.content.Context;
import android.util.AttributeSet;

import com.example.richard_dt.visualisation.R;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

public class sortableErreurTableView extends SortableTableView<ErreurItem> {

    public sortableErreurTableView(Context context) {
        this(context,null);
    }
    public sortableErreurTableView(Context context, AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }
    public sortableErreurTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);


        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "Salle","Date","Heure","Intensité", "Type d'erreur");
        simpleTableHeaderAdapter.setTextColor(context.getResources().getColor(R.color.table_header_text));
        setHeaderAdapter(simpleTableHeaderAdapter);

        int rowColorEven = context.getResources().getColor(R.color.table_data_row_even);
        int rowColorOdd = context.getResources().getColor(R.color.table_data_row_odd);
        setDataRowColorizer(TableDataRowColorizers.alternatingRows(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        setColumnWeight(0, 3);
        setColumnWeight(1, 2);
        setColumnWeight(2, 2);
        setColumnWeight(3, 2);
        setColumnWeight(4, 12);


        setColumnComparator(0, ErreurComparator.getSalleComparator());
        setColumnComparator(1,ErreurComparator.getStringComparator());
        setColumnComparator(2,ErreurComparator.getStringComparator());
        setColumnComparator(3, ErreurComparator.getIntensiteComparator());
        setColumnComparator(4, ErreurComparator.getErreurComparator());

    }
}
