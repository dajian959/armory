package cn.armory.explore.mvp;

import cn.armory.explore.bean.TextBean;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author gjq
 * @packagename cn.armory.explore.mvp
 * @date 2020/9/2
 * @describe
 */
public interface HttpInterface {
    @POST("/interface/getSplash.do")
    @FormUrlEncoded
    Observable<TextBean> getSplash(@Field("id") String id);
}
