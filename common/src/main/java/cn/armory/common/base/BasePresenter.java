package cn.armory.common.base;


import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

public abstract class BasePresenter<T extends BaseView, M extends BaseModel> {

    //View接口类型的软引用
    protected Reference<T> mViewRef;
    protected T mView;
    protected M mModel;

    public void attachView(T view) {
        //建立关系
        mViewRef = new SoftReference<T>(view);
        mView = getView();
    }

    private T getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        //解除关系
        if (mViewRef != null)
            mViewRef.clear();
        if (mView != null)
            mView = null;
    }

    // 创建Model实例
    protected abstract M createModel();

}