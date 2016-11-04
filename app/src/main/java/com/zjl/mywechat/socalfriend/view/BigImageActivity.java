package com.zjl.mywechat.socalfriend.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zjl.mywechat.R;

public class BigImageActivity extends AppCompatActivity {

    private ImageCycleView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        mAdView = (ImageCycleView) this.findViewById(R.id.ad_view);
        String[] images = getIntent().getStringArrayExtra("images");

        int page = getIntent().getIntExtra("page",0);

        mAdView.setImageResources(images,page,mImageCycleViewListener);

    }
    private ImageCycleView.ImageCycleViewListener mImageCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            Glide.with(BigImageActivity.this)
                    .load(imageURL).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        }

        @Override
        public void onImageClick(int position, View imageView) {
        }
    };
}
