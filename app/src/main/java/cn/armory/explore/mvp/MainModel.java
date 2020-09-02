package cn.armory.explore.mvp;

import cn.armory.common.base.BaseModel;
import cn.armory.common.http.HttpManager;
import cn.armory.explore.bean.TextBean;
import io.reactivex.Observable;

/**
 * @author gjq
 * @packagename cn.armory.explore.mvp
 * @date 2020/8/27
 * @describe
 */
public class MainModel implements BaseModel {
    public Observable<TextBean> getCC() {
        return HttpManager.getInstance().getService(HttpInterface.class).getSplash(null);
    }
}
