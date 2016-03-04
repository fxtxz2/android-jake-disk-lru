package com.zyl.androidjakedisklru;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;
import com.zyl.filemd5utils.MD5Utils;

import java.io.File;
import java.io.IOException;

/**
 * https://github.com/JakeWharton/DiskLruCache
 * Jake的磁盘Lru缓存
 * Created by zyl on 15/12/29.
 */
public class JakeDiskLruCache {
    /**
     * 缓存大小
     */
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    /**
     * 缓存目录名
     */
    private static final String CACHE_DIR = "cacheDir";
    /**
     * 缓存
     */
    private DiskLruCache diskLruCache;
    /**
     * 缓存目录对象
     */
    private File cacheDir;

    private JakeDiskLruCache(){}
    private static class JakeDiskLruCacheHolder{
        private static JakeDiskLruCache INSTANCE = new JakeDiskLruCache();
    }

    /**
     * 获取单例
     * @return
     */
    public static JakeDiskLruCache getInstance() {
        return JakeDiskLruCacheHolder.INSTANCE;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        cacheDir = getDiskCacheDir(context.getApplicationContext(), CACHE_DIR);
//        if (!cacheDir.exists()){
//            cacheDir.mkdirs();
//        }
        try {
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context.getApplicationContext()), 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存数据
     * @param url
     * @param value
     */
    public void put(String url, String value){
        try {
            String key = MD5Utils.hashKeyForDisk(url);
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            editor.set(0, value);
            editor.commit();
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据
     * @param url
     * @return
     */
    public String get(String url){
        try {
            String key = MD5Utils.hashKeyForDisk(url);
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null){
                return snapshot.getString(0);
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 清除并关闭
     */
    public void clear(){
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得缓存目录对象
     * @return
     */
    public File getCacheDir() {
        return cacheDir;
    }
    /**
     * 该方法会判断当前sd卡是否存在，然后选择缓存地址
     *
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null){
                cachePath = context.getExternalCacheDir().getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
    /**
     * 获得应用version号码
     *
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
