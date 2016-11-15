package com.zjl.mywechat.contacts.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.zjl.mywechat.R;
import com.zjl.mywechat.contacts.group.AddGroupMemberActivity;

import java.util.List;

/**
 * Created by dllo on 16/11/8.
 */

public class GroupGridAdapter extends BaseAdapter {
    Context mContext;

    private List<String> users;
    private String groupId;
    private OndeleteListener mOndeleteListener;
    private boolean deletebtn;



    public void setOndeleteListener(OndeleteListener ondeleteListener) {
        mOndeleteListener = ondeleteListener;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GroupGridAdapter(Context context) {
        mContext = context;
    }

    public void setUsers(List<String> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (users.get(0).equals(EMClient.getInstance().getCurrentUser())) {
            return users.size() + 2;
        } else if (users == null) {
            return 0;
        } else {
            return users.size();
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        GroupDetailViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.em_grid, null);
            groupViewHolder = new GroupDetailViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupDetailViewHolder) convertView.getTag();
        }

        if (position < users.size()) {
            groupViewHolder.mTextView.setText(users.get(position));
            groupViewHolder.delete.setVisibility(View.INVISIBLE);
            if (deletebtn == true) {
                groupViewHolder.delete.setVisibility(View.VISIBLE);
                final GroupDetailViewHolder finalGroupViewHolder = groupViewHolder;
                groupViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == 0) {
                            Toast.makeText(mContext, "不能删除群主", Toast.LENGTH_SHORT).show();
                        } else {
                            mOndeleteListener.delete(users.get(position));
                        }
                        deletebtn = false;
                        notifyDataSetChanged();
                    }
                });
            }
        } else if (position == users.size() && users.get(0).equals(EMClient.getInstance().getCurrentUser())) {
            groupViewHolder.delete.setVisibility(View.INVISIBLE);
            groupViewHolder.mTextView.setText("");
            groupViewHolder.mImageView.setImageResource(R.drawable.em_smiley_add_btn);
            groupViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, AddGroupMemberActivity.class).putExtra("newgroupid", groupId).putExtra("newgroupboo", false));
                }
            });
        } else if (users.get(0).equals(EMClient.getInstance().getCurrentUser())) {
            groupViewHolder.mTextView.setText("");
            groupViewHolder.delete.setVisibility(View.INVISIBLE);
            groupViewHolder.mImageView.setImageResource(R.drawable.em_smiley_minus_btn);
            groupViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletebtn = true;
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }


    class GroupDetailViewHolder {
        ImageView mImageView;
        TextView mTextView;
        ImageView delete;

        public GroupDetailViewHolder(View convertView) {
            mImageView = (ImageView) convertView.findViewById(R.id.iv_avatar);
            mTextView = (TextView) convertView.findViewById(R.id.tv_name);
            delete = (ImageView) convertView.findViewById(R.id.badge_delete);

        }
    }

    public interface OndeleteListener {
        void delete(String s);
    }
}
