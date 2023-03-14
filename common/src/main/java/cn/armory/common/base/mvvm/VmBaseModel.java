package cn.armory.common.base.mvvm;

import androidx.lifecycle.ViewModel;

public abstract class VmBaseModel extends ViewModel {
    protected abstract void createModel();

    public VmBaseModel() {
        createModel();
    }
}
