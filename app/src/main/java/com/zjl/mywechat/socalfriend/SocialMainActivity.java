package com.zjl.mywechat.socalfriend;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjl.mywechat.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SocialMainActivity extends BaseActivity{

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private String mImageName;
    String userID ;
    List<String> sIDs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_social_main);
        super.onCreate(arg0);
        userID = this.getIntent().getStringExtra("userID");
        initFile();
        initView();
    }


    private void initView() {

        ImageView iv_camera = (ImageView) findViewById(R.id.iv_camera);
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });
    }

    private void initFile(){
        File dir = new File("/sdcard/bizchat");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    private void showPhotoDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.fx_dialog_social_main);
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("拍照");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                getNowTime();
                mImageName = getNowTime() + ".jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File("/sdcard/bizchat/",mImageName)));
                startActivityForResult(intent,PHOTO_REQUEST_TAKEPHOTO);
                dlg.cancel();
            }
        });

        TextView tv_xingce = (TextView) window.findViewById(R.id.tv_content2);

        tv_xingce.setText("相册");
        tv_xingce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageName = getNowTime() + ".jpg";
                Intent intent = new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
                dlg.cancel();

            }
        });
    }
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
    //data为回传的Intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String path = null;
            switch (requestCode){
                case PHOTO_REQUEST_TAKEPHOTO:
                    path = "/sdcard/bizchat/" + mImageName;
                    break;
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        Uri imageFilePath = data.getData();
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageFilePath,proj,null,null,null);

                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToNext();
                        // 获取图片真实地址
                        path = cursor.getString(column_index);
                        System.out.println(path);
                    }

                    break;
            }
            Intent intent = new Intent();
            intent.putExtra("imagePath",path);
            intent.setClass(SocialMainActivity.this,MomentsPublishActivity.class);
            startActivity(intent);

            super.onActivityResult(requestCode,resultCode,data);
        }

    }
}
