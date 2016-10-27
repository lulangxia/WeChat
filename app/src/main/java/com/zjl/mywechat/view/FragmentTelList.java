package com.zjl.mywechat.view;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentTelList extends EaseContactListFragment implements View.OnClickListener {

    private Map<String, EaseUser> mMap;

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

//                String chatId = ((EaseUser)listView.getItemAtPosition(position)).getUsername();
//
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra(EaseConstant.EXTRA_USER_ID, chatId);
//                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
//                startActivity(intent);

            }
        });
    }

    @Override
    public void refresh() {
        super.refresh();


    }

    @Override
    public void onClick(View v) {

    }
}
