package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hui.tally.adapter.ChartVPAdapter;
import com.hui.tally.db.DBManager;
import com.hui.tally.frag_chart.IncomChartFragment;
import com.hui.tally.frag_chart.OutcomChartFragment;
import com.hui.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {
    Button inBtn,outBtn;
    TextView dateTv,inTv,outTv;
    ViewPager chartVp;
     int year;
     int month;
    int selectPos = -1,selectMonth =-1;
    List<Fragment>chartFragList;
    private IncomChartFragment incomChartFragment;
    private OutcomChartFragment outcomChartFragment;
    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year,month);
        initFrag();
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
//      add obj for Fragment
        incomChartFragment = new IncomChartFragment();
        outcomChartFragment = new OutcomChartFragment();
//        add data to Fragment
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomChartFragment.setArguments(bundle);
        outcomChartFragment.setArguments(bundle);
//        add Fragment to datasource
        chartFragList.add(outcomChartFragment);
        chartFragList.add(incomChartFragment);
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVPAdapter);
//        add Fragment to Acitivy
    }

    /** get monthly records*/
    private void initStatistics(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);  //收入总钱数
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0); //支出总钱数
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);  //收入多少笔
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0); //支出多少笔
        dateTv.setText(year+"-"+month);
        inTv.setText("Cost "+inMoneyOneMonth+" Cal in total ");
        outTv.setText("Gain "+outMoneyOneMonth+" Cal in total");

    }

    /** initialize time*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    /** initialize view*/
    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_in);
        outTv = findViewById(R.id.chart_tv_out);
        chartVp = findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_rili:
                showCalendarDialog();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }
    /** show calendar*/
    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year,month);
                incomChartFragment.setDate(year,month);
                outcomChartFragment.setDate(year,month);
            }
        });
    }

    /** set button  out-0  in-1 */
    private void setButtonStyle(int kind){
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        }else if (kind == 1){
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}
