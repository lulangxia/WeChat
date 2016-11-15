package com.zjl.mywechat.socalfriend.view;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zjl.mywechat.R;
import com.zjl.mywechat.app.MyApp;
import com.zjl.mywechat.socalfriend.adapter.SocialMainAdapter;
import com.zjl.mywechat.socalfriend.modle.Param;
import com.zjl.mywechat.socalfriend.presenter.OkHttpManager;
import com.zjl.mywechat.stringvalue.FXConstant;
import com.zjl.mywechat.tools.PermissionsChecker;
import com.zjl.mywechat.widget.PermissionsActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zjl.mywechat.R.id.pull_refresh_list;


public class SocialMainActivity extends BaseActivity {

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private String mImageName;
    String userID;
    List<String> sIDs = new ArrayList<>();
    private PullToRefreshListView mPullToRefreshListView;
    private int page = 0;
    private ListView mActualListView;
    private List<JSONObject> articles = new ArrayList<>();
    private SocialMainAdapter mAdapter;


    private static final int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private PermissionsChecker mPermissionsChecker;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_social_main);
        super.onCreate(arg0);
        userID = this.getIntent().getStringExtra("userID");
        initFile();
        initView();

        mPermissionsChecker = new PermissionsChecker();
    }


    private void initView() {
        mPullToRefreshListView = (PullToRefreshListView) findViewById(pull_refresh_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        SocialMainActivity.this,
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                if (mPullToRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    page = 0;

                } else if (mPullToRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    page++;

                }

                getData(page);
            }
        });
        mActualListView = mPullToRefreshListView.getRefreshableView();


        mAdapter = new SocialMainAdapter(SocialMainActivity.this, articles);

        mActualListView.setAdapter(mAdapter);
        ImageView iv_camera = (ImageView) findViewById(R.id.iv_camera);

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });
    }

    private void initFile() {
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
                        Uri.fromFile(new File("/sdcard/bizchat/", mImageName)));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                dlg.cancel();
            }
        });

        TextView tv_xingce = (TextView) window.findViewById(R.id.tv_content2);

        tv_xingce.setText("相册");
        tv_xingce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageName = getNowTime() + ".jpg";
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
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
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    path = "/sdcard/bizchat/" + mImageName;
                    break;
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        Uri imageFilePath = data.getData();
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageFilePath, proj, null, null, null);

                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToNext();
                        // 获取图片真实地址
                        path = cursor.getString(column_index);
                        System.out.println(path);
                    }

                    break;
            }
            Intent intent = new Intent();
            intent.putExtra("imagePath", path);
            intent.setClass(SocialMainActivity.this, MomentsPublishActivity.class);
            startActivity(intent);
            super.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }

    }

    private void getData(final int page_num) {

        List<Param> params = new ArrayList<>();
        params.add(new Param("userID", userID));
        params.add(new Param("num", page_num + ""));
        OkHttpManager.getInstance().post(params, FXConstant.URL_SOCIAL, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                mPullToRefreshListView.onRefreshComplete();
                int code = jsonObject.getInteger("code");
                if (code == 1000) {
                    JSONArray users_temp = jsonObject.getJSONArray("data");
                    String time = jsonObject.getString("time");
                    MyApp.getApp().setTime(time);
                    if (page_num == 0) {

                        //datas = users_temp;
                        articles.clear();
                        sIDs.clear();
                        for (int i = 0; i < users_temp.size(); i++) {
                            JSONObject json = users_temp.getJSONObject(i);
                            String sID = json.getString("sID");

                            sIDs.add(sID);
                            articles.add(json);
                        }

                    } else {

                        Map<String, JSONObject> map = new HashMap<String, JSONObject>();

                        for (int i = 0; i < users_temp.size(); i++) {
                            JSONObject json = users_temp.getJSONObject(i);
                            String sID = json.getString("sID");
                            if (!sIDs.contains(sID)) {
                                sIDs.add(sID);
                                articles.add(json);
                            }
                        }

                    }
//					adapter = new SocialMainAdapter(SocialMainActivity.this,
//							datas, time);
                    //	actualListView.setAdapter(adapter);

                    mAdapter.notifyDataSetChanged();
                    // ACache.get(getActivity()).put("last_login", users);

                } else {
                    // ToastUtil.showMessage("服务器出错...");
                }
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        getData(0);

        // 缺少权限时, 进入权限配置页面

        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);


    }

}
