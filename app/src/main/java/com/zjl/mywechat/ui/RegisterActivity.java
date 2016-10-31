package com.zjl.mywechat.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.app.MyApp;
import com.zjl.mywechat.staticfinal.StringStatic;
import com.zjl.mywechat.widget.FXAlertDialog;
import com.zjl.mywechat.staticfinal.FXConstant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends BaseAty {

    private Toolbar mToolbar;
    private EditText mUsername;
    private EditText mPassword;
    private Button mRegister;
    private ProgressDialog mDialog;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private ImageView mShowimg;
    private String imageName = "false";

    @Override
    protected int setLayout() {
        return R.layout.activity_register;

    }

    @Override
    protected void initView() {
        mToolbar = bindView(R.id.toolbar_register);
        mUsername = bindView(R.id.et_usertel);
        mPassword = bindView(R.id.et_password);
        mRegister = bindView(R.id.btn_register);
        mShowimg = bindView(R.id.iv_photo);
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("填写手机号");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.toolback);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUsername.addTextChangedListener(new TextChange());
        mPassword.addTextChangedListener(new TextChange());


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        mShowimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamera();
            }
        });

    }

    public void register() {

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("注册中，请稍后...");
        mDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //注册失败会抛出HyphenateException
                try {
                    EMClient.getInstance().createAccount(mUsername.getText().toString().trim(), mPassword.getText().toString().trim());//同步方法
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!RegisterActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            Log.d("MainActivity", "注册成功");
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!RegisterActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            /**
                             * 关于错误码可以参考官方api详细说明
                             * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                             */
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            Log.d("register", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(RegisterActivity.this, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                    Toast.makeText(RegisterActivity.this, "用户已存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(RegisterActivity.this, "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(RegisterActivity.this, "服务器未知错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(RegisterActivity.this, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this, "ml_sign_up_failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.putExtra(StringStatic.USERNAME, mUsername.getText().toString());
        intent.putExtra(StringStatic.PASSWORD, mPassword.getText().toString());
        Log.d("RegisterActivity", "mPassword.getText():" + mUsername.getText().toString());
        setResult(StringStatic.REQUESTCODE, intent);
        mDialog.dismiss();
        finish();
    }

    // 拍照部分
    private void showCamera() {

        List<String> items = new ArrayList<String>();
        items.add("拍照");
        items.add("相册");
        FXAlertDialog fxAlertDialog = new FXAlertDialog(RegisterActivity.this, null, items);
        fxAlertDialog.init(new FXAlertDialog.OnItemClickListner() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        Log.d("RegisterActivity", "aaa222");
                        imageName = getNowTime() + ".png";
                        if (ContextCompat.checkSelfPermission(MyApp.getmContext(),
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) MyApp.getmContext(),
                                    new String[]{Manifest.permission.CAMERA},
                                    1);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 指定调用相机拍照后照片的储存路径
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(FXConstant.DIR_AVATAR, imageName)));
                            startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                        }
                        break;
                    case 1:
                        Log.d("RegisterActivity", "aaa11");
                        imageName = getNowTime() + ".png";
                        Intent intent2 = new Intent(Intent.ACTION_PICK, null);
                        intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent2, PHOTO_REQUEST_GALLERY);
                        break;
                }
            }
        });

    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAGGG", "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    Log.d("RegisterActivity", "aaabb");
                    startPhotoZoom(Uri.fromFile(new File(FXConstant.DIR_AVATAR, imageName)), 480);
                    break;

                case PHOTO_REQUEST_GALLERY:
                    Log.d("RegisterActivity", "aaacc");
                    if (data != null)
                        startPhotoZoom(data.getData(), 480);
                    break;

                case PHOTO_REQUEST_CUT:

                    Log.d("RegisterActivity", "aaa");
                    Bitmap bitmap = BitmapFactory.decodeFile(FXConstant.DIR_AVATAR
                            + imageName);
                    mShowimg.setImageBitmap(bitmap);

                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);

        } else if (resultCode == 0 && requestCode == PHOTO_REQUEST_CUT) {
            Log.d("RegisterActivity", "aaa");
            Bitmap bitmap = BitmapFactory.decodeFile(FXConstant.DIR_AVATAR
                    + imageName);
            mShowimg.setImageBitmap(bitmap);
        }
    }

    private void startPhotoZoom(Uri uri1, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);
        Log.d("RegisterActivity", "aaaaaa");
        File file = new File(FXConstant.DIR_AVATAR);
        if (!file.exists()) {
            file.mkdir();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(FXConstant.DIR_AVATAR, imageName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection


        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {

            boolean Sign1 = mUsername.getText().length() > 0;
            boolean Sign2 = mPassword.getText().length() > 0;


            if (Sign1 & Sign2) {

                mRegister.setEnabled(true);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {

                mRegister.setEnabled(false);
            }
        }


    }
}