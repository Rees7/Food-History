package com.hui.tally.frag_chart;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hui.tally.R;
import com.hui.tally.adapter.ChartItemAdapter;
import com.hui.tally.db.BarChartItemBean;
import com.hui.tally.db.ChartItemBean;
import com.hui.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomChartFragment extends BaseChartFragment {
    int kind = 1;
    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        //      get gain in total (month)
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size() == 0) {
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }else{
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);
        //   num of bars
            List<BarEntry> barEntries1 = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
        //   initialize bar and add it
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries1.add(entry);
            }
            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                /** get x axis location (-1 to the next day; get day now back) important!! */
                int day = itemBean.getDay();
                int xIndex = day-1;
                BarEntry barEntry = barEntries1.get(xIndex);
                barEntry.setY(itemBean.getSummoney());
            }
            //setting for value bar
            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.BLACK); // for nums
            barDataSet1.setValueTextSize(8f);
            barDataSet1.setColor(Color.parseColor("#006400")); // for bars

            /** setting for values shown on bar */
            barDataSet1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // float (x.x)
                    if (value==0) {
                        return "";
                    }
                    return value + "";
                }
            });
            sets.add(barDataSet1);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f);
            barChart.setData(barData);
        }
    }

    @Override
    /** setting for bars */
    protected void setYAxis(int year, int month) {
    //  set highest bar with biggest out
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney);   // celi it to get maxium

        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // max-y
        yAxis_right.setAxisMinimum(0f);  // min-y
        yAxis_right.setEnabled(false);  // hide y axis (will not be rectangle)

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }


    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,kind);
    }
}
