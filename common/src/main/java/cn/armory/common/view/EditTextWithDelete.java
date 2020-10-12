package cn.armory.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.armory.common.R;
import cn.armory.common.utils.CheckStringUtils;



public class EditTextWithDelete extends AppCompatEditText {
    private Drawable imgEnable;
    private Context context;
    private OnDeleteListener mOnDeleteListener;

    public interface OnDeleteListener {
        void delete();
    }

    public void setOnDeleteListener(OnDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }

    public EditTextWithDelete(Context paramContext) {
        super(paramContext);
        this.context = paramContext;
        init();
    }

    public EditTextWithDelete(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
        init();
    }

    public EditTextWithDelete(Context paramContext, AttributeSet paramAttributeSet,
                              int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.context = paramContext;
        init();
    }


    private void init() {
        // 获取图片资源
        imgEnable = context.getResources().getDrawable(
                R.drawable.edittext_delete);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    /**
     * 设置删除图片
     */
    private void setDrawable() {
        if (length() == 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgEnable, null);
        }
    }

    /**
     * event.getX() 获取相对应自身左上角的X坐标 event.getY() 获取相对应自身左上角的Y坐标 getWidth()
     * 获取控件的宽度 getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离 getPaddingRight()
     * 获取删除图标右边缘到控件右边缘的距离 getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgEnable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            // 判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight()))
                    && (x < (getWidth() - getPaddingRight()));
            // 获取删除图标的边界，返回一个Rect对象
            Rect rect = imgEnable.getBounds();
            // 获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            // 计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            // 判断触摸点是否在竖直范围内(可能会有点误差)
            // 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if (isInnerWidth && isInnerHeight) {
                setText("");
                if (null != mOnDeleteListener) {
                    mOnDeleteListener.delete();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        // 获得焦点，判断是否有内容
        if (this.hasFocus()) {
            // 如果没有内容，则保护显示清空按钮，否则就显示
            if (CheckStringUtils.isEmpty(getText())) {
                setCompoundDrawables(null, null, null, null);
            } else {
                setCompoundDrawables(null, null, imgEnable, null);
            }
        } else {
            // 失去焦点，隐藏按钮
            setCompoundDrawables(null, null, null, null);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}