package cn.armory.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import androidx.annotation.Nullable;

/**
 * 自定义View实现拖动并自动吸边效果
 * <p>
 * 处理滑动和贴边 {@link #onTouchEvent(MotionEvent)}
 * 处理事件分发 {@link #dispatchTouchEvent(MotionEvent)}
 * </p>
 */
public class AttachButton extends View {
    private float mLastRawX;
    private float mLastRawY;
    private boolean isDrug = false;
    private int mRootWidth = 0;
    private int mRootHeight = 0;
    private int mRootTopY = 0;

    public AttachButton(Context context) {
        this(context, null);
    }

    public AttachButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AttachButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //当前手指的坐标
        float mRawX = ev.getRawX();
        float mRawY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下
                isDrug = false;
                //记录按下的位置
                mLastRawX = mRawX;
                mLastRawY = mRawY;
                ViewGroup mViewGroup = (ViewGroup) getParent();
                if (mViewGroup != null) {
                    int[] location = new int[2];
                    mViewGroup.getLocationInWindow(location);
                    //获取父布局的高度
                    mRootHeight = mViewGroup.getMeasuredHeight();
                    mRootWidth = mViewGroup.getMeasuredWidth();
                    //获取父布局顶点的坐标
                    mRootTopY = location[1];
                }
                break;
            case MotionEvent.ACTION_MOVE://手指滑动
                if (mRawX >= 0 && mRawX <= mRootWidth && mRawY >= mRootTopY && mRawY <= (mRootHeight + mRootTopY)) {
                    //手指X轴滑动距离
                    float diffX = mRawX - mLastRawX;
                    //手指Y轴滑动距离
                    float diffY = mRawY - mLastRawY;
                    //判断是否为拖动操作
                    if (!isDrug) {
                        isDrug = !(Math.sqrt(diffX * diffX + diffY * diffY) < 2);
                    }
                    //获取手指按下的距离与控件本身X轴的距离
                    float ownX = getX();
                    //获取手指按下的距离与控件本身Y轴的距离
                    float ownY = getY();
                    //理论中X轴拖动的距离
                    float endX = ownX + diffX;
                    //理论中Y轴拖动的距离
                    float endY = ownY + diffY;
                    //X轴可以拖动的最大距离
                    float maxX = mRootWidth - getWidth();
                    //Y轴可以拖动的最大距离
                    float maxY = mRootHeight - getHeight();
                    //X轴边界限制
                    endX = endX < 0 ? 0 : endX > maxX ? maxX : endX;
                    //Y轴边界限制
                    endY = endY < 0 ? 0 : endY > maxY ? maxY : endY;
                    //开始移动
                    setX(endX);
                    setY(endY);
                    //记录位置
                    mLastRawX = mRawX;
                    mLastRawY = mRawY;
                }

                break;
            case MotionEvent.ACTION_UP://手指离开
                //判断是否为点击事件
                if (isDrug) {
                    int center = mRootWidth / 2;
                    //自动贴边
                    if (mLastRawX <= center) {
                        //向左贴边
                        AttachButton.this.animate()
                                .setInterpolator(new BounceInterpolator())
                                .setDuration(500)
                                .x(mRootWidth - getWidth())
                                .start();
                    } else {
                        //向右贴边
                        AttachButton.this.animate()
                                .setInterpolator(new BounceInterpolator())
                                .setDuration(500)
                                .x(mRootWidth - getWidth())
                                .start();
                    }
                }

                break;
        }

        //是否拦截事件
        return isDrug ? isDrug : super.onTouchEvent(ev);
    }
}

