package com.leielyq.mapboxwithrongcloud;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String mAccessToken = "pk.eyJ1IjoibGVpZWx5cSIsImEiOiJjazFsbzV0NTMwNzV4M2lsbzllODdlM296In0.7YfACRsimwYWVFkvRWAREA";
        Mapbox.getInstance(this, mAccessToken);

        //中国api
//        Mapbox.getInstance(this,
//        "pk.eyJ1IjoiYmR4MjciLCJhIjoiY2prZHJ1NHJtMzNvcDNxa3lnczMyaW5jcCJ9.Srw0di6BdZdG_-yaw-IouQ");

    }
}
