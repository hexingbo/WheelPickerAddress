package com.hxb.address_picker.view;


import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxb.address_picker.R;
import com.hxb.address_picker.adapter.ArrayWheelAdapter;
import com.hxb.address_picker.handler.XmlParserHandler;
import com.hxb.address_picker.listener.OnWheelChangedListener;
import com.hxb.address_picker.listener.OnWheelScrollListener;
import com.hxb.address_picker.modle.CityModel;
import com.hxb.address_picker.modle.DistrictModel;
import com.hxb.address_picker.modle.ProvinceModel;
import com.hxb.address_picker.wheel.WheelView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class AddressPicker extends RelativeLayout implements OnWheelScrollListener, OnWheelChangedListener {
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 区域id
     */
    protected Map<String, String> mAreaIdDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的id
     */
    protected String mCurrentAreaId = "";

    private WheelView mWheelViewProvince;
    private WheelView mWheelViewCity;
    private WheelView mWheelViewDistrict;

    private LayoutInflater mInflater = null;

    private View mViewAddressPick;

    private TextView mBtnAddressConfirm;
    private TextView mBtnAddressCancel;

    private OnAddressPickerChangListener mOnAddressPickChangListener;

    private Context mContext;
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


    public AddressPicker(Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
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
        this.mOnAddressPickChangListener = builder.listener;
        initView(builder.context);
        initData();

    }

    //建造器
    public static class Builder {
        private Context context;
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
        private int selectItemColor = R.color.selectItemColor; ;//当前选中item背景
        private int Color_Content_Line = R.color.Color_Content_Line; //内容区域割线的颜色
        private boolean isShowTotal = false;
        private OnAddressPickerChangListener listener;

        //Required
        public Builder(Context context, OnAddressPickerChangListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public Builder setStrSubmit(String Str_Submit) {
            this.Str_Submit = Str_Submit;
            return this;
        }

        public Builder setStrCancel(String Str_Cancel) {
            this.Str_Cancel = Str_Cancel;
            return this;
        }

        public Builder setColorSubmit(int Color_Submit) {
            this.Color_Submit = Color_Submit;
            return this;
        }

        public Builder setColorCancel(int Color_Cancel) {
            this.Color_Cancel = Color_Cancel;
            return this;
        }

        public Builder setColorBackgroundWheel(int Color_Background_Wheel) {
            this.Color_Background_Wheel = Color_Background_Wheel;
            return this;
        }

        public Builder setColorBackgroundTitle(int Color_Background_Title) {
            this.Color_Background_Title = Color_Background_Title;
            return this;
        }

        public Builder setSizeSubmitCancel(int Size_Submit_Cancel) {
            this.Size_Submit_Cancel = Size_Submit_Cancel;
            return this;
        }

        public Builder setSizeContent(int Size_Content) {
            this.Size_Content = Size_Content;
            return this;
        }

        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Builder setShowTotalOptions(boolean isShowTotal) {
            this.isShowTotal = isShowTotal;
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

        public Builder setColorContentLine(int Color_Content_Line) {
            Color_Content_Line = Color_Content_Line;
            return this;
        }

        public Builder setSelectItemColor(int selectItemColor) {
            this.selectItemColor = selectItemColor;
            return this;
        }

        public AddressPicker build() {
            return new AddressPicker(this);
        }

    }

    public void initView(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        mViewAddressPick = mInflater.inflate(R.layout.pop_address_pick, null);
        mViewAddressPick.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mWheelViewProvince = mViewAddressPick.findViewById(R.id.id_province);
        mWheelViewProvince.setCyclic(false);//是否循环
        mWheelViewProvince.setDividerColor(Color_Content_Line);
//        mWheelViewProvince.setShadowColor(R.drawable.wheel_center, R.drawable.wheel_center, R.drawable.wheel_center);

        mWheelViewCity = mViewAddressPick.findViewById(R.id.id_city);
        mWheelViewCity.setCyclic(false);//是否循环
        mWheelViewCity.setDividerColor(Color_Content_Line);
//        mWheelViewCity.setShadowColor(R.drawable.wheel_bg, R.drawable.wheel_bg, R.drawable.wheel_bg);

        mWheelViewDistrict = mViewAddressPick.findViewById(R.id.id_district);
        mWheelViewDistrict.setCyclic(false);//是否循环
        mWheelViewDistrict.setDividerColor(Color_Content_Line);
//        mWheelViewDistrict.setShadowColor(R.drawable.wheel_bg, R.drawable.wheel_bg, R.drawable.wheel_bg);

        mWheelViewProvince.addChangingListener(this);
        mWheelViewCity.addChangingListener(this);
        mWheelViewDistrict.addChangingListener(this);

        RelativeLayout mRlAdddressTop = mViewAddressPick.findViewById(R.id.rl_adddress_top);
        mRlAdddressTop.setBackgroundColor(mContext.getResources().getColor(Color_Background_Title));

        LinearLayout mLlAddressContent = mViewAddressPick.findViewById(R.id.ll_address_content);
        mLlAddressContent.setBackgroundColor(getResources().getColor(Color_Background_Wheel));

        View viewLine = mViewAddressPick.findViewById(R.id.view_line);
        viewLine.setBackgroundColor(getResources().getColor(dividerColor));

        mBtnAddressConfirm = mViewAddressPick.findViewById(R.id.address_confirm_btn);
        mBtnAddressConfirm.setText(Str_Submit);
        mBtnAddressConfirm.setTextColor(getResources().getColor(Color_Submit));
        mBtnAddressConfirm.setTextSize(TypedValue.COMPLEX_UNIT_SP, Size_Submit_Cancel);
        mBtnAddressConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnAddressPickChangListener != null) {
                    mOnAddressPickChangListener.onAddressPickerSelect(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName, mCurrentAreaId);

                }
            }
        });
        mBtnAddressCancel = mViewAddressPick.findViewById(R.id.address_cancel_btn);
        mBtnAddressCancel.setText(Str_Cancel);
        mBtnAddressCancel.setTextColor(getResources().getColor(Color_Cancel));
        mBtnAddressCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, Size_Submit_Cancel);
        mBtnAddressCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnAddressPickChangListener != null) {
                    mOnAddressPickChangListener.onAddressPickerCancel();
                }
            }
        });

        addView(mViewAddressPick);
    }

    public void initData() {
        initProvinceDatas();
        ArrayWheelAdapter<String> awa = new ArrayWheelAdapter<String>(mContext, mProvinceDatas);
        awa.setTextSize(Size_Content);
        awa.setTextColor(getResources().getColor(Color_Province));//省
        mWheelViewProvince.setViewAdapter(awa);
        updateCities();
        updateAreas();

    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            if (isShowTotal) {
                List<CityModel> citys = new ArrayList<CityModel>();
                List<DistrictModel> districtLists = new ArrayList<DistrictModel>();
                districtLists.add(new DistrictModel("全部", ""));
                CityModel city = new CityModel("全部", districtLists);
                citys.add(0, city);
                provinceList.add(0, new ProvinceModel("全部", citys));
            }
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentAreaId = districtList.get(0).getAreaId();
                }
            }
            // */
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getAreaId());
                        // 区/县对于的区域id，保存到mAreaIdDatasMap
                        mAreaIdDatasMap.put(cityList.get(j).getName() + districtList.get(k).getName(), districtList.get(k).getAreaId());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mWheelViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        try {
            ArrayWheelAdapter<String> awa = new ArrayWheelAdapter<String>(mContext, areas);
            awa.setTextSize(Size_Content);
            awa.setTextColor(getResources().getColor(Color_City));//市
            mWheelViewDistrict.setViewAdapter(awa);
            mWheelViewDistrict.setCurrentItem(0);
            mCurrentDistrictName = areas[0];
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mWheelViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        ArrayWheelAdapter<String> awa = new ArrayWheelAdapter<String>(mContext, cities);
        awa.setTextSize(Size_Content);
        awa.setTextColor(getResources().getColor(Color_District));//区
        mWheelViewCity.setViewAdapter(awa);
        mWheelViewCity.setCyclic(false);
        mWheelViewCity.setCurrentItem(0);
        updateAreas();
    }


    public void setIndicatorById(String areaId) {
        if (TextUtils.isEmpty(areaId))
            return;
        int provinceIndex = 0;
        int cityIndex = 0;
        int districtIndex = 0;
        String district = "";
        String city = "";
        String province = "";
        for (Entry<String, String> entry : mAreaIdDatasMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (areaId.equals(value)) {
                district = key;
                break;
            }
        }

        if (TextUtils.isEmpty(district))
            return;

        boolean find = false;
        for (Entry<String, String[]> entry : mDistrictDatasMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            int len = value.length;
            for (int i = 0; i < len; i++) {
                String tmpDistrict = value[i];
                if (district.equals(tmpDistrict)) {
                    city = key;
                    find = true;
                    districtIndex = i;
                    break;
                }
            }
            // for(String tmpDistrict:value) {
            // if(district.equals(tmpDistrict)) {
            // city = key;
            // find = true;
            // break;
            // }
            // }
            if (find)
                break;
        }

        if (TextUtils.isEmpty(city))
            return;
        find = false;
        for (Entry<String, String[]> entry : mCitisDatasMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();

            int len = value.length;
            for (int i = 0; i < len; i++) {
                String tmpCity = value[i];
                if (city.equals(tmpCity)) {
                    province = key;
                    find = true;
                    cityIndex = i;
                    break;
                }
            }

            // for(String tmpCity:value) {
            // if(city.equals(tmpCity)) {
            // province = key;
            // find = true;
            // break;
            // }
            // }
            if (find)
                break;
        }
        if (TextUtils.isEmpty(province))
            return;

        int len = mProvinceDatas.length;
        for (int i = 0; i < len; i++) {
            String tmpProvince = mProvinceDatas[i];
            if (province.equals(tmpProvince)) {
                provinceIndex = i;
                break;
            }
        }

        mWheelViewProvince.setCurrentItem(provinceIndex);
        mWheelViewCity.setCurrentItem(cityIndex);
        mWheelViewCity.setCyclic(false);
        mWheelViewDistrict.setCurrentItem(districtIndex);

    }

    public String getAreaById(String areaId) {
        if (TextUtils.isEmpty(areaId))
            return "";
        String area = "";
        String district = "";
        String city = "";
        String province = "";
        for (Entry<String, String> entry : mAreaIdDatasMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (areaId.equals(value)) {
                district = key;
                break;
            }
        }

        if (TextUtils.isEmpty(district))
            return "";

        boolean find = false;
        for (Entry<String, String[]> entry : mDistrictDatasMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            for (String tmpDistrict : value) {
                if (district.equals(tmpDistrict)) {
                    city = key;
                    find = true;
                    break;
                }
            }
            if (find)
                break;
        }

        if (TextUtils.isEmpty(city))
            return "";
        find = false;
        for (Entry<String, String[]> entry : mCitisDatasMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            for (String tmpCity : value) {
                if (city.equals(tmpCity)) {
                    province = key;
                    find = true;
                    break;
                }
            }
            if (find)
                break;
        }

        if (city.equals(province)) {
            area = city + district;
        } else {
            area = province + city + district;
        }

        return area;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
//        Util.Log(mCurrentDistrictName + " " + mCurrentAreaId);
        if (wheel == mWheelViewProvince) {
            updateCities();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
            mCurrentAreaId = mAreaIdDatasMap.get(mCurrentDistrictName);
        } else if (wheel == mWheelViewCity) {
            updateAreas();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
            mCurrentAreaId = mAreaIdDatasMap.get(mCurrentDistrictName);
        } else if (wheel == mWheelViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentAreaId = mAreaIdDatasMap.get(mCurrentDistrictName);
        }
        mCurrentAreaId = mAreaIdDatasMap.get(mCurrentCityName + mCurrentDistrictName);
        // Log.e("AreaID", mCurrentAreaId + "   is null?");
        // Log.e("mCurrentCityName", mCurrentCityName + "   is null?");
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        // TODO Auto-generated method stub

    }

    public interface OnAddressPickerChangListener {
        void onAddressPickerSelect(String province, String city, String district, String areaId);

        void onAddressPickerCancel();
    }

}
