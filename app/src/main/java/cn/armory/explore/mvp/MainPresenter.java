package cn.armory.explore.mvp;

import cn.armory.common.base.BaseModel;
import cn.armory.common.base.BasePresenter;

/**
 * @author gjq
 * @packagename cn.armory.explore.prenster
 * @date 2020/8/27
 * @describe
 */
public class MainPresenter extends BasePresenter<MainView,MainModel> {
    public void getList() {
        mModel.getCC();
        mView.onTextSuccess();
    }


    @Override
    protected MainModel createModel() {
        return null;
    }
}
