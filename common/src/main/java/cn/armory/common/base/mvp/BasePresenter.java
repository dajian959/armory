package cn.armory.common.base.mvp;


import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.armory.common.base.IBaseModel;
import cn.armory.common.base.IBaseView;

public abstract class BasePresenter<T extends IBaseView, M extends IBaseModel> {

    //View接口类型的软引用
    protected Reference<T> viewRef;
    protected T view;
    protected M model = createModel();

    @SuppressWarnings("unchecked")
    public void attachView(T view) {
        //建立关系
        viewRef = new SoftReference<T>(view);
        this.view = (T) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if (viewRef == null || viewRef.get() == null) {
                    return null;
                }
                return method.invoke(viewRef.get(), objects);
            }
        });

    }

    public void detachView() {
        //解除关系
        if (viewRef != null)
            viewRef.clear();
        if (view != null)
            view = null;
    }

    // 创建Model实例
    protected abstract M createModel();

}