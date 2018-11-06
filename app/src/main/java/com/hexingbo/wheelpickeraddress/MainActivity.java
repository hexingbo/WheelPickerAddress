package com.hexingbo.wheelpickeraddress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hxb.hxbaddressselectionviewlibrary.view.AddressPicker;
import com.hxb.hxbaddressselectionviewlibrary.view.AddressPickerPopupWindow;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AddressPickerPopupWindow popupWindow = new AddressPickerPopupWindow(MainActivity.this);
        popupWindow.setOnAddressPickerSelectListener(new AddressPickerPopupWindow.OnAddressPickerSelectListener() {
            @Override
            public void onSelected(String Province, String City, String District, String PostCode) {
                Toast.makeText(MainActivity.this, Province + City + District, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.show();
            }
        });
    }
}
