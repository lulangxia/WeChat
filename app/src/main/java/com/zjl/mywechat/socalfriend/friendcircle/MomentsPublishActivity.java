package com.zjl.mywechat.socalfriend.friendcircle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zjl.mywechat.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MomentsPublishActivity extends BaseActivity implements View.OnClickListener {
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private String imagePath = null;
    private ImageView mBack;
    private Button mSned;
    private EditText mContent;
    private List<Uri> lists = new ArrayList<>();
    private GridView mGridView;
    private TextView mLocation;
    private TextView mCancel;
    private String mylocation;
    private String mImageName;
    private String tempcoor = "gcj02";
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private LocationClient mLocationClient = null;

    private BDLocationListener myListener = new MyLocationListener();
    private LinearLayout mLllocation;
    private ImageAdapter mAdapter;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_moments_publish);
        super.onCreate(arg0);
        initView();
        imagePath = this.getIntent().getStringExtra("imagePath");

        Log.d("MomentsPublishActivity", imagePath);
        if (TextUtils.isEmpty(imagePath)) {
            finish();
            return;
        }

        Uri uri_temp = Uri.fromFile(new File(imagePath));

        // 第一张图片要特别注意一下，是传过来的...
        getTwoImage(uri_temp, true);
        lists.add(uri_temp);
        initDate();
        // 位置相关
        mLocationClient = new LocationClient(this); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
    }
    @SuppressLint("SdCardPath")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String path = null;
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    path = "/sdcard/bizchat/" + mImageName;
                    System.out.println(path);
                    break;
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        Uri imageFilePath = data.getData();
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageFilePath, proj, null, null, null);
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToNext();
                        path = cursor.getString(column_index);
                        System.out.println(path);
                    }

                    break;
            }
            Log.d("MomentsPublishActivity", path);
            Log.d("MomentsPublishActivity", "lala");
            getTwoImage(Uri.fromFile(new File(path)), false);
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.iv_back);
        mSned = (Button) findViewById(R.id.btn_send);
        mContent = (EditText) findViewById(R.id.et_content);
        mGridView = (GridView) findViewById(R.id.gridview);
        mLocation = (TextView) findViewById(R.id.tv_location);
        mCancel = (TextView) findViewById(R.id.tv_cancel);
        mLllocation = (LinearLayout) findViewById(R.id.ll_location);

        mLllocation.setOnClickListener(this);
        mSned.setOnClickListener(this);
    }
    @SuppressLint("SdCardPath")
    private void getTwoImage(Uri uri_temp, boolean is_first) {
        if (uri_temp == null) {
            Toast.makeText(this, "添加图片失败，请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        String imageUrl = uri_temp.getPath();
        String imageName_temp = imageUrl
                .substring(imageUrl.lastIndexOf("/") + 1);
        // 生成大图
        save(imageUrl, 200, "big_" + imageName_temp);
        // 生成小图
        save(imageUrl, 60, imageName_temp);
        Log.e("imageUrl---->>>>", imageUrl);
        Log.e("imageName_temp---->>>>", imageName_temp);
        if ((new File("/sdcard/bizchat/" + imageName_temp)).exists()&&
                (new File("/sdcard/bizchat/" + "big_" + imageName_temp))
                .exists()) {
            if (!is_first) {
                Toast.makeText(this, "成功聊", Toast.LENGTH_SHORT).show();
                lists.add(uri_temp);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "添加图片失败，请重试lllll", Toast.LENGTH_SHORT).show();

        }
    }
    private void initDate() {
        mAdapter = new ImageAdapter(this, lists);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lists.size() < 9 && position == lists.size()) {
                    showPhotoDialog();
                } else {
                    checkDialog(position);
                }
            }
        });

        // 获取位置

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

                mImageName = getNowTime() + ".jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File("/sdcard/bizchat/", mImageName)));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                dlg.cancel();
            }
        });

        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("相册");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getNowTime();
                mImageName = getNowTime() + ".jpg";
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

                dlg.cancel();
            }
        });

    }
    private void checkDialog(final int position) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.fx_dialog_social_main);
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("看大图");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 用于显示用户的数据
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(lists.get(position), "image/*");
                startActivity(it);
                dlg.cancel();
            }
        });

        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("删除");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lists.remove(position);
                mAdapter.notifyDataSetChanged();
                dlg.cancel();
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_location:

                InitLocation();
                mLocationClient.start();
                mLocation.setText("正在获取位置...");
                break;
            case R.id.btn_send:
                String content = mContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(this, "请输入文字内容....", Toast.LENGTH_SHORT).show();
                    return;
                }
                // send(content);
        }
    }
    private void InitLocation() {

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);
        // 返回的定位结果是百度经纬度，默认值gcj02
        option.setCoorType(tempcoor);
        int span = 1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String str_addr = bdLocation.getAddrStr();
            if (!TextUtils.isEmpty(str_addr)) {
                mLocationClient.stop();
                mLocation.setText(str_addr);
                mCancel.setVisibility(View.VISIBLE);
                mylocation = str_addr;
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLocation.setText("所在位置");
                        mCancel.setVisibility(View.GONE);
                        mylocation = "";
                    }
                });
            }
        }
    }
    private void save(String path, int size, String saveName) {

        try {
            // File f = new File(path);

            Bitmap bm = PictureUtil.getSmallBitmap(path);
            int degree = readPictureDegree(path);

            if (degree != 0) {// 旋转照片角度
                bm = rotateBitmap(bm, degree);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            FileOutputStream fos = new FileOutputStream(new File(
                    PictureUtil.getAlbumDir(), saveName));

            int options = 100;
            // 如果大于80kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / 1024 > size && options > 10) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 30;
            }

            fos.write(baos.toByteArray());
            fos.close();
            baos.close();
            // bm.compress(Bitmap.CompressFormat.JPEG, 70, fos);

            // Toast.makeText(this, "Compress OK!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

        }

    }
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
