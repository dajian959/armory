package cn.armory.explore;

import cn.armory.common.base.BaseActivity;
import cn.armory.common.base.BasePresenter;
import cn.armory.common.base.BaseView;

public class MainActivity extends BaseActivity<BasePresenter<BaseView>> {


    @Override
    protected BasePresenter<BaseView> createPresenter() {
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