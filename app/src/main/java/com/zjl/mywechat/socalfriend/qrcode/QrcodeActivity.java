package com.zjl.mywechat.socalfriend.qrcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;

import cn.bingoogolapple.qrcode.core.QRCodeView;


public class QrcodeActivity extends BaseAty implements QRCodeView.Delegate {


    private QRCodeView mQRCodeView;

    @Override
    protected int setLayout() {
        return R.layout.activity_qrcode;
    }

    @Override
    protected void initView() {
        mQRCodeView = bindView(R.id.zbarview);
        // 设置扫描二维码的代理
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void initData() {
        // 开始识别
        mQRCodeView.startSpot();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 打开后置摄像头开始预览，但是并未开始识别
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    protected void onStop() {
        // 关闭摄像头预览，并且隐藏扫描框
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 销毁二维码扫描控件
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        // 获取震动服务
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // 震动事件：200毫秒
        vibrator.vibrate(200);
    }


    // 成功扫描出二维码，会生成一个连接，跳转到WebView里
    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i("QrcodeActivity", "result:" + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        vibrate();
        if (result.startsWith("http")) {
            // 跳转到浏览器加载网址
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
            startActivity(intent);
        }
        finish();
    }

    // 没有相机权限
    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("QrcodeActivity", "打开相机出错");
    }
}
