package com.zjl.mywechat.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FragmentTelList extends EaseContactListFragment implements View.OnClickListener {

    private Map<String, EaseUser> mMap;

    @Override
    protected void initView() {
        super.initView();
        Log.d("FragmentTelList", "initview");
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.contract_fragment, null);
        headerView.findViewById(R.id.re_newfriends).setOnClickListener(this);
        headerView.findViewById(R.id.re_chatroom).setOnClickListener(this);

        listView.addHeaderView(headerView);
        this.titleBar.setVisibility(View.GONE);
        getView().findViewById(R.id.search_bar_view).setVisibility(View.GONE);
        registerForContextMenu(listView);


    }


    @Override
    protected void setUpView() {
        Log.d("FragmentTelList", "setupview");
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
                            Log.d("FragmentTelList", "runonui");
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((EaseUser) listView.getItemAtPosition(position)).getUsername();
                showPopwindow(username);
//                try {
//                    EMClient.getInstance().contactManager().deleteContact(chatId);
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                });

                return true;
            }
        });
    }

    @Override
    public void refresh() {
      // setUpView();
        super.refresh();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.re_newfriends:

                // 跳转
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                startActivity(intent);


                break;

            case R.id.re_chatroom:


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
                    Thread.sleep(2000);
                    deletePop.dismiss();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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
}
