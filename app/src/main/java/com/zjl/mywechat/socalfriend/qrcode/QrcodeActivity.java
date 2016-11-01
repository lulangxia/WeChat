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
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void initData() {
        mQRCodeView.startSpot();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i("QrcodeActivity", "result:" + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        vibrate();
        if (result.startsWith("http")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("QrcodeActivity", "打开相机出错");
    }
}
