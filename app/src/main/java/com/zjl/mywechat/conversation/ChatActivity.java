package com.zjl.mywechat.conversation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zjl.mywechat.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        EaseChatFragment easeChatFragment = new EaseChatFragment();
        MyChatFragment easeChatFragment = new MyChatFragment();


        easeChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.chat_fr, easeChatFragment).commit();


    }
}
