[![bintray](https://img.shields.io/bintray/v/zyl/maven/android-jake-disk-lru.svg)](https://bintray.com/zyl/maven/android-jake-disk-lru/_latestVersion)
# android-jake-disk-lru
[Jake的磁盘Lru缓存](https://github.com/JakeWharton/DiskLruCache)工具类,现在只支持String类型,已经实现了单列模式(内部静态实例)。

# 使用方法
## 初始化
```Java
import com.zyl.androidjakedisklru.JakeDiskLruCache;

public class Application{
  @Override
  public void onCreate() {
    // 初始化任务大厅数据
    JakeDiskLruCache.getInstance().init(getApplicationContext());
  }
}
```
## 保存数据
```Java
JakeDiskLruCache.getInstance().put(key, data);
```
String key:表示保存在缓存中的key；  
String data:表示保存的String

## 获取数据
```Java
String data = JakeDiskLruCache.getInstance().get(key);
```
String data:获取到的数据；  
String key:该String保存在缓存中的key

## 获取缓存目录
```Java
String dir = JakeDiskLruCache.getInstance().getCacheDir()
```
String dir:返回缓存保存目录

## 清除并关闭缓存
```Java
JakeDiskLruCache.getInstance().clear();
```

# Gradle引入
`compile 'com.zyl.androidjakedisklru:androidjakedisklru:0.0.2'`
