package com.chinonso.wearos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinonso.wearos.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclickbttn(View view){
        TextView txtv = findViewById(R.id.text);
        txtv.setText("hello chinonso");
    }
}