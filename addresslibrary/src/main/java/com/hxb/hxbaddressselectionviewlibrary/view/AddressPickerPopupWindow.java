package com.hxb.hxbaddressselectionviewlibrary.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hxb.hxbaddressselectionviewlibrary.R;

/**
 * @Author :hexingbo
 * @Date :2018/11/6
 * @FileName： AddressPickerPopupWindow
 * @Describe :
 */
public class AddressPickerPopupWindow extends PopupWindow {

    Activity activity;
    private final AddressPicker addressPicker;

    private OnAddressPickerSelectListener listener;

    public void setOnAddressPickerSelectListener(OnAddressPickerSelectListener listener) {
        this.listener = listener;
    }

    public AddressPickerPopupWindow(Activity activity) {
        this.activity = activity;
        addressPicker = new AddressPicker(activity);
        addressPicker.setListener(new AddressPicker.OnAddressPickerChangListener() {
            @Override
            public void onAddressPickerSelect(String province, String city, String district, String areaId) {
                close();
                if (listener != null) {
                    listener.onSelected(province, city, district, areaId);
                }
            }

            @Override
            public void onAddressPickerCancel() {
                close();
            }
        });

        //设置PopupWindow的View
        this.setContentView(addressPicker);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Animation);
        this.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setOutsideTouchable(true);

        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                close();
            }
        });
    }

    // 设置屏幕透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        activity.getWindow().setAttributes(lp);
    }

    public void show() {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.6f);
    }

    public void close() {
        backgroundAlpha(1f);
        dismiss();
    }

    /**
     * 关闭软键盘
     */
    public void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnAddressPickerSelectListener {
        void onSelected(String Province, String City, String District, String PostCode);
    }

}
