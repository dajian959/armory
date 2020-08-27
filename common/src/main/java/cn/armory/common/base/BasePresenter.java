package cn.armory.common.base;


import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class BasePresenter<T extends BaseView, M extends BaseModel> {

    //View接口类型的软引用
    protected Reference<T> mViewRef;
    protected T mView;
    protected M mModel = createModel();

    @SuppressWarnings("unchecked")
    public void attachView(T view) {
        //建立关系
        mViewRef = new SoftReference<T>(view);
        mView = (T) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if (mViewRef == null || mViewRef.get() == null) {
                    return null;
                }
                return method.invoke(mViewRef.get(), objects);
            }
        });

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