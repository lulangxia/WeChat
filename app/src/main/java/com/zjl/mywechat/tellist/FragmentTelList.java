package com.zjl.mywechat.tellist;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;


public class FragmentTelList extends BaseFragment {

    private ListView lvPeople;
    private TextView tvCharacter;
    private LinearLayout llCharacters;

    private boolean flag = false;
    private int height;

    // 字典对应
    private String[] indexStr = {"↑", "☆", "A", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z", "#"};
    // 没排序的
    private ArrayList<TelListBean> telListBeen;
    // 排好序的？/ 姓氏的汉字排序？
    private ArrayList<TelListBean> newTelListBeen;
    // 索引
    private HashMap<String, Integer> selector;


    @Override
    protected int setLayout() {
        return R.layout.fragment_main_tellist;
    }

    @Override
    protected void initView() {
        Log.d("FragmentTelList", "view");

        lvPeople = bindView(R.id.lv_main_tellist);
        tvCharacter = bindView(R.id.tv_tellist_character);
        llCharacters = bindView(R.id.ll_main_tellist);
        tvCharacter.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.contract_fragment, null);
        lvPeople.addHeaderView(view);
        Log.d("FragmentTelList", "data");
        telListBeen = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TelListBean bean = new TelListBean("张三" + i, "123");
            telListBeen.add(bean);
        }
        for (int i = 0; i < 10; i++) {
            TelListBean bean2 = new TelListBean("李四" + i, "456");
            telListBeen.add(bean2);
        }
        telListBeen.add(new TelListBean("王五", "1"));
        telListBeen.add(new TelListBean("孙六", "1"));

        newTelListBeen = new ArrayList<>();


        initSendInternet();

        // 观察者
        ViewTreeObserver observer = llCharacters.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!flag) {
                    height = llCharacters.getMeasuredHeight() / indexStr.length;
                    getIndexView();
                    flag = true;
                }
                return true;
            }
        });
    }


    private void initSendInternet() {
        // 网络解析
        String[] allNames = sortIndex(telListBeen);
        sortList(allNames);
        selector = new HashMap<String, Integer>();
        for (int i = 0; i < indexStr.length; i++) {
            for (int j = 0; j < newTelListBeen.size(); j++) {
                //?
                if (newTelListBeen.get(j).getName().equals(indexStr[i])) {
                    selector.put(indexStr[i], j);
                }
            }
        }


        // 绑定适配器
        LvAdapter adapter = new LvAdapter(getContext());
        adapter.setArrayList(newTelListBeen);
        lvPeople.setAdapter(adapter);


    }


    // 根据索引排序（封装好了的）
    private String[] sortIndex(ArrayList<TelListBean> telListBeen) {
        TreeSet<String> set = new TreeSet<String>();
        // 获取初始化数据源中的首字母，添加到set中
        for (TelListBean bean : telListBeen) {
            set.add(StringHelper.getPinYinHeadChar(bean.getName()).substring(0, 1));
        }
        // 新数组的长度为原数据加上set的大小
        String[] names = new String[telListBeen.size() + set.size()];
        int i = 0;
        for (String string : set) {
            names[i] = string;
            i++;
        }
        String[] pinYinNames = new String[telListBeen.size()];
        for (int j = 0; j < telListBeen.size(); j++) {
            telListBeen.get(j).setPinYinName(StringHelper.getPingYin(telListBeen.get(j).getName().toString()));
            pinYinNames[j] = StringHelper.getPingYin(telListBeen.get(j).getName().toString());
        }
        // 将原数据拷贝到新数据中
        System.arraycopy(pinYinNames, 0, names, set.size(), pinYinNames.length);
        // 自动按照首字母排序
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }


    // ListView内容的排序
    private void sortList(String[] allNames) {
        for (int i = 0; i < allNames.length; i++) {
            if (allNames[i].length() != 1) {
                for (int j = 0; j < telListBeen.size(); j++) {
                    if (allNames[i].equals(telListBeen.get(j).getPinYinName())) {
                        TelListBean p = new TelListBean(telListBeen.get(j).getName(), telListBeen.get(j).getPinYinName());
                        newTelListBeen.add(p);
                    }
                }
            } else {
                newTelListBeen.add(new TelListBean(allNames[i], ""));
            }
        }
    }


    // 根据索引拿到ListView的东西
    private void getIndexView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        for (int i = 0; i < indexStr.length; i++) {
            final TextView tv = new TextView(getContext());
            tv.setLayoutParams(params);
            tv.setPadding(10, 0, 10, 0);
            tv.setText(indexStr[i]);
            llCharacters.addView(tv);


            llCharacters.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float y = event.getY();
                    int index = (int) (y / height);
                    if (index > -1 && index < indexStr.length) {
                        String key = indexStr[index];
                        // selector没数据、、、
                        // 需要把数据索引添加到那里面去！
                        if (index == 0) {
                            lvPeople.setSelectionFromTop(0, 0);
                            tvCharacter.setVisibility(View.VISIBLE);
                            tvCharacter.setText(indexStr[index]);
                        }
                        if (selector.containsKey(key)) {
                            int pos = selector.get(key);
                            if (lvPeople.getHeaderViewsCount() > 0) {
                                lvPeople.setSelectionFromTop(pos + lvPeople.getHeaderViewsCount(), 0);
                            } else {
                                lvPeople.setSelectionFromTop(pos, 0);
                            }
                            tvCharacter.setVisibility(View.VISIBLE);
                            tvCharacter.setText(indexStr[index]);
                        }
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            llCharacters.setBackgroundColor(0x5f9a9a9a);
                            break;
                        case MotionEvent.ACTION_UP:
                            llCharacters.setBackgroundColor(Color.parseColor("#00000000"));
                            tvCharacter.setVisibility(View.INVISIBLE);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                    }

                    return true;
                }
            });

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        flag = false;
        Log.d("FragmentTelList", "res");
    }


}
