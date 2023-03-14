package cn.armory.common.base.mvvm;


import android.app.Activity;
import android.app.Application;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;

import cn.armory.common.base.BaseActivity;
import cn.armory.common.base.BaseApplication;

public abstract class VmBaseActivity<D extends ViewDataBinding, V extends VmBaseModel> extends BaseActivity {
    protected D binding;
    protected V viewModel;
    private ViewModelProvider activityProvider;
    private ViewModelProvider applicationProvider;

    @Override
    protected void initActivityView() {
        initViewModel();
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.setLifecycleOwner(this);
        initLiveDataObserver();
        DataBindingConfig dataBindingConfig = getDataBindingConfig();
        if (dataBindingConfig == null) {
            return;
        }
        SparseArray<Object> bindingParams = dataBindingConfig.getBindingParams();
        if (bindingParams == null) {
            return;
        }
        for (int i = 0; i < dataBindingConfig.getBindingParams().size(); i++) {
            binding.setVariable(dataBindingConfig.getBindingParams().keyAt(i), dataBindingConfig.getBindingParams().valueAt(i));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }

    protected enum ViewModelScopeType {
        ACTIVITY,
        APPLICATION
    }

    /**
     * 设置 viewModel作用域
     *
     * @return ViewModelScopeType
     */
    protected ViewModelScopeType getViewModelScopeType() {
        return ViewModelScopeType.ACTIVITY;
    }

    /**
     * 初始化viewModel
     */
    @SuppressWarnings("unchecked")
    private void initViewModel() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        if (type == null) {
            return;
        }
        if (type.getActualTypeArguments().length < 2) {
            return;
        }
        Class<V> modelClass = (Class<V>) type.getActualTypeArguments()[1];
        if (getViewModelScopeType() == ViewModelScopeType.APPLICATION) {
            viewModel = getApplicationScopeViewModel(modelClass);
        } else {
            viewModel = getActivityScopeViewModel(modelClass);
        }
    }

    private V getActivityScopeViewModel(@NonNull Class<V> modelClass) {
        if (activityProvider == null) {
            activityProvider = new ViewModelProvider(this);
        }
        return activityProvider.get(modelClass);
    }

    private V getApplicationScopeViewModel(@NonNull Class<V> modelClass) {
        if (applicationProvider == null) {
            applicationProvider = new ViewModelProvider((BaseApplication) getApplication(), getApplicationFactory(this));
        }
        return applicationProvider.get(modelClass);
    }

    private ViewModelProvider.Factory getApplicationFactory(Activity activity) {
        Application application = checkApplication(activity);
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application);
    }

    private Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request ViewModel before onCreate call.");
        }
        return application;
    }

    /**
     * 初始化livedata监听事件
     */
    protected abstract void initLiveDataObserver();

    /**
     * 双向绑定
     *
     * @return 双向绑定的参数
     */
    protected abstract DataBindingConfig getDataBindingConfig();
}
