package cn.armory.common.base.mvvm;

import android.util.SparseArray;

import androidx.annotation.NonNull;

public class DataBindingConfig {
    private final SparseArray<Object> bindingParams = new SparseArray<>();

    public DataBindingConfig() {

    }

    public SparseArray<Object> getBindingParams() {
        return this.bindingParams;
    }

    public DataBindingConfig addBindingParam(@NonNull Integer variableId, @NonNull Object object) {
        if (this.bindingParams.get(variableId) == null) {
            this.bindingParams.put(variableId, object);
        }
        return this;
    }
}