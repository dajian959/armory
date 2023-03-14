package cn.armory.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tbruyelle.rxpermissions3.RxPermissions;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import cn.armory.common.http.HttpManager;
import cn.armory.common.utils.BarUtils;
import cn.armory.common.view.LoadingDialog;
import io.reactivex.rxjava3.functions.Consumer;

public abstract class BaseFragment extends Fragment implements IBaseView {
    protected Activity activity;
    protected View rootView;
    /**
     * 是否初始化过布局
     */
    protected boolean isViewInitiated;
    /**
     * 当前界面是否可见
     */
    protected boolean isVisibleToUser;
    /**
     * 是否加载过数据
     */
    protected boolean isDataInitiated;
    private Dialog loadingDialog;
    private RxPermissions rxPermissions;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocalData();
        HttpManager.getInstance().bindLifecycleOwner(this);
        loadingDialog = setLoadingDialog();
        rxPermissions = new RxPermissions(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            initFragmentView(inflater, container);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        initEvent();
        setStatusBar();
        initView();
        initListener();
        //加载数据
        prepareFetchData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewInitiated = false;
        isDataInitiated = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.rootView = null;
        HttpManager.getInstance().unbindLifecycleOwner(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            prepareFetchData();
        }
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(activity);
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
     * 判断懒加载条件
     */
    private void prepareFetchData() {
        if (isVisibleToUser && isViewInitiated && !isDataInitiated) {
            initData();
            isDataInitiated = true;
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
            if (ContextCompat.checkSelfPermission(activity, perm)
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
        return new LoadingDialog(activity);
    }

    /**
     * 此处设置沉浸式地方
     */
    protected void setStatusBar() {
        BarUtils.setStatusBarColor(activity, Color.WHITE, true);
    }

    /**
     * 初始化总线事件
     * 如果想手动控制loading展示，去掉super方法即可
     */
    protected abstract void initEvent();

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
     * 用于加载fragment的view
     */
    protected abstract void initFragmentView(LayoutInflater inflater, ViewGroup container);

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