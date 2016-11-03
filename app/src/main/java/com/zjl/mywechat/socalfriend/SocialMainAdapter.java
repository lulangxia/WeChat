package com.zjl.mywechat.socalfriend;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zjl.mywechat.R;
import com.zjl.mywechat.tool.stringvalue.FXConstant;

import java.util.List;

/**
 * Created by dllo on 16/10/31.
 */

public class SocialMainAdapter extends BaseAdapter {

    private Activity context;
    private List<JSONObject> users;
    private LayoutInflater inflater;

    public SocialMainAdapter(Activity content1, List<JSONObject> articles) {
        this.context = content1;
        this.users = articles;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        Log.d("SocialMainAdapter", "users.size():" + users.size());
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
    public View getView(int position, View convertView, ViewGroup parent) {
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

                holder.tv_content = (TextView) convertView
                        .findViewById(R.id.tv_content);
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

            String nick = userID;
            String avatar = "";

            holder.tv_nick.setText(nick);
         //   holder.iv_avatar.setImageURI(Ur);

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

            // 显示文章内容
            setUrlTextView(content,holder.tv_content);

            return convertView;
        }
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
           // intent.setClass(context, BigImageActivity.class);
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

            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            context.startActivity(intent);
//            context.startActivity(new Intent(context, MyWebViewActivity.class)
//                    .putExtra("url", url));

        }

    }
}
