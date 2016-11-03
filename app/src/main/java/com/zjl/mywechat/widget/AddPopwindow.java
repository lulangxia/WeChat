package com.zjl.mywechat.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zjl.mywechat.R;
import com.zjl.mywechat.addfriends.AddFriendsActivity;
import com.zjl.mywechat.group.AddGroupActivity;


/**
 * Created by dllo on 16/10/21.
 */

public class AddPopwindow extends PopupWindow {

    private View conentView;


    @SuppressLint("InflateParams")
    public AddPopwindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popwindow_add, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPop);

        conentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPopwindow.this.dismiss();
            }
        });
        RelativeLayout re_addfriends = (RelativeLayout) conentView.findViewById(R.id.re_addfriends);
        RelativeLayout re_chatroom = (RelativeLayout) conentView.findViewById(R.id.re_chatroom);
        re_addfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "添加朋友", Toast.LENGTH_SHORT).show();
                AddPopwindow.this.dismiss();
                Intent intent = new Intent(context, AddFriendsActivity.class);
                context.startActivity(intent);

            }

        });
        re_chatroom.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "创建群聊", Toast.LENGTH_SHORT).show();
                AddPopwindow.this.dismiss();
                Intent intent = new Intent(context, AddGroupActivity.class);
                context.startActivity(intent);

            }

        });


    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

}
