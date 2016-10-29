package com.zjl.mywechat.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
        Log.d("FragmentConversationLis", "initview");
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.fragment_conversationlist, null);
        errorItemContainer.addView(errorView);
        mErrorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        this.titleBar.setVisibility(View.GONE);

    }

    @Override
    protected void setUpView() {
        Log.d("FragmentConversationLis", "setupview");
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
        conversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                showPopwindow(username);
                refresh();
                return true;
            }
        });


        //发广播,未读消息数
        int allnum = 0;
        Log.d("FragmentConversationLis", "allnum0:" + allnum);
        Log.d("FragmentConversationLis", "conversationListView.getCount():" + conversationListView.getCount());
        for (int i = 0; i < conversationListView.getCount() / 2; i++) {
            EMConversation conversation = conversationListView.getItem(i);
            Log.d("FragmentConversationLis", "i:" + i);
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
        Log.d("FragmentConversationLis", "2");
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
        deteText.setText("删除该聊天");
        deteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "shanchu", Toast.LENGTH_SHORT).show();

                //删除和某个user会话，如果需要保留聊天记录，传false
                EMClient.getInstance().chatManager().deleteConversation(username, true);


                deletePop.dismiss();

            }
        });
        if (!deletePop.isShowing()) {
            // 以下拉方式显示popupwindow
            deletePop.showAtLocation(conversationListView, Gravity.TOP, 0, 0);
        } else {
            deletePop.dismiss();
        }

    }


}
