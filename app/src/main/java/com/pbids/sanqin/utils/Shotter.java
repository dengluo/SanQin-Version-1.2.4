package com.pbids.sanqin.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;

public class Shotter {

    private final SoftReference<Context> mRefContext;
    private ImageReader mImageReader;

    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private String mLocalUrl = "";

    private OnShotListener mOnShotListener;

    private WindowManager mWindowManager;
    private DisplayMetrics metrics ;
    //截屏工具类

    public Shotter(Context context, Intent data) {
        this.mRefContext = new SoftReference<>(context);
        //取屏幕信息
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, data);
            createImageReader();
           /* mImageReader = ImageReader.newInstance(
                    getScreenWidth(),
                    getScreenHeight(),
                    PixelFormat.RGB_565,// a pixel两节省一些内存 个2个字节 此处RGB_565 必须和下面 buffer处理一致的格式
                    1);*/
        }
    }

    @TargetApi( Build.VERSION_CODES.LOLLIPOP)
    private void createImageReader() {
        mImageReader = ImageReader.newInstance(getScreenWidth(), getScreenHeight(), PixelFormat.RGBA_8888, 1);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                getScreenWidth(),
                getScreenHeight(),
                getScreenDpi(),
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);

    }

    public void startScreenShot(OnShotListener onShotListener, String loc_url) {
        mLocalUrl = loc_url;
        startScreenShot(onShotListener);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void startScreenShot(OnShotListener onShotListener) {
        mOnShotListener = onShotListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            virtualDisplay();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Image image = mImageReader.acquireLatestImage();
                                        AsyncTaskCompat.executeParallel(new SaveTask(), image);
                                    }
                                },
                    300);

        }

    }

    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Bitmap doInBackground(Image... params) {

            if (params == null || params.length < 1 || params[0] == null) {

                return null;
            }

            Image image = params[0];

            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            //Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.RGB_565);
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
            File fileImage = null;
            if (bitmap != null) {
                try {

                    /*if (TextUtils.isEmpty(mLocalUrl)) {
                        mLocalUrl = getContext().getExternalFilesDir("screenshot").getAbsoluteFile()
                                +
                                "/"
                                +
                                SystemClock.currentThreadTimeMillis() + ".png";
                    }
                    fileImage = new File(mLocalUrl);*/

                    String fileName = "";
                    if (TextUtils.isEmpty(mLocalUrl)) {
                        //自动生成目录
                        File appDir = new File(Environment.getExternalStorageDirectory(), "DCIM"+ File.separator + "Screenshots" + File.separator);
                        if (!appDir.exists()) {
                            appDir.mkdir();
                        }
                        fileName = System.currentTimeMillis() + ".png";
                        fileImage = new File(appDir, fileName);
                        if (!fileImage.exists()) {
                            fileImage.createNewFile();
                        }
                    }else{
                        //指定目录
                        new File(mLocalUrl);
                        //取文件名
                        fileName =  mLocalUrl.substring(mLocalUrl.lastIndexOf("/") + 1, mLocalUrl.length());
                    }

                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    }
                    // 其次把文件插入到系统图库
                    MediaStore.Images.Media.insertImage(getContext().getContentResolver(), fileImage.getAbsolutePath(), fileName, null);
                    // 最后通知图库更新
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(fileImage.getPath()))));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileImage = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    fileImage = null;
                }
            }

            if (fileImage != null) {
                return bitmap;
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }

            if (mVirtualDisplay != null) {
                mVirtualDisplay.release();
            }

            if (mOnShotListener != null) {
                mOnShotListener.onFinish();
            }

        }
    }

    @TargetApi( Build.VERSION_CODES.LOLLIPOP)
    private MediaProjectionManager getMediaProjectionManager() {
        return (MediaProjectionManager) getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private Context getContext() {
        return mRefContext.get();
    }


    private int getScreenWidth() {
        return metrics.widthPixels;
        //return Resources.getSystem().getDisplayMetrics().widthPixels;

    }

    private int getScreenHeight() {
        return metrics.heightPixels;
        //return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    private int getScreenDpi() {
        //Resources.getSystem().getDisplayMetrics().densityDpi,
        return metrics.densityDpi;
    }

    // a  call back listener
    public interface OnShotListener {
        void onFinish();
    }
}
