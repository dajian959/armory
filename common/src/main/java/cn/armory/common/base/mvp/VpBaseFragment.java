package cn.armory.common.base.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import cn.armory.common.base.BaseFragment;

public abstract class VpBaseFragment<P extends BasePresenter> extends BaseFragment {
    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    protected void initFragmentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, rootView);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(activity).unbind();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
    }

    protected abstract P createPresenter();
}
