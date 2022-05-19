package com.hui.tally.frag_record;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hui.tally.R;
import com.hui.tally.db.TypeBean;
import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {
    Context context;
    List<TypeBean>mDatas;
    int selectPos = 0; //position of context
    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv,parent,false);
        //find
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);
        //get datasource from selected location
        TypeBean typeBean = mDatas.get(position);
        tv.setText(typeBean.getTypename());
        if (selectPos == position) {
            iv.setImageResource(typeBean.getSimageId());
        }else{
            iv.setImageResource(typeBean.getImageId());
        }
        return convertView;
    }
}
