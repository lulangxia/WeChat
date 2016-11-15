package com.zjl.mywechat.conversation;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.zjl.mywechat.R;
import com.zjl.mywechat.stringvalue.Constant;

import java.util.List;

public class MyChatFragment extends EaseChatFragment {

    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;

    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;

    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;

    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;

    private static final int ITEM_RED_PACKET = 16;

    private static final int REQUEST_CODE_SEND_RED_PACKET = 16;


    // 添加红包按钮
    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
        // 给红包按钮单独设置一个点击监听
        ClickEvent event = new ClickEvent();
        if (chatType != Constant.CHATTYPE_CHATROOM) {
            // 复制粘贴的
            inputMenu.registerExtendMenuItem(R.string.attach_red_packet, R.drawable.em_chat_red_packet_selector, ITEM_RED_PACKET, event);
        }
    }



    // 添加按钮的点击事件
    class ClickEvent extends MyItemClickListener {
        @Override
        public void onClick(int itemId, View view) {
            super.onClick(itemId, view);
            switch (itemId) {
                case ITEM_RED_PACKET:
                    RedPacketUtil.startRedPacketActivityForResult(MyChatFragment.this, chatType, toChatUsername, REQUEST_CODE_SEND_RED_PACKET);
            }

        }
    }





    // 发红包消息到聊天界面
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEND_RED_PACKET:
                    if (data != null) {
                        sendMessage(RedPacketUtil.createRPMessage(getActivity(), data, toChatUsername));
                    }
            }
        }
    }









    // 群红包领取回执的处理
    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);

        for (EMMessage message : messages) {
            EMCmdMessageBody cmdMessageBody = (EMCmdMessageBody) message.getBody();
            String action = cmdMessageBody.action();
//            if (action.equals(RedPacketConstant.REFRESH_GROUP_MONEY_ACTION)) 压根没有这个类，怎么写。。
        }
    }


//    protected void startVoiceCall() {
//        if (!EMClient.getInstance().isConnected()) {
//            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
//        } else {
//            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
//                    .putExtra("isComingCall", false));
//            // voiceCallBtn.setEnabled(false);
//            inputMenu.hideExtendMenuContainer();
//        }
//    }



}
