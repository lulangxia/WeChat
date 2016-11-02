package com.zjl.mywechat.socalfriend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSONObject;
import com.zjl.mywechat.R;

import java.util.List;

/**
 * Created by dllo on 16/10/31.
 */

public class SocialMainAdapter extends BaseAdapter{

    private Activity content;
    private List<JSONObject> users;
    private LayoutInflater inflater;

    public SocialMainAdapter(Activity content1, List<JSONObject> articles) {
        this.content = content1;
        this.users = articles;

        inflater = LayoutInflater.from(content);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        } else {
            return users.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            View view = inflater.inflate(R.layout.fx_item_moments_header,null,false);
            return view;
        } else {
            convertView = inflater.inflate(R.layout.item_social_main,null,false);
            return convertView;
        }
    }
}
