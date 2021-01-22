package cn.armory.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author gjq
 * @packagename cn.armory.common.utils
 * @date 2020/9/5
 * @describe 用于图片相关
 */
public class ImageUtils {

    public static void loadImage(String url, ImageView view) {
        loadImage(url, view, null);
    }

    //glide简单封装
    public static void loadImage(String url, ImageView view, RequestOptions options) {
        RequestManager requestManager = Glide.with(ACUtils.getApplicationContext());
        if (options == null) {
            requestManager
                    .asDrawable()
                    .load(url)
                    .into(view);
        } else {
            requestManager
                    .asDrawable()
                    .load(url)
                    .apply(options)
                    .into(view);
        }

    }

    //清理磁盘缓存
    public static void GuideClearDiskCache() {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(ACUtils.getApplicationContext()).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory() {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(ACUtils.getApplicationContext()).clearMemory();
    }

    /**
     * 保存图片,bitmap保存
     *
     * @param picUrl  图片的网络地址
     * @param path    保存目录
     * @param picName 图片名字
     */
    public static void savePicByBitmap(String picUrl, String path, String picName) {
        try {
            URL url = new URL(picUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // 连接超时时间
            conn.setConnectTimeout(15 * 1000);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                saveBitmapToSDCard(mBitmap, path, picName);
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 保存图片，字节流保存
     *
     * @param picUrl  图片的网络地址
     * @param path    保存目录
     * @param picName 图片名字
     */
    public static void saveImage(String picUrl, String path, String picName) {
        saveImageFile(getImageFromNetByUrl(picUrl), path, picName);
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    private static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            return readInputStream(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    //保存图片文件
    public static void saveImageFile(byte[] buffer, String imageFile, String fileName) {
        File dirFile = new File(imageFile);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(imageFile + fileName);
        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bos.write(buffer);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //获取图片文件位置
    public static String getImageFile(String path, String fileName) {
        try {
            File file = new File(path + fileName);
            if (file.exists()) {
                return path + fileName;
            } else {
                return "";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 保存bitamp到sd卡
     *
     * @param bitmap
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean saveBitmapToSDCard(Bitmap bitmap, String filePath, String fileName) {
        boolean flag = false;
        if (null != bitmap) {
            try {
                fileName = fileName + ".png";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File f = new File(filePath + fileName);
                if (f.exists()) {
                    f.delete();
                }
                BufferedOutputStream outputStream = new BufferedOutputStream(
                        new FileOutputStream(f));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                flag = true;
            } catch (FileNotFoundException e) {
                flag = false;
            } catch (IOException e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * bitmap转byte[]
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]转bitmap
     *
     * @param bytes 字节数组
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 获取bitmap
     *
     * @param filePath  文件路径
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    public static Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {
        if (filePath == null || "".equals(filePath)) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算采样大小
     *
     * @param options   选项
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 采样大小
     */
    private static int calculateInSampleSize(final BitmapFactory.Options options, final int maxWidth, final int maxHeight) {
        if (maxWidth == 0 || maxHeight == 0) return 1;
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((height >>= 1) > maxHeight && (width >>= 1) > maxWidth) {
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @param recycle  是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(final Bitmap src, final String filePath, final Bitmap.CompressFormat format, final boolean recycle) {
        return save(src, FileUtils.getFileByPath(filePath), format, recycle);
    }

    /**
     * 保存图片
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(final Bitmap src, final File file, final Bitmap.CompressFormat format, final boolean recycle) {
        if (isEmptyBitmap(src) || !FileUtils.createOrExistsFile(file)) return false;
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }
}
