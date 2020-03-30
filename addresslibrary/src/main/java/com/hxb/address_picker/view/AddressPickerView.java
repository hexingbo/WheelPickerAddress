package com.hxb.address_picker.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hxb.address_picker.R;

/**
 * @Author :hexingbo
 * @Date :2018/11/6
 * @FileName： AddressPickerView
 * @Describe :
 */
public class AddressPickerView extends PopupWindow implements AddressPicker.OnAddressPickerChangListener {

    private Activity activity;
    private OnAddressListener listener;
    private String Str_Submit;//确定按钮文字
    private String Str_Cancel;//取消按钮文字
    private int Color_Province;//省份字体颜色
    private int Color_City;//城市字体颜色
    private int Color_District;//区域字体颜色

    private int Color_Submit;//确定按钮颜色
    private int Color_Cancel;//取消按钮颜色

    private int Color_Background_Wheel;//滚轮背景颜色
    private int Color_Background_Title;//标题背景颜色

    private int Size_Submit_Cancel;//确定取消按钮大小
    private int Size_Content;//内容字体大小
    private int dividerColor; //分割线的颜色
    private int selectItemColor;//当前选中item背景
    private int Color_Content_Line;//内容区域分割线颜色
    private boolean isShowTotal = false;

    public AddressPickerView(Builder builder) {
        super(builder.activity);
        this.activity = builder.activity;
        this.Str_Submit = builder.Str_Submit;
        this.Str_Cancel = builder.Str_Cancel;
        this.Color_Submit = builder.Color_Submit;
        this.Color_Cancel = builder.Color_Cancel;
        this.Color_Background_Wheel = builder.Color_Background_Wheel;
        this.Color_Background_Title = builder.Color_Background_Title;
        this.Size_Submit_Cancel = builder.Size_Submit_Cancel;
        this.Size_Content = builder.Size_Content;
        this.dividerColor = builder.dividerColor;
        this.selectItemColor = builder.selectItemColor;
        this.Color_Content_Line = builder.Color_Content_Line;
        this.isShowTotal = builder.isShowTotal;
        this.Color_Province = builder.Color_Province;
        this.Color_City = builder.Color_City;
        this.Color_District = builder.Color_District;
        this.listener = builder.listener;
        initView(builder.activity);
    }

    private void initView(Context context) {
        AddressPicker addressPicker = new AddressPicker.Builder(context, new AddressPicker.OnAddressPickerChangListener() {
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
        })
                .setStrSubmit(Str_Submit)
                .setStrCancel(Str_Cancel)
                .setColorProvince(Color_Province)
                .setColorCity(Color_City)
                .setColorDistrict(Color_District)
                .setColorSubmit(Color_Submit)
                .setColorCancel(Color_Cancel)
                .setSelectItemColor(selectItemColor)
                .setColorBackgroundWheel(Color_Background_Wheel)
                .setColorBackgroundTitle(Color_Background_Title)
                .setSizeSubmitCancel(Size_Submit_Cancel)
                .setSizeContent(Size_Content)
                .setDividerColor(dividerColor)
                .setColorContentLine(Color_Content_Line)
                .setShowTotalOptions(isShowTotal)
                .build();
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

    @Override
    public void onAddressPickerSelect(String province, String city, String district, String areaId) {
        close();
    }

    @Override
    public void onAddressPickerCancel() {
        close();
    }

    public interface OnAddressListener {
        void onSelected(String Province, String City, String District, String PostCode);
    }

    //建造器
    public static class Builder {
        private Activity activity;
        private String Str_Submit = "确定";//确定按钮文字
        private String Str_Cancel = "取消";//取消按钮文字

        private int Color_Submit = R.color.Color_Submit;//确定按钮颜色
        private int Color_Cancel = R.color.Color_Cancel;//取消按钮颜色
        private int Color_Province = R.color.Color_Province;//省份字体颜色
        private int Color_City = R.color.Color_City;//城市字体颜色
        private int Color_District = R.color.Color_District;//区域字体颜色

        private int Color_Background_Wheel = R.color.Color_Background_Wheel;//滚轮背景颜色
        private int Color_Background_Title = R.color.Color_Background_Title;//标题背景颜色

        private int Size_Submit_Cancel = 16;//确定取消按钮大小
        private int Size_Content = 16;//内容字体大小
        private int dividerColor = R.color.dividerColor; //分割线的颜色
        private int selectItemColor=R.color.selectItemColor;//当前选中item背景
        private int Color_Content_Line = R.color.Color_Content_Line; //内容区域割线的颜色
        private boolean isShowTotal;
        private OnAddressListener listener;

        //Required
        public Builder(Activity activity, OnAddressListener listener) {
            this.activity = activity;
            this.listener = listener;
        }
        
        public Builder setStrSubmit(String str_Submit) {
            Str_Submit = str_Submit;
            return this;
        }

        public Builder setStrCancel(String str_Cancel) {
            Str_Cancel = str_Cancel;
            return this;
        }

        public Builder setColorSubmit(int color_Submit) {
            Color_Submit = color_Submit;
            return this;
        }

        public Builder setColorCancel(int color_Cancel) {
            Color_Cancel = color_Cancel;
            return this;
        }

        public Builder setColorProvince(int color_Province) {
            Color_Province = color_Province;
            return this;
        }

        public Builder setColorCity(int color_City) {
            Color_City = color_City;
            return this;
        }

        public Builder setColorDistrict(int color_District) {
            Color_District = color_District;
            return this;
        }

        public Builder setColorBackgroundWheel(int color_Background_Wheel) {
            Color_Background_Wheel = color_Background_Wheel;
            return this;
        }

        public Builder setColorBackgroundTitle(int color_Background_Title) {
            Color_Background_Title = color_Background_Title;
            return this;
        }

        public Builder setSizeSubmitCancel(int size_Submit_Cancel) {
            Size_Submit_Cancel = size_Submit_Cancel;
            return this;
        }

        public Builder setSizeContent(int size_Content) {
            Size_Content = size_Content;
            return this;
        }

        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Builder setColorContentLine(int color_Content_Line) {
            Color_Content_Line = color_Content_Line;
            return this;
        }

        public Builder setShowTotal(boolean showTotal) {
            isShowTotal = showTotal;
            return this;
        }

        public Builder setSelectItemColor(int selectItemColor) {
            this.selectItemColor = selectItemColor;
            return this;
        }

        public AddressPickerView build() {
            return new AddressPickerView(this);
        }

    }

}
