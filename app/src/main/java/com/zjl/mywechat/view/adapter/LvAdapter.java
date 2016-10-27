package com.zjl.mywechat.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjl.mywechat.R;
import com.zjl.mywechat.bean.TelListBean;

import java.util.ArrayList;

public class LvAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<TelListBean> arrayList;

    public LvAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(ArrayList<TelListBean> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        // 字母等单个字符就绑定那个布局。。。
        String item = arrayList.get(position).getName();


        // 没办法使用重用池？

//        if (convertView == null) {
            if (item.length() == 1) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_tellist_index, null);
                viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_tellist_index);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_tellist, parent, false);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_tellist_name);
            }
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

        // 赋值
        if (item.length() == 1) {
            viewHolder.tvIndex.setText(arrayList.get(position).getName());
        } else {
            viewHolder.tvName.setText(arrayList.get(position).getName());
        }




        return convertView;
    }

    private class ViewHolder {

        private TextView tvName;
        private TextView tvIndex;
        private ImageView iv;

//        public ViewHolder(View view) {
//            tv = (TextView) view.findViewById(R.id.tv_tellist_name);
//        }


    }



}
