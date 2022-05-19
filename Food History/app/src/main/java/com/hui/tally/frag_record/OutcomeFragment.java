package com.hui.tally.frag_record;
import androidx.fragment.app.Fragment;
import com.hui.tally.R;
import com.hui.tally.db.DBManager;
import com.hui.tally.db.TypeBean;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 */
public class OutcomeFragment extends BaseRecordFragment {


    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //get data
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("Others");
        typeIv.setImageResource(R.mipmap.in_others_select);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItemToAccounttb(accountBean);
    }
}
