package com.zjl.mywechat.socalfriend.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zjl.mywechat.R;
import com.zjl.mywechat.app.MyApp;
import com.zjl.mywechat.socalfriend.modle.Param;
import com.zjl.mywechat.socalfriend.modle.PreferenceManager;
import com.zjl.mywechat.socalfriend.presenter.OkHttpManager;
import com.zjl.mywechat.socalfriend.view.BigImageActivity;
import com.zjl.mywechat.values.FXConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dllo on 16/10/31.
 */

public class SocialMainAdapter extends BaseAdapter {

    private Activity context;
    private List<JSONObject> users;
    private LayoutInflater inflater;
    private String myuserID;
    private RelativeLayout re_edittext;
    private String myNick;
    private String myAvatar;

    public SocialMainAdapter(Activity content1, List<JSONObject> articles) {
        this.context = content1;
        this.users = articles;
        inflater = LayoutInflater.from(context);

        // 底部评论栏
        re_edittext = (RelativeLayout) context.findViewById(R.id.re_edittext);

        myuserID = PreferenceManager.getIntance().getCurrentUserName();
        myAvatar = MyApp.getApp().getUserJson().getString(FXConstant.JSON_KEY_AVATAR);
        myNick = MyApp.getApp().getUserJson().getString(FXConstant.JSON_KEY_NICK);
    }

    @Override
    public int getCount() {
        return users.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        } else {
            return users.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            View view = inflater.inflate(R.layout.fx_item_moments_header, null, false);
            return view;
        } else {
            convertView = inflater.inflate(R.layout.item_social_main, null, false);

            ViewHolder holder = (ViewHolder) convertView.getTag();

            if (holder == null) {
                holder = new ViewHolder();
                holder.tv_nick = (TextView) convertView
                        .findViewById(R.id.tv_nick);
                holder.tv_time = (TextView) convertView
                        .findViewById(R.id.tv_time);

                holder.iv_avatar = (SimpleDraweeView) convertView
                        .findViewById(R.id.sdv_image);
                holder.image_1 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_1);
                holder.image_2 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_2);
                holder.image_3 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_3);
                holder.image_4 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_4);
                holder.image_5 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_5);
                holder.image_6 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_6);
                holder.image_7 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_7);
                holder.image_8 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_8);
                holder.image_9 = (SimpleDraweeView) convertView
                        .findViewById(R.id.image_9);
                holder.ll_one = (LinearLayout) convertView
                        .findViewById(R.id.ll_one);
                holder.ll_two = (LinearLayout) convertView
                        .findViewById(R.id.ll_two);
                holder.ll_three = (LinearLayout) convertView
                        .findViewById(R.id.ll_three);
                holder.tv_location = (TextView) convertView
                        .findViewById(R.id.tv_location);
                holder.tv_content = (TextView) convertView
                        .findViewById(R.id.tv_content);
                holder.iv_pop = (ImageView) convertView
                        .findViewById(R.id.iv_pop);
                holder.tv_commentmenbers = (TextView) convertView
                        .findViewById(R.id.tv_commentmembers);
                holder.tv_goodmenbers = (TextView) convertView
                        .findViewById(R.id.tv_goodmembers);
                holder.ll_goodmenbers = (LinearLayout) convertView
                        .findViewById(R.id.ll_goodmembers);
                holder.view_pop = convertView.findViewById(R.id.view_pop);

                holder.tv_delete = (TextView) convertView
                        .findViewById(R.id.tv_delete);
                convertView.setTag(holder);
            }
            JSONObject json = users.get(position - 1);
            if (json == null || json.size() == 0) {
                users.remove(position - 1);
                this.notifyDataSetChanged();
            }
            final String userID = json.getString("userID");
            final String content = json.getString("content");
            String imageStr = json.getString("imageStr");
            String location = json.getString("location");
            final String sID = json.getString("sID");
            String rel_time = json.getString("time");
            Log.d("SocialMainAdapterddddddddd", myuserID);
            // 设置删除键
            if (userID.equals(myuserID)) {

                holder.tv_delete.setVisibility(View.VISIBLE);
                holder.tv_delete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showPhotoDialog(position - 1, sID);
                        //users.remove(position - 1);
                        notifyDataSetChanged();
                    }

                });
            } else {
                holder.tv_delete.setVisibility(View.GONE);
            }

            String nick = userID;
            String avatar = "";

            holder.tv_nick.setText(nick);
            holder.iv_avatar.setImageResource(R.mipmap.fx_default_useravatar);

            holder.tv_nick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //context.startActivity(new Intent(context,));
                }
            });

            Log.d("SocialMainAdapter--->>", imageStr);
            if (!imageStr.equals("0")) {
                String[] images = imageStr.split("split");
                int imNumb = images.length;
                holder.image_1.setVisibility(View.VISIBLE);
                holder.image_1.setImageURI(Uri.parse(FXConstant.URL_SOCIAL_PHOTO
                        + images[0]));
                holder.image_1.setOnClickListener(new ImageListener(images, 0));

                Log.e("imNumb--->>", String.valueOf(imNumb));
                // 四张图的时间情况比较特殊
                if (imNumb == 4) {
                    holder.image_2.setVisibility(View.VISIBLE);
                    holder.image_2.setImageURI(Uri
                            .parse(FXConstant.URL_SOCIAL_PHOTO + images[1]));
                    holder.image_2.setOnClickListener(new ImageListener(images,
                            1));
                    holder.image_4.setVisibility(View.VISIBLE);
                    holder.image_4.setImageURI(Uri
                            .parse(FXConstant.URL_SOCIAL_PHOTO + images[2]));
                    holder.image_4.setOnClickListener(new ImageListener(images,
                            2));
                    holder.image_5.setVisibility(View.VISIBLE);
                    holder.image_5.setImageURI(Uri
                            .parse(FXConstant.URL_SOCIAL_PHOTO + images[3]));
                    holder.image_5.setOnClickListener(new ImageListener(images,
                            3));
                } else {
                    if (imNumb > 1) {
                        holder.image_2.setVisibility(View.VISIBLE);
                        holder.image_2.setImageURI(Uri
                                .parse(FXConstant.URL_SOCIAL_PHOTO + images[1]));
                        holder.image_2.setOnClickListener(new ImageListener(
                                images, 1));
                        if (imNumb > 2) {
                            holder.image_3.setVisibility(View.VISIBLE);
                            holder.image_3.setImageURI(Uri
                                    .parse(FXConstant.URL_SOCIAL_PHOTO
                                            + images[2]));
                            holder.image_3
                                    .setOnClickListener(new ImageListener(
                                            images, 2));
                            if (imNumb > 3) {
                                holder.image_4.setVisibility(View.VISIBLE);
                                holder.image_4.setImageURI(Uri
                                        .parse(FXConstant.URL_SOCIAL_PHOTO
                                                + images[3]));
                                holder.image_4
                                        .setOnClickListener(new ImageListener(
                                                images, 3));
                                if (imNumb > 4) {
                                    holder.image_5.setVisibility(View.VISIBLE);
                                    holder.image_5.setImageURI(Uri
                                            .parse(FXConstant.URL_SOCIAL_PHOTO
                                                    + images[4]));
                                    holder.image_5
                                            .setOnClickListener(new ImageListener(
                                                    images, 4));
                                    if (imNumb > 5) {
                                        holder.image_6
                                                .setVisibility(View.VISIBLE);
                                        holder.image_6
                                                .setImageURI(Uri
                                                        .parse(FXConstant.URL_SOCIAL_PHOTO
                                                                + images[5]));
                                        holder.image_6
                                                .setOnClickListener(new ImageListener(
                                                        images, 5));
                                        if (imNumb > 6) {
                                            holder.image_7
                                                    .setVisibility(View.VISIBLE);
                                            holder.image_7
                                                    .setImageURI(Uri
                                                            .parse(FXConstant.URL_SOCIAL_PHOTO
                                                                    + images[6]));
                                            holder.image_7
                                                    .setOnClickListener(new ImageListener(
                                                            images, 6));
                                            if (imNumb > 7) {
                                                holder.image_8
                                                        .setVisibility(View.VISIBLE);
                                                holder.image_8
                                                        .setImageURI(Uri
                                                                .parse(FXConstant.URL_SOCIAL_PHOTO
                                                                        + images[7]));
                                                holder.image_8
                                                        .setOnClickListener(new ImageListener(
                                                                images, 7));
                                                if (imNumb > 8) {
                                                    holder.image_9
                                                            .setVisibility(View.VISIBLE);
                                                    holder.image_9
                                                            .setImageURI(Uri
                                                                    .parse(FXConstant.URL_SOCIAL_PHOTO
                                                                            + images[8]));
                                                    holder.image_9
                                                            .setOnClickListener(new ImageListener(
                                                                    images, 8));

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            final View view_pop = holder.view_pop;
            final ImageView iv_temp = holder.iv_pop;
            final LinearLayout ll_goodmembers_temp = holder.ll_goodmenbers;

            // 显示位置
            if (location != null && !location.equals("0")) {
                holder.tv_location.setVisibility(View.VISIBLE);
                holder.tv_location.setText(location);
            }
            // 显示文章内容
            setUrlTextView(content, holder.tv_content);

            // 点赞评论数据
            final JSONArray goodArray = json.getJSONArray("good");
            final JSONArray commentArray = json.getJSONArray("comment");

            // 点赞

            setGoodTextClick(holder.tv_goodmenbers, goodArray
                    , ll_goodmembers_temp, view_pop, commentArray.size());

            boolean is_good_temp = true;
            for (int i = 0; i < goodArray.size(); i++) {
                JSONObject json_good = goodArray.getJSONObject(i);
                if (json_good.getString("userID").equals(myuserID)) {
                    is_good_temp = false;
                }
            }

            if (commentArray != null && commentArray.size() != 0) {
                holder.tv_commentmenbers.setVisibility(View.VISIBLE);
                setCommentTextClick(holder.tv_commentmenbers, commentArray,
                        view_pop, goodArray.size());

            }

            final boolean is_good = is_good_temp;



            String goodStr = "赞";
            if (is_good) {
                goodStr = "取消";
            }
            iv_temp.setTag(goodStr);

            final TextView tv_good_temp = holder.tv_goodmenbers;
            final TextView tv_commentmembers_temp = holder.tv_commentmenbers;
            // 显示时间
            holder.tv_time.setText(getTime(rel_time, MyApp.getApp().getTime()));


            iv_temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT);

                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(true);

                    View view = LayoutInflater.from(MyApp.getmContext()).inflate(R.layout.pop_item, null);
                    popupWindow.setContentView(view);

                    final LinearLayout ll_zan = (LinearLayout) view.findViewById(R.id.ll_zan);
                    final TextView tv_good = (TextView) view.findViewById(R.id.tv_good);
                    LinearLayout ll_pl = (LinearLayout) view.findViewById(R.id.ll_pl);

                    int width1 = view.getMeasuredWidth();
                    int[] location = new int[2];
                    iv_temp.getLocationOnScreen(location);
                    popupWindow.showAtLocation(iv_temp, Gravity.NO_GRAVITY,
                            (location[0] - width1) * 2 / 5, location[1]);
                    if (((String) iv_temp.getTag()).equals("赞")) {
                        tv_good.setText("取消");
                        iv_temp.setTag("取消");
                    } else {
                        tv_good.setText("赞");
                        iv_temp.setTag("赞");
                    }

                    ll_zan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((String) iv_temp.getTag()).equals("赞")) {
                                iv_temp.setTag("取消");
                                setGood(sID, tv_good_temp, goodArray
                                        , ll_goodmembers_temp, view_pop, commentArray.size());
                                popupWindow.dismiss();

                            } else {
                                iv_temp.setTag("赞");
                                cancelGood(sID, tv_good_temp, goodArray
                                        , ll_goodmembers_temp, view_pop, commentArray.size());
                                popupWindow.dismiss();
                            }
                        }

                    });

                    // 点击评论

                    ll_pl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();

                            showCommentEditText(sID, tv_commentmembers_temp
                                    , commentArray, view_pop, goodArray.size());

                        }
                    });
                }
            });


            return convertView;
        }
    }

    /**
     * 点赞
     */
    public void setGood(String sID, TextView tv_good, JSONArray jsons,
                        LinearLayout ll_goodmembers_temp, View view, int cSize) {

        // 即时改变当前UI
        JSONObject json = new JSONObject();
        json.put("userID", myuserID);
        jsons.add(json);
        setGoodTextClick(tv_good, jsons, ll_goodmembers_temp, view, cSize);
        // 更新后台
        List<Param> params = new ArrayList<>();
        params.add(new Param("sID", sID));

        params.add(new Param("userID", myuserID));

        OkHttpManager.getInstance().post(params, FXConstant.URL_SOCIAL_GOOD, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("code");
                if (code != 1000) {
                    Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void cancelGood(String sID, TextView tv_good, JSONArray jsons,
                           LinearLayout ll_goodmembers_temp, View view, int cSize) {
        // 即时改变当前UI
        for (int i = 0; i < jsons.size(); i++) {
            JSONObject json = jsons.getJSONObject(i);
            if (json.getString("userID").equals(myuserID)) {
                jsons.remove(i);
            }
        }
        setGoodTextClick(tv_good, jsons, ll_goodmembers_temp, view, cSize);
        // 更新后台
        List<Param> params = new ArrayList<>();
        params.add(new Param("sID", sID));

        params.add(new Param("userID", myuserID));

        OkHttpManager.getInstance().post(params, FXConstant.URL_SOCIAL_GOOD_CANCEL, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("code");
                if (code != 1000) {
                    Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setGoodTextClick(TextView mTextView2
            , JSONArray data, LinearLayout ll_goodmembers, View view_pop, int size) {
        if (data == null || data.size() == 0) {
            ll_goodmembers.setVisibility(View.GONE);
        } else {
            ll_goodmembers.setVisibility(View.VISIBLE);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        int start = 0;
        for (int i = 0; i < data.size(); i++) {
            JSONObject json_good = data.getJSONObject(i);
            String userID_temp = json_good.getString("userID");
            String nick = userID_temp;

            if (userID_temp.equals(myuserID)) {
                nick = myuserID;

            } else {
                nick = myuserID;
            }

            if (i != (data.size() - 1) && data.size() > 1) {
                ssb.append(nick + ",");
            } else {
                ssb.append(nick);
            }
            ssb.setSpan(new TextViewURLSpan(nick, userID_temp, 0)
                    , start
                    , start + nick.length()
                    , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = ssb.length();
        }
    }

    private class TextViewURLSpan extends ClickableSpan {
        private String userID;
        // 0是点赞里面的名字。1是评论里面的名字；2是评论中的删除
        private int type = 0;
        private TextView ctextView;
        private JSONArray cjsons;
        private View view;
        private int goodSize;
        private String scID;
        private int postion;

        public TextViewURLSpan(String nick, String userID, int postion,
                               String scID, int type, TextView ctextView, JSONArray cjsons,
                               View view, int goodSize) {
            this.userID = userID;
            this.type = type;
            this.ctextView = ctextView;
            this.cjsons = cjsons;
            this.view = view;
            this.goodSize = goodSize;
            this.scID = scID;
            this.postion = postion;
        }

        public TextViewURLSpan(String nick, String userID, int type) {
            this.userID = userID;
            this.type = type;

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (type != 2) {
                ds.setColor(
                        context.getResources().getColor(R.color.text_blue));

            }
            ds.setUnderlineText(false); // 去掉下划线
        }

        @Override
        public void onClick(final View widget) {

            if (widget instanceof TextView) {
                ((TextView) widget).setHighlightColor(context.getResources()
                        .getColor(android.R.color.darker_gray));
                new Handler().postDelayed(new Runnable() {

                    public void run() {

                        ((TextView) widget)
                                .setHighlightColor(context.getResources()
                                        .getColor(android.R.color.transparent));

                    }

                }, 1000);

            }

            if (type == 2) {
                showDeleteDialog(userID, postion, scID, type, ctextView, cjsons,
                        view, goodSize);
              //  Toast.makeText(context, "lalal", Toast.LENGTH_SHORT).show();

            } else {

//                context.startActivity(
//                        new Intent(context, SocialFriendActivity.class)
//                                .putExtra("friendID", userID));
                Toast.makeText(context, "SocialFriendActivity", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void showDeleteDialog(final String userID, final int postion,
                                  final String scID, final int type, final TextView ctextView,
                                  final JSONArray cjsons, final View view, final int goodSize) {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.fx_dialog_social_main);
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("复制");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(cjsons.getJSONObject(postion).getString("content")
                        .trim());

                //cmb.setPrimaryClip(ClipData clip);

                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("删除");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteComment(userID, postion, scID, type, ctextView, cjsons,
                        view, goodSize);

                dlg.cancel();
            }
        });

    }
    // 删除评论
    private void deleteComment(String userID, final int postion, String scID,
                               int type, TextView ctextView, final JSONArray cjsons, View view,
                               int goodSize) {

        if (scID == null) {
            scID = "LOCAL";
        }
        ;
        String tag = cjsons.getJSONObject(postion).getString("tag");
        if (tag == null) {
            tag = String.valueOf(System.currentTimeMillis());
        }
        // 更新UI
        cjsons.remove(postion);
        setCommentTextClick(ctextView, cjsons, view, goodSize);
        // 更新服务器
        // 更新后台
        List<Param> params = new ArrayList<>();
        params.add(new Param("scID", scID));
        params.add(new Param("tag", tag));
        // params.add(new Param("userID", myuserID));

        OkHttpManager.getInstance().post(params, FXConstant.URL_SOCIAL_DELETE_COMMENT, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("code");
                if (code != 1000) {
                    Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static class ViewHolder {
        SimpleDraweeView iv_avatar;
        // 昵称
        TextView tv_nick;
        // 时间
        TextView tv_time;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        LinearLayout ll_three;
        SimpleDraweeView image_1;
        SimpleDraweeView image_2;

        SimpleDraweeView image_3;
        SimpleDraweeView image_4;
        SimpleDraweeView image_5;
        SimpleDraweeView image_6;
        SimpleDraweeView image_8;
        SimpleDraweeView image_9;
        SimpleDraweeView image_7;
        // 动态内容
        TextView tv_content;

        TextView tv_location;

        // 点赞 和 评论
        ImageView iv_pop;
        LinearLayout ll_goodmenbers;
        TextView tv_goodmenbers;
        TextView tv_commentmenbers;
        View view_pop;

        // 删除
        TextView tv_delete;
    }

    class ImageListener implements View.OnClickListener {
        String[] images;
        int page;

        public ImageListener(String[] images, int page) {

            this.images = images;
            this.page = page;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(context, BigImageActivity.class);
            intent.putExtra("images", images);
            intent.putExtra("page", page);
            context.startActivity(intent);

        }

    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    private void setUrlTextView(String test_temp, TextView tv_content) {

        String test = test_temp;

        if ((test_temp != null) && (test_temp.contains("http://")
                || test_temp.contains("https://")
                || test_temp.contains("www."))) {
            int start = 0;
            while (test != null && !(test.startsWith("http://")
                    || test.startsWith("https://")
                    || test.startsWith("www."))) {

                test = test.substring(1);
                start++;

            }
            int end = 0;

            for (int i = 0; i < test.length(); i++) {
                char item = test.charAt(i);
                if (isChinese(item) || item == ' ') {

                    break;
                }
                end = i;

            }

            String result = (String) test_temp.substring(start,
                    start + end + 1);
            // 可以检验是否有效连接，但是影响效率
            // if(result!=nullcheckURL(result)){
            //
            // }
            if (result != null) {

                SpannableStringBuilder ssb = new SpannableStringBuilder();
                ssb.append(test_temp);

                ssb.setSpan(new ContentURLSpan(result), start, start + end + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv_content.setText(ssb);
                tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            }

        } else {
            tv_content.setText(test_temp);
        }

    }

    private class ContentURLSpan extends ClickableSpan {
        private String url;

        public ContentURLSpan(String url) {
            this.url = url;

        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(false); // 去掉下划线
        }

        @Override
        public void onClick(final View widget) {

            if (widget instanceof TextView) {
                ((TextView) widget).setHighlightColor(context.getResources()
                        .getColor(android.R.color.darker_gray));
                new Handler().postDelayed(new Runnable() {

                    public void run() {

                        ((TextView) widget)
                                .setHighlightColor(context.getResources()
                                        .getColor(android.R.color.transparent));

                    }

                }, 1000);

            }
            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            context.startActivity(intent);
//            context.startActivity(new Intent(context, MyWebViewActivity.class)
//                    .putExtra("url", url));

        }

    }

    @SuppressLint("SimpleDateFormat")
    private String getTime(String rel_time, String now_time) {
        String backStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(rel_time);
            d2 = format.parse(now_time);
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = (diff / (60 * 1000)) % 60;
            long diffHours = (diff / (60 * 60 * 1000)) / 24;
            Log.d("SocialMainAdapter----->", "diffHours:" + diffHours);
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays != 0) {
                if (diffDays < 30) {
                    if (1 < diffDays && diffDays < 2) {
                        backStr = "昨天";
                    } else if (2 < diffDays && diffDays < 3) {
                        backStr = "前天";
                    } else {
                        backStr = String.valueOf(diffDays) + "天前";
                    }
                } else {
                    backStr = "很久以前";
                }
            } else if (diffHours != 0) {
                backStr = String.valueOf(diffHours) + "小时前";
            } else if (diffMinutes != 0) {
                backStr = String.valueOf(diffMinutes) + "分钟前";
            } else {
                backStr = "刚刚";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("SocialMainAdapter", backStr);

        return backStr;
    }

    private void showPhotoDialog(final int index, final String sID) {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.fx_dialog_social_delete);
        TextView tv_cancel = (TextView) window.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        TextView tv_ok = (TextView) window.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                users.remove(index);
                notifyDataSetChanged();

                List<Param> params = new ArrayList<>();
                params.add(new Param("sID", sID));

                OkHttpManager.getInstance().post(params, FXConstant.URL_SOCIAL_DELETE, new OkHttpManager.HttpCallBack() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        int code = jsonObject.getIntValue("code");
                        if (code != 1000) {
                            Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
                    }
                });


                dlg.cancel();
            }
        });

    }

    /**
     * 显示发表评论的输入框
     *
     * @param sID
     * @param tv_comment
     * @param jsons
     * @param view
     * @param goodSize
     */
    public void showCommentEditText(final String sID, final TextView tv_comment,
                                    final JSONArray jsons, final View view, final int goodSize) {

        if (re_edittext == null || re_edittext.getVisibility() != View.VISIBLE) {

            re_edittext.setVisibility(View.VISIBLE);
            final EditText et_comment = (EditText) context.findViewById(R.id.et_comment);
            Button btn_send = (Button) context.findViewById(R.id.btn_send);

            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = et_comment.getText().toString().trim();
                    if (TextUtils.isEmpty(comment)) {
                        Toast.makeText(context, "请输入评论", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    submitComment(sID, comment, tv_comment, jsons, view,
                            goodSize);
                    et_comment.setText("");
                    hideCommentEditText();

                }
            });

        }
    }

    /**
     * 隐藏输入框
     */
    public void hideCommentEditText(){
        if (re_edittext != null && re_edittext.getVisibility() == View.VISIBLE) {
            re_edittext.setVisibility(View.GONE);
        }
    }

    /**
     *  提交评论
     *
     * @param sID
     * @param comment
     * @param tv_comment
     * @param jsons
     * @param view
     * @param goodSize
     */

    private void submitComment(String sID, String comment,
                               TextView tv_comment, JSONArray jsons, View view, int goodSize) {

        String tag = String.valueOf(SystemClock.currentThreadTimeMillis());
        Log.d("SocialMainAdapter", tag);
        // 即时改变当前UI
        JSONObject json = new JSONObject();
        json.put("user", myuserID);
        json.put("content", comment);
        // 本地标记，方便本地定位删除，服务器端用不到这个字段
        json.put("tag", tag);
        jsons.add(json);
        setCommentTextClick(tv_comment, jsons, view, goodSize);
        // 更新后台
        List<Param> params = new ArrayList<>();
        params.add(new Param("sID",sID));
        params.add(new Param("content",comment));
        params.add(new Param("userID",myuserID));
        params.add(new Param("tag",tag));
        OkHttpManager.getInstance().post(params, FXConstant.URL_SOCIAL_COMMENT, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
               int code = jsonObject.getIntValue("code");
                if (code != 1000) {
                    Toast.makeText(context, "服务器端响应失败...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(context, "服务器无响应...", Toast.LENGTH_SHORT).show();
            }
        });

    }
     // 设置评论

    private void setCommentTextClick(TextView tv_comment,
                                     JSONArray data, View view, int goodSize) {
        if (goodSize > 0 && data.size() > 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
        if (data.size() ==0) {
            tv_comment.setVisibility(View.GONE);
        } else {
            tv_comment.setVisibility(View.VISIBLE);
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int start = 0;
        for (int i = 0; i < data.size(); i++) {
            JSONObject json = data.getJSONObject(i);
            String userID_temp = json.getString("userID");
            String nick = userID_temp;
            if (userID_temp ==null || userID_temp.equals(myuserID)){
                nick = myuserID;
                userID_temp = myuserID;
            } else {
                nick = myuserID;
            }

            String content = json.getString("content");
            String scID = json.getString("scID");

            String content_0 = "";
            String content_1 = ": " + content;
            String content_2 = ": " + content + "\n";
            if (i == (data.size() - 1) || (data.size() == 1 && i == 0)) {
                ssb.append(nick + content_1);
                content_0 = content_1;
            } else {

                ssb.append(nick + content_2);
                content_0 = content_2;
            }

            try {
                ssb.setSpan(new TextViewURLSpan(nick, userID_temp, 1), start,
                        start + nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (userID_temp == null || userID_temp.equals(myuserID)) {

                ssb.setSpan(
                        new TextViewURLSpan(nick, userID_temp, i, scID, 2,
                                tv_comment, data, view, goodSize),
                        start, start + nick.length() + content_0.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            start = ssb.length();

        }

        tv_comment.setText(ssb);

//        tv_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "lalal", Toast.LENGTH_SHORT).show();
//            }
//        });
        tv_comment.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
