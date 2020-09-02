package cn.armory.common.base;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import butterknife.ButterKnife;
import cn.armory.common.http.HttpManager;
import cn.armory.common.utils.ACUtils;
import cn.armory.common.utils.KeyBoardUtils;
import cn.armory.common.view.LoadingDialog;
import cn.armory.common.view.ProgressDialog;


/**
 * activity基类
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P mPresenter;

    protected abstract P createPresenter();

    private Dialog loadingDialog;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mPresenter = createPresenter();
        loadingDialog = setLoadingDialog();
        progressDialog = setProgressDialog();
        setStatusBar();
        setScreenRotate(true);
        this.initData();
    }

    /**
     * 重写该方法以设置加载框
     * 否则将使用默认加载框
     *
     * @return Dialog
     */
    protected Dialog setLoadingDialog() {
        return new LoadingDialog(this);
    }

    /**
     * 重写该方法以设置进度框
     * 否则将使用默认进度框
     *
     * @return Dialog
     */
    protected Dialog setProgressDialog() {
        return new ProgressDialog(this);
    }

    /**
     * 获取布局ID
     *
     * @return int
     */
    protected abstract int getLayoutId();

    /**
     * 数据初始化操作
     */
    protected abstract void initData();

    /**
     * 此处设置沉浸式地方
     */
    protected void setStatusBar() {

    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onResume() {
        super.onResume();
        if (null != mPresenter)
            mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();

        if (loadingDialog != null)
            loadingDialog.dismiss();

        if (progressDialog != null)
            progressDialog.dismiss();

        if (mPresenter != null)
            mPresenter.detachView();

        if (mPresenter != null)
            mPresenter = null;

        HttpManager.removeDisposable();
    }


    @Override
    public void showProgress() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);

        if (progressDialog instanceof ProgressDialog)
            ((ProgressDialog) progressDialog).getProgressBar().performAnimation();


        if (!progressDialog.isShowing())
            progressDialog.show();

    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog instanceof ProgressDialog)
                ((ProgressDialog) progressDialog).getProgressBar().releaseAnimation();

            progressDialog.dismiss();
        }
    }

    @Override
    public void onProgress(int progress) {
        if (progressDialog != null && progressDialog instanceof ProgressDialog)
            ((ProgressDialog) progressDialog).updateProgress(progress);
    }

    /**
     * [页面跳转]
     *
     * @param clz 跳转的Activity
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }


    /**
     * [携带数据的页面跳转]
     *
     * @param clz    跳转的Activity
     * @param bundle bundle添加的参数
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls         跳转的Activity
     * @param bundle      bundle添加的参数
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param ids 输入框
     */
    public void clearViewFocus(View v, int... ids) {
        if (null != v && null != ids && ids.length > 0) {
            for (int id : ids) {
                if (v.getId() == id) {
                    v.clearFocus();
                    break;
                }
            }
        }
    }


    /**
     * 添加fragment
     *
     * @param fragment 添加的fragment
     * @param frameId  布局id
     */
    public void addFragment(BaseFragment fragment, @IdRes int frameId) {
        ACUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     *
     * @param fragment 替换的fragment
     * @param frameId  布局id
     */
    public void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        ACUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     *
     * @param fragment 隐藏的fragment
     */
    public void hideFragment(BaseFragment fragment) {
        ACUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 显示fragment
     *
     * @param fragment 显示的fragment
     */
    public void showFragment(BaseFragment fragment) {
        ACUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     *
     * @param fragment 移除的fragment
     */
    public void removeFragment(BaseFragment fragment) {
        ACUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    public void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


    /**
     * 隐藏键盘
     *
     * @param v   焦点所在View
     * @param ids 输入框
     * @return true代表焦点在edit上
     */
    public boolean isFocusEditText(View v, int... ids) {
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            for (int id : ids) {
                if (et.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    //是否触摸在指定view上面,对某个控件过滤
    public boolean isTouchView(View[] views, MotionEvent ev) {
        if (views == null || views.length == 0) {
            return false;
        }
        int[] location = new int[2];
        for (View view : views) {
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getX() > x && ev.getX() < (x + view.getWidth())
                    && ev.getY() > y && ev.getY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTouchView(filterViewByIds(), ev)) {
                return super.dispatchTouchEvent(ev);
            }
            if (hideSoftByEditViewIds() == null || hideSoftByEditViewIds().length == 0) {
                return super.dispatchTouchEvent(ev);
            }
            View v = getCurrentFocus();
            if (isFocusEditText(v, hideSoftByEditViewIds())) {
                KeyBoardUtils.hideInputForce(this);
                clearViewFocus(v, hideSoftByEditViewIds());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return id 数组
     */
    public int[] hideSoftByEditViewIds() {
        return null;
    }

    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return id 数组
     */
    public View[] filterViewByIds() {
        return null;
    }

    /**
     * 设置屏幕横竖屏切换
     *
     * @param screenRotate true  竖屏     false  横屏
     */
    private void setScreenRotate(Boolean screenRotate) {
        if (screenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

}