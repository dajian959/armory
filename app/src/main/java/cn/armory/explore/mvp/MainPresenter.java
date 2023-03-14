package cn.armory.explore.mvp;

import cn.armory.common.base.BaseObserver;
import cn.armory.common.base.Result;
import cn.armory.common.base.mvp.BasePresenter;
import cn.armory.common.http.HttpManager;
import cn.armory.explore.bean.TextBean;

/**
 * @author gjq
 * @packagename cn.armory.explore.prenster
 * @date 2020/8/27
 * @describe
 */
public class MainPresenter extends BasePresenter<MainView, MainModel> {
    @Override
    protected MainModel createModel() {
        return new MainModel();
    }

    public void getList() {
        HttpManager.getInstance().request(model.getCC(), new BaseObserver<TextBean>() {

            @Override
            public void onSuccess(Result<TextBean> o) {
                view.onTextSuccess();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
}
