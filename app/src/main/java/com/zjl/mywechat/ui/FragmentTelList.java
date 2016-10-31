package com.zjl.mywechat.ui;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.addfriends.RequestActivity;
import com.zjl.mywechat.addfriends.RequestBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FragmentTelList extends EaseContactListFragment implements View.OnClickListener {

    private Map<String, EaseUser> mMap;
    private TextView tvUnAgreeNum;
    private String requestName;
    private String requestReason;

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


//        UnAgreeRequest receiver = new UnAgreeRequest();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("加好友");
//        getActivity().registerReceiver(receiver, filter);





        if (!EventBus.getDefault().isRegistered(this)) {
            // 接受的注册暂时写在这个Fragment里面
            EventBus.getDefault().register(this);
        }




    }


    @Override
    protected void setUpView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mMap = new HashMap<>();
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for (int i = 0; i < usernames.size(); i++) {
                        EaseUser easeUser = new EaseUser(usernames.get(i));
                        mMap.put(usernames.get(i), easeUser);
                    }
                    Iterator<Map.Entry<String, EaseUser>> iterator = mMap.entrySet().iterator();
                    List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
                    while (iterator.hasNext()) {
                        Map.Entry<String, EaseUser> entry = iterator.next();

                        if (!blackList.contains(entry.getKey())) {
                            // 不显示黑名单中的用户
                            EaseUser user = entry.getValue();
                            EaseCommonUtils.setUserInitialLetter(user);
                            contactList.add(user);
                        }
                    }
                    // 排序
                    Collections.sort(contactList, new Comparator<EaseUser>() {

                        @Override
                        public int compare(EaseUser lhs, EaseUser rhs) {
                            if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                                return lhs.getNick().compareTo(rhs.getNick());
                            } else {
                                if ("#".equals(lhs.getInitialLetter())) {
                                    return 1;
                                } else if ("#".equals(rhs.getInitialLetter())) {
                                    return -1;
                                }
                                return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                            }
                        }
                    });

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContactsMap(mMap);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        super.setUpView();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String chatId = ((EaseUser) listView.getItemAtPosition(position)).getUsername();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, chatId);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                startActivity(intent);

            }
        });
    }

    @Override
    public void refresh() {
        setUpView();
        super.refresh();


    }



    // 使用前注册
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(RequestBean event) {
        requestName = event.getName();
        requestReason = event.getReason();
    }



    // 取消注册
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.re_newfriends:
                // 将未读好友请求数目变成0
                num = 0;
                // 跳转
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                intent.putExtra("name", requestName);
                intent.putExtra("reason", requestReason);
                startActivity(intent);
                break;

            case R.id.re_chatroom:



                break;
        }
    }






    private int num = 0;// 从数据库里面取

//    private class UnAgreeRequest extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            tvUnAgreeNum.setVisibility(View.VISIBLE);
//            tvUnAgreeNum.setText(++num + "");
//            Log.d("UnAgreeRequest", "num:" + num);
//
//
//        }
//    }





}
