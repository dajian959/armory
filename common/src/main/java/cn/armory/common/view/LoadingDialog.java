package cn.armory.common.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import cn.armory.common.R;


/**
 * File descripition:   简单菊花框
 *
 * @date 2019/4/10
 */
public class LoadingDialog extends Dialog {
    private TextView mTvText;

    public LoadingDialog(Context context) {
        super(context);
        try {
            Context contexts = this.getContext();
            int dividerID = contexts.getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = this.findViewById(dividerID);
            if (divider != null) {
                divider.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_loading);
        mTvText = findViewById(R.id.tv_text);
        mTvText.setText("正在加载...");
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

}