package com.zjl.mywechat.socalfriend;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class PictureUtil {

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	/**
	 * 根据路径获得图片并压缩返回bitmap
	 * 
	 * @param filePath	
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {

		//实现步骤：

//		第一步：BitmapFactory.Option
//
//		设置 inJustDecodeBounds为true
//
//		第二步：BitmapFactory.decodeFile(path,option)方法
//
//		解码图片路径为一个位图。如果指定的文件名是空的,或者不能解码到一个位图,函数将返回null[空值]。
//
//		获取到outHeight(图片原始高度)和 outWidth(图片的原始宽度)
//
//		第三步：计算缩放比例，也可以不计算，直接给它设定一个值。
//
//		options.inSampleSize = "你的缩放倍数";
//
//		如果是2就是高度和宽度都是原始的一半。
//
//		第四步：设置options.inJustDecodeBounds = false;
//
//		重新读出图片
//
//				bitmap = BitmapFactory.decodeFile(path, options);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	@SuppressLint("SdCardPath")
	public static File getAlbumDir() {
		File dir = new File("/mnt/sdcard/bizchat/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
}
