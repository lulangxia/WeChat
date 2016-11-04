package com.zjl.mywechat.contacts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.addfriends.RequestActivity;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.conversation.ChatActivity;
import com.zjl.mywechat.group.GrouplistActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentTelList extends EaseContactListFragment implements View.OnClickListener {

    private Map<String, EaseUser> mMap;
    private TextView tvUnAgreeNum;
    private String requestName;
    private String requestReason;
    private UnAgreeRequest mReceiver;
    private int num = 0;// 从数据库里面取
    private SharedPreferences sharedSetting;
    private SharedPreferences sharedPreferences;

    @Override
    protected void initView() {
        super.initView();

        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.contract_fragment, null);
        headerView.findViewById(R.id.re_newfriends).setOnClickListener(this);
        headerView.findViewById(R.id.re_chatroom).setOnClickListener(this);

        listView.addHeaderView(headerView);
        this.titleBar.setVisibility(View.GONE);
        getView().findViewById(R.id.search_bar_view).setVisibility(View.GONE);
        registerForContextMenu(listView);

        tvUnAgreeNum = (TextView) headerView.findViewById(R.id.tv_unread);


        if (!EventBus.getDefault().isRegistered(FragmentTelList.this)) {
            //        UnAgreeRequest receiver = new UnAgreeRequest();
            //        IntentFilter filter = new IntentFilter();
            //        filter.addAction("加好友");
            //        getActivity().registerReceiver(receiver, filter);


            if (!EventBus.getDefault().isRegistered(this)) {
                // 接受的注册暂时写在这个Fragment里面
                EventBus.getDefault().register(FragmentTelList.this);
            }


            mReceiver = new UnAgreeRequest();
            IntentFilter filter = new IntentFilter();
            filter.addAction("加好友");
            getActivity().registerReceiver(mReceiver, filter);



            registerForContextMenu(listView);
        }
    }

    @Override
    protected void setUpView() {


        super.setUpView();


        sharedPreferences = getActivity().getSharedPreferences("TelList", Context.MODE_PRIVATE);


        num = sharedPreferences.getInt("Num", 0);
        if (num != 0) {
            tvUnAgreeNum.setVisibility(View.VISIBLE);
            tvUnAgreeNum.setText(num + "");
        }
        Log.d("FragmentTelList2", "num:" + num);





        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(final List<String> strings) {
                mMap = new HashMap<String, EaseUser>();
                for (String s : strings) {
                    EaseUser user = new EaseUser(s);
                    mMap.put(s, user);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContactsMap(mMap);
                        refresh();
                    }
                });

            }

            @Override
            public void onError(int i, String s) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String chatId = ((EaseUser) listView.getItemAtPosition(position)).getUsername();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, chatId);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((EaseUser) listView.getItemAtPosition(position)).getUsername();
                showPopwindow(username);


                return true;
            }
        });
    }


    // 使用前注册
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(RequestBean event) {
        requestName = event.getName();
        requestReason = event.getReason();


        //        getActivity().runOnUiThread(new Runnable() {
        //            @Override
        //            public void run() {
        //                tvUnAgreeNum.setVisibility(View.VISIBLE);
        //                tvUnAgreeNum.setText(++num + "");
        //            }
        //        });

        Log.d("FragmentTelList", "？？");
        Log.d("UnAgreeRequest", "num:" + requestName);

    }


    //实时刷新通讯录页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Refresh(Boolean event) {
        if (event) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setUpView();
                    refresh();
                }
            });

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.re_newfriends:
                num = 0;
                tvUnAgreeNum.setVisibility(View.INVISIBLE);

                Intent broadIntent = new Intent("未读消息数目变化");
                broadIntent.putExtra("zeroNum", num);
                getActivity().sendBroadcast(broadIntent);


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Num", 0);
                editor.commit();



                // 跳转
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                intent.putExtra("name", requestName);
                intent.putExtra("reason", requestReason);
                startActivity(intent);
                break;

            case R.id.re_chatroom:
                Intent intent1 = new Intent(getActivity(), GrouplistActivity.class);
                startActivity(intent1);
                break;
        }
    }


    private void showPopwindow(final String username) {
        final PopupWindow deletePop = new PopupWindow();
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.deletepopwindow, null);

        // 设置SelectPicPopupWindow的View
        deletePop.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        deletePop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        deletePop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        deletePop.setFocusable(true);
        deletePop.setOutsideTouchable(true);
        // 刷新状态
        deletePop.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        deletePop.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        deletePop.setAnimationStyle(R.style.AnimationPop);

        conentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePop.dismiss();
            }
        });
        TextView deteText = (TextView) conentView.findViewById(R.id.delete_pop);
        deteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "shanchu", Toast.LENGTH_SHORT).show();
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    setUpView();
                    refresh();
                    deletePop.dismiss();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
        });
        if (!deletePop.isShowing()) {
            // 以下拉方式显示popupwindow
            deletePop.showAtLocation(listView, Gravity.TOP, 0, 0);
        } else {
            deletePop.dismiss();
        }
    }


    private class UnAgreeRequest extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            num = intent.getIntExtra("num", 0);
            tvUnAgreeNum.setVisibility(View.VISIBLE);
            tvUnAgreeNum.setText(num + "");
            Log.d("UnAgreeRequest", "num:" + num);


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Num", num);
            editor.commit();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
        EventBus.getDefault().unregister(this);

    }


}
