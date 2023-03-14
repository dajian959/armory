package cn.armory.common.base;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tbruyelle.rxpermissions3.RxPermissions;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import cn.armory.common.R;
import cn.armory.common.http.HttpManager;
import cn.armory.common.utils.AntiHijackingUtils;
import cn.armory.common.utils.BarUtils;
import cn.armory.common.utils.ToastUtils;
import cn.armory.common.view.LoadingDialog;
import io.reactivex.rxjava3.functions.Consumer;


public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private Dialog loadingDialog;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocalData();
        initActivityView();
        HttpManager.getInstance().bindLifecycleOwner(this);
        loadingDialog = setLoadingDialog();
        rxPermissions = new RxPermissions(this);
        setStatusBar();
        initEvent();
        initView();
        initListener();
        initData();
    }


    @Override
    protected void onStop() {
        super.onStop();
        // 白名单
        boolean safe = AntiHijackingUtils.checkActivity(this);
        // 系统桌面
        boolean isHome = AntiHijackingUtils.isHome(this);
        // 锁屏操作
        boolean isReflectScreen = AntiHijackingUtils.isReflectScreen(this);
        // 判断程序是否当前显示
        if (!safe && !isHome && !isReflectScreen) {
            ToastUtils.showLong(getString(R.string.warning));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpManager.getInstance().unbindLifecycleOwner(this);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 添加fragment
     *
     * @param fragment 添加的fragment
     * @param frameId  布局id
     */
    public void addFragment(@NonNull BaseFragment fragment, @IdRes int frameId) {
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     *
     * @param fragment 替换的fragment
     * @param frameId  布局id
     */
    public void replaceFragment(@NonNull BaseFragment fragment, @IdRes int frameId) {

        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     *
     * @param fragment 隐藏的fragment
     */
    public void hideFragment(@NonNull BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 显示fragment
     *
     * @param fragment 显示的fragment
     */
    public void showFragment(@NonNull BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     *
     * @param fragment 移除的fragment
     */
    public void removeFragment(@NonNull BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    public void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    protected void requestPermissions(String... permissions) {
        if (hasPermissions(permissions)) {
            return;
        }
        rxPermissions.request(permissions)
                .to(AutoDispose.<Boolean>autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            onPermissionsGranted();
                        } else {
                            onPermissionsDenied();
                        }
                    }
                });
    }


    protected boolean hasPermissions(@Size(min = 1) @NonNull String... perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(this, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 重写该方法以设置加载框
     * 否则将使用默认加载框
     *
     * @return Dialog
     */
    protected Dialog setLoadingDialog() {
        return new LoadingDialog(this);
    }

    /**
     * 此处设置沉浸式地方
     */
    protected void setStatusBar() {
        BarUtils.setStatusBarColor(this, Color.WHITE, true);
    }

    /**
     * 初始化总线事件
     * 如果想手动控制loading展示，去掉super方法即可
     */
    protected void initEvent() {
        LiveEventBus.get(BaseEvent.IS_SHOW_LOADING, Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isShow) {
                if (isShow) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });
    }

    /**
     * 已获取权限
     */
    protected void onPermissionsGranted() {
    }

    /**
     * 未获取权限
     */
    protected void onPermissionsDenied() {
    }

    /**
     * 在view加载前初始化的数据
     */
    protected abstract void initLocalData();

    /**
     * 用于加载activity的view
     */
    protected abstract void initActivityView();

    /**
     * 获取布局ID
     *
     * @return int
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view属性
     */
    protected abstract void initView();

    /**
     * 初始化操作
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();
}
