package cn;

import cn.armory.common.base.BaseApplication;
import cn.armory.common.http.HttpManager;

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpManager.init("https://www.baidu.com", 200);
    }
}
