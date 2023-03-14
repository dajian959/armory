package cn.armory.common.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.jeremyliao.liveeventbus.LiveEventBus;

import cn.armory.common.utils.ACUtils;

public class BaseApplication extends Application implements ViewModelStoreOwner {
    private ViewModelStore appViewModelStore;

    @Override
    public void onCreate() {
        super.onCreate();
        appViewModelStore = new ViewModelStore();
        boolean debug = ACUtils.isAppDebug(this);
        LiveEventBus.config()
                .lifecycleObserverAlwaysActive(true)
                .autoClear(true)
                .setContext(this)
                .enableLogger(debug);
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return appViewModelStore;
    }
}
