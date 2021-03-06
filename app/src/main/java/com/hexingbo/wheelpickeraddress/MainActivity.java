package com.hexingbo.wheelpickeraddress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hxb.address_picker.view.AddressPickerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddressPickerView.Builder(MainActivity.this, new AddressPickerView.OnAddressListener() {
                    @Override
                    public void onSelected(String Province, String City, String District, String PostCode) {
                        Toast.makeText(MainActivity.this, Province + City + District, Toast.LENGTH_LONG).show();
                    }
                }).build().show();
            }
        });
    }
}
