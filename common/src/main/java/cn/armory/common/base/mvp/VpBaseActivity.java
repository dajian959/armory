package cn.armory.common.base.mvp;

import android.os.Bundle;

import butterknife.ButterKnife;
import cn.armory.common.base.BaseActivity;


/**
 * activity基类
 */
public abstract class VpBaseActivity<P extends BasePresenter> extends BaseActivity {
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    protected void initActivityView() {
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onResume() {
        super.onResume();
        if (null != presenter)
            presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
        if (presenter != null) {
            presenter.detachView();
        }
        if (presenter != null) {
            presenter = null;
        }
    }

    protected abstract P createPresenter();

}