package com.pbids.sanqin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import Decoder.BASE64Encoder;

/**
 * Created by pbids903 on 2017/12/12.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:32
 * @desscribe 类描述:文件相关操作
 * @remark 备注:
 * @see
 */
public class FileUtil {

    public static final String PATH_DOWNLOAD =Environment.getExternalStorageDirectory()+
            File.separator+"sanqin"+File.separator+"crash"+File.separator+"download";
    public static final String PATH_IMAGES =Environment.getExternalStorageDirectory()+
            File.separator+"sanqin"+File.separator+"images";
    public static final String PATH_ERROR =Environment.getExternalStorageDirectory()+
            File.separator+"sanqin"+File.separator+"crash"+File.separator+"error";
    public static final String PATH_CRASH =Environment.getExternalStorageDirectory()+
            File.separator+"sanqin"+File.separator+"crash";

    public File getParentFile(Context context){
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"sanqin");
//        File file = context.getExternalFilesDir("");
        if (!file.exists()){
            //要点！
            file.mkdirs();
        }
        return file;
    }

    public static File getChildFileDir(Context context,String fileFir){
        File file = new File(Environment.getExternalStorageDirectory()
                +File.separator+"sanqin"+File.separator+fileFir);
//        File file = context.getExternalFilesDir(fileFir);
        if (!file.exists()){
            //要点！
            file.mkdirs();
        }
        return file;
    }

    public static File getFile(String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    public static File getFile(Context context,String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    public static String getDownLoadPath(Context context){
        return getChildFileDir(context,"download").getAbsolutePath();
    }

    /**
     * 将文件转成base64 字符串
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
//        return Base64.encodeToString(buffer,Base64.NO_WRAP);
        return new BASE64Encoder().encodeBuffer(buffer);
    }
    /**
     * 将base64字符解码保存文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code,String targetPath) throws Exception {
//        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        byte[] buffer = Base64.decode(base64Code,Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }
    /**
     * 将base64字符保存文本文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void toFile(String base64Code,String targetPath) throws Exception {
        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    //将bitmap转化为png格式
    public static File saveMyBitmap(Bitmap mBitmap){
        File storageDir = getFile(PATH_IMAGES);
        File file = null;
        try {
            file = File.createTempFile(
                    "img_"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            FileOutputStream out=new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  file;
    }

}
