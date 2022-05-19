package com.hui.tally.frag_record;
import com.hui.tally.R;
import com.hui.tally.db.DBManager;
import com.hui.tally.db.TypeBean;
import java.util.List;
/**
 * record the in history
 */
public class IncomeFragment extends BaseRecordFragment {


    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //get datesource
        List<TypeBean> inlist = DBManager.getTypeList(1);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("Others");
        typeIv.setImageResource(R.mipmap.out_others_select);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);
    }
}
