package com.zjl.mywechat.ui;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.zjl.mywechat.R;
import com.zjl.mywechat.staticfinal.Constant;

/**
 * Created by dllo on 16/10/27.
 */

public class FragmentConversationList extends EaseConversationListFragment {

    private TextView mErrorText;

    private EMMessageListener msgListener;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.fragment_conversationlist, null);
        errorItemContainer.addView(errorView);
        mErrorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        this.titleBar.setVisibility(View.GONE);

    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("FragmentConversationLis", "position:" + position);
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });

        //发广播,未读消息数
        int allnum = 0;
        Log.d("FragmentConversationLis", "allnum0:" + allnum);
        Log.d("FragmentConversationLis", "conversationListView.getCount():" + conversationListView.getCount());
        for (int i = 0; i < conversationListView.getCount()-1; i++) {
            EMConversation conversation = conversationListView.getItem(i);
            allnum += conversation.getUnreadMsgCount();
        }
        Log.d("FragmentConversationLis", "allnum:" + allnum);

        Intent numIntent = new Intent(Constant.UNREAD_MSG);
        numIntent.putExtra(Constant.UNREAD_MSG_CONVERSA, allnum);
        getActivity().sendBroadcast(numIntent);


    }


    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            mErrorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            mErrorText.setText(R.string.the_current_network);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.getUserName());
        }

        refresh();

        // update unread count
        return true;
    }


}
