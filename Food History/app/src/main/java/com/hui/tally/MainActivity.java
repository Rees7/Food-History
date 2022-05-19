package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hui.tally.adapter.AccountAdapter;
import com.hui.tally.db.AccountBean;
import com.hui.tally.db.DBManager;
import com.hui.tally.utils.BudgetDialog;
import com.hui.tally.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;  //ListView of today
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;
    //page deployer kong-jian
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        //add head for ListView
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //set adaptor and load data for list
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }
     /** initialize View */
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }
    /** set onclick event for ListView*/
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  //head = 0
                    return false;
                }
                int pos = position-1;
                AccountBean clickBean = mDatas.get(pos);  //get clicked position

                //whether to delete?
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
    /** delete function */
    private void showDeleteItemDialog(final  AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tip").setMessage("Do you want to delete this record？")
                .setNegativeButton("No",null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //deletion
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);   //refresh after deletion
                        adapter.notifyDataSetChanged();   //adaptor refresh
                        setTopTvShow();   //relist page from head
                    }
                });
        builder.create().show();
    }

    /** add head for ListView*/
    private void addLVHeaderView() {
        //change to View form
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }
    /** get time of today*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // set activity to get focus
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }
    /** set context in head */
    private void setTopTvShow() {
        //get list for today -> view
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "Daily Gain Cal "+outcomeOneDay+"  Cost Cal "+incomeOneDay;
        topConTv.setText(infoOneDay);
        //get list for this month -> view
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("Cal "+incomeOneMonth);
        topOutTv.setText("Cal "+outcomeOneMonth);

    //set budget
        float bmoney = preferences.getFloat("bmoney", 0); //initial=0, set by howBudgetDialog() line202
        if (bmoney == 0) {
            topbudgetTv.setText("Cal 0");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            topbudgetTv.setText("Cal "+syMoney);
        }
    }

    // load from db
    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_search:
                Intent it = new Intent(this, SearchActivity.class);  //jump
                startActivity(it);
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);  //jump
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                // textview change encrypt and decrypt
                toggleShow();
                break;
        }
        if (v == headerView) {
            //heard onclick
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }
    /** show dialog and set budget*/
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //load budget into bmoney function
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                //calculate remain
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money-outcomeOneMonth;//r = b- out
                topbudgetTv.setText("Cal "+syMoney);
            }
        });
    }

    boolean isShow = true;
    /**
     * onclick head button: change encrype <-> decrypt
     * */
    private void toggleShow() {
        if (isShow) {   //password->********
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);   //all transfer to hide mode
            topOutTv.setTransformationMethod(passwordMethod);
            topbudgetTv.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;   //also hide position mark
        }else{  //******** -> password
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);   // all hidden
            topOutTv.setTransformationMethod(hideMethod);
            topbudgetTv.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;   //also hide position mark
        }
    }
}
