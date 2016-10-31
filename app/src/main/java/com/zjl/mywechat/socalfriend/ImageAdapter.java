package com.zjl.mywechat.socalfriend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zjl.mywechat.R;

import java.util.List;

/**
 * Created by dllo on 16/10/29.
 */

public class ImageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    List<Uri> mList;

    public ImageAdapter(Context context, List<Uri> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int total = mList.size();
        if (total < 9 ) {
            total++;
        }
        return total;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

//    屏蔽android lint错误，所以在方法中还要判断版本做不同的操作
    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.fx_item_gridview_image,null);
        SimpleDraweeView sdv_image = (SimpleDraweeView) convertView.findViewById(R.id.sdv_image);

        if (position == mList.size() && mList.size() < 9) {
            GenericDraweeHierarchy hierarchy = sdv_image.getHierarchy();
            hierarchy.setPlaceholderImage(R.mipmap.fx_icon_tag_add);
        } else {
            Uri uri = (Uri) getItem(position);
            sdv_image.setImageURI(uri);
        }
        return convertView;
    }
}
