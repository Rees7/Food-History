package com.hui.tally.frag_chart;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hui.tally.R;
import com.hui.tally.adapter.ChartItemAdapter;
import com.hui.tally.db.ChartItemBean;
import com.hui.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
abstract public class BaseChartFragment extends Fragment {
    ListView chartLv;
     int year;
     int month;
    List<ChartItemBean>mDatas;   //datasource read
    private ChartItemAdapter itemAdapter;
    BarChart barChart;
    TextView chartTv;     //TextView without chart (no information)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    /** get data from Activity */
        View view =  inflater.inflate(R.layout.fragment_incom_chart, container, false);
        chartLv = view.findViewById(R.id.frag_chart_lv);

        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");

        mDatas = new ArrayList<>(); //datasource

        itemAdapter = new ChartItemAdapter(getContext(), mDatas); //adaptor
        chartLv.setAdapter(itemAdapter);

        addLVHeaderView();
        return view;
    }

    /** change to view form*/
    protected  void addLVHeaderView(){

        View headerView = getLayoutInflater().inflate(R.layout.item_chartfrag_top,null);
        chartLv.addHeaderView(headerView);
        barChart = headerView.findViewById(R.id.item_chartfrag_chart);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv);
        barChart.getDescription().setEnabled(false);
//        inner space
        barChart.setExtraOffsets(20, 20, 20, 20);
        /** get data from Activity */
        setAxis(year,month); // 设置坐标轴
        //xOy axis
        setAxisData(year,month);
    }
    protected abstract void setAxisData(int year, int month);

    /** show the chart*/
    protected  void setAxis(int year, final int month){
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //设置x轴显示在下方
        xAxis.setDrawGridLines(true);  //设置绘制该轴的网格线
        //x-lable=types
        xAxis.setLabelCount(31);
        xAxis.setTextSize(12f);
        //x value showing
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int) value;
                if (val == 0) {
                    return month+"-1";
                }
                if (val==14) {
                    return month+"-15";
                }
                if (month==2) {
                    if (val == 27) {
                        return month+"-28";
                    }
                }else if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                    if (val == 30) {
                        return month+"-31";
                    }
                }else if(month==4||month==6||month==9||month==11){
                    if (val==29) {
                        return month+"-30";
                    }
                }
                return "";
            }
        });
        xAxis.setYOffset(10); // verticle

        // y type
        setYAxis(year,month);
    }

    /** set y axis*/
    protected abstract void setYAxis(int year,int month);

    public void setDate(int year,int month){
        this.year = year;
        this.month = month;
        barChart.clear();
        barChart.invalidate();
        setAxis(year,month);
        setAxisData(year,month);
    }


    public void loadData(int year,int month,int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }
}
