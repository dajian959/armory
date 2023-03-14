package cn.armory.explore.viewModel;

import androidx.lifecycle.MutableLiveData;

import cn.armory.common.base.BaseObserver;
import cn.armory.common.base.Result;
import cn.armory.common.base.mvvm.VmBaseModel;
import cn.armory.common.http.HttpManager;
import cn.armory.explore.bean.TextBean;
import cn.armory.explore.mvp.MainModel;

public class VmMainViewModel extends VmBaseModel {
    private MainModel mainModel;
    private MutableLiveData<Result<TextBean>> listData = new MutableLiveData<>();

    public MutableLiveData<Result<TextBean>> getListData() {
        return listData;
    }

    @Override
    protected void createModel() {
        mainModel = new MainModel();
    }

    public void getList() {
        HttpManager.getInstance().request(mainModel.getCC(), new BaseObserver<TextBean>() {

            @Override
            public void onSuccess(Result<TextBean> o) {
                listData.postValue(o);
            }

            @Override
            public void onError(String msg) {
                listData.postValue(new Result<>(msg, 500));
            }
        });
    }
}
