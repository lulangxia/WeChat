package com.zjl.mywechat.addfriends;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.bean.RequestBean;

import java.util.ArrayList;

public class Lv extends BaseAdapter{

    private Context mContext;
    private ArrayList<RequestBean> arrayList;

    public Lv(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(ArrayList<RequestBean> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_personrequest, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(arrayList.get(position).getName());
        viewHolder.tvReason.setText(arrayList.get(position).getReason());

        // 同意
        final ViewHolder finalViewHolder = viewHolder;


        viewHolder.btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(arrayList.get(position).getName());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                finalViewHolder.btnAgree.setVisibility(View.GONE);
                finalViewHolder.btnDisAgree.setVisibility(View.GONE);
                finalViewHolder.tvIsAgree.setVisibility(View.VISIBLE);
                finalViewHolder.tvIsAgree.setText("已同意");

            }
        });


        // 不同意
        viewHolder.btnDisAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    EMClient.getInstance().contactManager().declineInvitation(arrayList.get(position).getName());
                    finalViewHolder.btnAgree.setVisibility(View.GONE);
                    finalViewHolder.btnDisAgree.setVisibility(View.GONE);
                    finalViewHolder.tvIsAgree.setVisibility(View.VISIBLE);
                    finalViewHolder.tvIsAgree.setText("已拒绝");

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }



            }
        });




        return convertView;
    }


    private class ViewHolder {

        private TextView tvName;
        private TextView tvReason;

        private Button btnAgree;
        private Button btnDisAgree;

        private TextView tvIsAgree;

        public ViewHolder(View view) {

            tvName = (TextView) view.findViewById(R.id.tv_request_name);
            tvReason = (TextView) view.findViewById(R.id.tv_request_reason);

            btnAgree = (Button) view.findViewById(R.id.btn_request_agree);
            btnDisAgree = (Button) view.findViewById(R.id.btn_request_disagree);

            tvIsAgree = (TextView) view.findViewById(R.id.tv_request_isagree);
        }







    }





}
