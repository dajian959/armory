package cn.armory.explore.activity;

import android.app.Dialog;

import cn.armory.common.base.BaseActivity;
import cn.armory.explore.mvp.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter>{


    @Override
    protected MainPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initData() {

    }
}