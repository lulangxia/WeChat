package com.zjl.mywechat.contacts.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.database.DBTools;

import java.util.ArrayList;

public class AddContactsAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<RequestBean> arrayList;

    public AddContactsAdapter(Context mContext) {
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


        setTextOrButton(viewHolder, position);
        setButtonClickEvent(viewHolder, position);


        return convertView;
    }


    // 设置显示文字还是按钮
    private void setTextOrButton(ViewHolder viewHolder, int position) {

        if (arrayList.get(position).getIsPositive() == 1) {
            showText(viewHolder, "请等待对方验证");
        } else if (arrayList.get(position).getIsPositive() == 2){
            showText(viewHolder, "对方已同意");

        } else if (arrayList.get(position).getIsPositive() == 3){
            showText(viewHolder, "对方已拒绝");

        } else {

            if (arrayList.get(position).getIsAgree() == 0) {
                // 消息还未读，那就是默认布局
            } else {
                // 消息已读，选择出已经同意还是拒绝
                showText(viewHolder,"");
                // 已同意/拒绝
                if (arrayList.get(position).getIsAgree() == 2) {
                    viewHolder.tvIsAgree.setText("已拒绝");
                } else if (arrayList.get(position).getIsAgree() == 1){
                    viewHolder.tvIsAgree.setText("已同意");
                }
            }
        }
    }

    // 设置按钮点击事件
    private void setButtonClickEvent(ViewHolder viewHolder, final int position) {

        final ViewHolder finalViewHolder = viewHolder;

        // 同意
        viewHolder.btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(arrayList.get(position).getName());
                    Toast.makeText(mContext, "同意", Toast.LENGTH_SHORT).show();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

                showText(finalViewHolder, "已同意");

                // 同意的话，LitOrm表中添加一条数据
                RequestBean requestBean = arrayList.get(position);
                requestBean.setIsAgree(1);
                DBTools.getInstance().getmLiteOrm().update(requestBean);

            }
        });


        // 不同意
        viewHolder.btnDisAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Toast.makeText(mContext, "拒绝成功", Toast.LENGTH_SHORT).show();
                    EMClient.getInstance().contactManager().declineInvitation(arrayList.get(position).getName());


                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                showText(finalViewHolder,"已拒绝");

                RequestBean requestBean = arrayList.get(position);
                requestBean.setIsAgree(2);
                DBTools.getInstance().getmLiteOrm().update(requestBean);

            }
        });


    }

    // 设置tv显示内容
    private void showText(ViewHolder viewHolder, String text) {
        viewHolder.btnAgree.setVisibility(View.GONE);
        viewHolder.btnDisAgree.setVisibility(View.GONE);
        viewHolder.tvIsAgree.setVisibility(View.VISIBLE);
        viewHolder.tvIsAgree.setText(text);
    }



    private static class ViewHolder {

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
