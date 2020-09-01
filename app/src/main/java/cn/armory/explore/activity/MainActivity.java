package cn.armory.explore.activity;

import android.widget.TextView;

import butterknife.BindView;
import cn.armory.common.base.BaseActivity;
import cn.armory.explore.R;
import cn.armory.explore.mvp.MainPresenter;
import cn.armory.explore.mvp.MainView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {


    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onTextSuccess() {

    }
}