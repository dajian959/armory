package cn.armory.common.base;

public interface BaseView {
    //显示dialog
    void showLoading();

    //隐藏 dialog
    void hideLoading();

    //进度条显示
    void showProgress();

    //进度条隐藏
    void hideProgress();

    //文件下载进度监听
    void onProgress(int progress);
}