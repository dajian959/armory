package cn.armory.common.http;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import cn.armory.common.base.BaseObserver;
import cn.armory.common.http.cookie.CookieJarImpl;
import cn.armory.common.http.cookie.PersistentCookieStore;
import cn.armory.common.http.interceptor.BaseInterceptor;
import cn.armory.common.http.interceptor.CacheInterceptor;
import cn.armory.common.http.interceptor.Level;
import cn.armory.common.http.interceptor.LoggingInterceptor;
import cn.armory.common.utils.ACUtils;
import cn.armory.common.utils.HttpsUtils;
import cn.armory.common.utils.Logger;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * RetrofitClient封装单例类, 实现网络请求
 */
public class HttpManager {
    //原子计数器
    public static AtomicInteger count = new AtomicInteger(0);
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    //服务端根路径
    public static String baseUrl = "";
    public static int code = 0;
    private static Retrofit retrofit;
    private File httpCacheDirectory;
    private LifecycleOwner lifecycleOwner;

    /**
     * 请在application的onCreate()调用此方法
     *
     * @param url         全局url
     * @param successCode 网络请求返回的状态码---正确拿到数据的状态码
     */
    public static void init(String url, int successCode) {
        baseUrl = url;
        code = successCode;
    }

    private static class SingletonHolder {
        private static final HttpManager MANAGER = new HttpManager();
    }

    public static HttpManager getInstance() {
        return SingletonHolder.MANAGER;
    }


    private HttpManager() {
        this(baseUrl, null, null);
    }

    private HttpManager(String url, Map<String, String> headers, Cache cache) {
        Context mContext = ACUtils.getApplicationContext();
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            Logger.e("Could not create http cache", e);
        }
        boolean debug = ACUtils.isAppDebug(mContext);
        String version = ACUtils.getVersionName(mContext);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CacheInterceptor(mContext))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(debug) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .addHeader("version", version) // 添加打印头, 注意 key 和 value 都不能是中文
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }


    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T getService(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    /**
     * 普通的网络请求
     *
     * @param observable
     * @param observer
     */
    public <T> void request(Observable<T> observable, BaseObserver<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
                .subscribe((Observer<? super T>) observer);
    }

    /**
     * 设置网络生命周期对象
     *
     * @param lifecycleOwner
     */
    public void bindLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * 解绑，防止异常情况下内存泄漏
     *
     * @param lifecycleOwner
     */
    public void unbindLifecycleOwner(LifecycleOwner lifecycleOwner) {
        if (this.lifecycleOwner == lifecycleOwner) {
            this.lifecycleOwner = null;
        }
    }
}