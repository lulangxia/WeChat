package com.zjl.mywechat.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zjl.mywechat.R;
import com.zjl.mywechat.bean.GroupContactBean;

import java.util.List;

/**
 * Created by dllo on 16/11/4.
 */

public class AddGroupAdapter extends BaseAdapter {
    Context mContext;
    List<GroupContactBean> friends;

    public AddGroupAdapter(Context context) {
        mContext = context;
    }

    public void setFriends(List<GroupContactBean> friends) {
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size() == 0 ? 0 : friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_addgroup, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.name.setText(friends.get(position).getName());
        groupViewHolder.cb.setChecked(friends.get(position).isChecked());
        groupViewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cbc = (CheckBox) v;
                friends.get(position).setChecked(cbc.isChecked());
            }
        });
        return convertView;
    }

    class GroupViewHolder {

        private final TextView name;
        private final CheckBox cb;

        public GroupViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            cb = (CheckBox) convertView.findViewById(R.id.check_addgroup);

        }
    }
}
