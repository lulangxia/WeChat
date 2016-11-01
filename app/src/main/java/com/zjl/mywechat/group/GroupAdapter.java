package com.zjl.mywechat.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.zjl.mywechat.R;

import java.util.List;

/**
 * Created by dllo on 16/11/1.
 */
public class GroupAdapter extends BaseAdapter {

    Context mContext;
    List<EMGroup> mGroupList;

    public GroupAdapter(Context context) {
        mContext = context;
    }

    public void setGroupList(List<EMGroup> groupList) {
        mGroupList = groupList;
    }

    @Override
    public int getCount() {
        return mGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.test_group_item, parent, false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.mTextView.setText(mGroupList.get(position).getGroupName());

        return convertView;
    }

    class GroupViewHolder {
        TextView mTextView;

        public GroupViewHolder(View convertView) {
            mTextView = (TextView) convertView.findViewById(R.id.te_item);

        }
    }


}

