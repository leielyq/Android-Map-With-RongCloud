package com.leielyq.mylibrary;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mapbox.mapboxsdk.Mapbox;

public class BaseActivity extends AppCompatActivity {
    public String mAccessToken = "pk.eyJ1IjoibGVpZWx5cSIsImEiOiJjazFsbzR4ZGgwNzRmM25wY3doM2ZxcWM2In0.ByPOPdfO_jxy8ofzH-qJgw";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(this, mAccessToken);
        setStatusBarTransparent();
        super.onCreate(savedInstanceState);
    }
    /**
     * 设置透明状态栏
     */
    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }
}
