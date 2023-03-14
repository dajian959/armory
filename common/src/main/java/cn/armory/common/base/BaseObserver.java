package cn.armory.common.base;

import com.google.gson.JsonParseException;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import cn.armory.common.http.HttpManager;
import io.reactivex.rxjava3.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class BaseObserver<T> extends DisposableObserver<Result<T>> {
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1008;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1007;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1006;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1005;
    /**
     * 其他所有情况
     */
    public static final int NOT_TRUE_OVER = 1004;

    public BaseObserver() {
    }

    @Override
    protected void onStart() {
        if (HttpManager.count.get() == 0) {
            LiveEventBus.get(BaseEvent.IS_SHOW_LOADING, Boolean.class).post(true);
        }
        HttpManager.count.incrementAndGet();
    }

    @Override
    public void onNext(Result<T> o) {
        int count = HttpManager.count.decrementAndGet();
        if (count == 0) {
            LiveEventBus.get(BaseEvent.IS_SHOW_LOADING, Boolean.class).post(false);
        }
        if (o.getCode() == HttpManager.code) {
            onSuccess(o);
        } else {
            onError(o.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        int count = HttpManager.count.decrementAndGet();
        if (count == 0) {
            LiveEventBus.get(BaseEvent.IS_SHOW_LOADING, Boolean.class).post(false);
        }
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK, "");
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR, "");
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT, "");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR, "");
            e.printStackTrace();
        } else {
            if (e != null) {
                e.printStackTrace();
                onException(NOT_TRUE_OVER, e.getMessage());
            } else {
                onError("未知错误");
            }
        }
    }

    private void onException(int unknownError, String message) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError("连接错误");
                break;
            case CONNECT_TIMEOUT:
                onError("连接超时");
                break;
            case BAD_NETWORK:
                onError("网络超时");
                break;
            case PARSE_ERROR:
                onError("数据解析失败");
                break;
            //非true的所有情况
            case NOT_TRUE_OVER:
                onError(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(Result<T> o);

    public abstract void onError(String msg);

}