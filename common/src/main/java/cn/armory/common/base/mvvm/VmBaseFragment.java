package cn.armory.common.base.mvvm;

import android.app.Activity;
import android.app.Application;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;

import cn.armory.common.base.BaseApplication;
import cn.armory.common.base.BaseFragment;

public abstract class VmBaseFragment<D extends ViewDataBinding, V extends VmBaseModel> extends BaseFragment {
    protected D binding;
    protected V viewModel;
    private ViewModelProvider fragmentProvider;
    private ViewModelProvider activityProvider;
    private ViewModelProvider applicationProvider;

    @Override
    protected void initFragmentView(LayoutInflater inflater, ViewGroup container) {
        initViewModel();
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        binding.setLifecycleOwner(this);
        rootView = binding.getRoot().getRootView();
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
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }

    protected enum ViewModelScopeType {
        FRAGMENT,
        ACTIVITY,
        APPLICATION
    }

    /**
     * 设置 viewModel作用域
     *
     * @return ViewModelScopeType
     */
    protected ViewModelScopeType getViewModelScopeType() {
        return ViewModelScopeType.FRAGMENT;
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
        if (getViewModelScopeType() == ViewModelScopeType.ACTIVITY) {
            viewModel = getActivityScopeViewModel(modelClass);
        } else if (getViewModelScopeType() == ViewModelScopeType.APPLICATION) {
            viewModel = getApplicationScopeViewModel(modelClass);
        } else {
            viewModel = getFragmentScopeViewModel(modelClass);
        }
    }

    protected V getFragmentScopeViewModel(@NonNull Class<V> modelClass) {
        if (fragmentProvider == null) {
            fragmentProvider = new ViewModelProvider(this);
        }
        return fragmentProvider.get(modelClass);
    }

    private V getActivityScopeViewModel(@NonNull Class<V> modelClass) {
        if (activityProvider == null) {
            activityProvider = new ViewModelProvider(this);
        }
        return activityProvider.get(modelClass);
    }

    private V getApplicationScopeViewModel(@NonNull Class<V> modelClass) {
        if (applicationProvider == null) {
            applicationProvider = new ViewModelProvider((BaseApplication) activity.getApplication(), getApplicationFactory(activity));
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
