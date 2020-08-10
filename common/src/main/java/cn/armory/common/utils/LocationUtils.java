package cn.armory.common.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;


/**
 * 基于高德地图的定位工具类
 * 在使用前请参考 "https://lbs.amap.com/api/android-location-sdk/gettingstarted"
 */

public class LocationUtils {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private Handler mHandler;
    public int successCode = 123;        //成功回调码（默认值）
    public int failCode = 500;           //失败回调码（默认值）

    public LocationUtils(Handler handler) {
        mHandler = handler;
        initLocation();
    }

    public LocationUtils(Handler handler, int successCode, int failCode) {
        mHandler = handler;
        this.successCode = successCode;
        this.failCode = failCode;
        initLocation();
    }

    /**
     * @param option   为null时使用默认
     * @param listener 为null时使用默认
     */
    public LocationUtils(AMapLocationClientOption option, AMapLocationListener listener) {
        initLocation(option, listener);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            if (null != location) {
                //解析定位结果
                if (location.getErrorCode() == 0) {
                    bundle.putDouble("latitude", location.getLatitude());
                    bundle.putDouble("longitude", location.getLongitude());
                    bundle.putString("address", location.getAddress());
                    bundle.putString("city", location.getCity());
                    bundle.putString("district", location.getDistrict());
                    bundle.putString("province", location.getProvince());
                }
                message.what = successCode;
                message.setData(bundle);
            } else {
                //定位失败，location is null
                message.what = failCode;
            }
            mHandler.handleMessage(message);
        }
    };

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(ACUtils.getApplicationContext());
        //设置定位参数
        mLocationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        mLocationClient.setLocationListener(locationListener);
    }

    /**
     * 初始化定位
     */
    private void initLocation(AMapLocationClientOption option, AMapLocationListener listener) {
        //初始化client
        mLocationClient = new AMapLocationClient(ACUtils.getApplicationContext());
        //设置定位参数
        mLocationClient.setLocationOption(option == null ? getDefaultOption() : option);
        // 设置定位监听
        mLocationClient.setLocationListener(listener == null ? locationListener : listener);
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        // 启动定位
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        // 停止定位
        mLocationClient.stopLocation();
    }

    /**
     * 销毁定位
     */
    public void destroyLocation() {
        if (null != mLocationClient) {
            /*
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    /**
     * 手动计算距离
     *
     * @param lat1 经度1
     * @param lng1 纬度1
     * @param lat2 经度2
     * @param lng2 纬度2
     */
    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        DPoint start = new DPoint(lat1, lng1);
        DPoint end = new DPoint(lat2, lng2);
        float distance = CoordinateConverter.calculateLineDistance(start, end);
        return distance / 1000;
    }
}
