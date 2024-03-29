package com.leielyq.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.LocationMessage;

public class MapPluginModule implements IPluginModule {

    int REQUEST_CODE_AUTOCOMPLETE = 123;
    private Conversation.ConversationType conversationType;
    private String targetId;

    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_ext_plugin_location_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        return "地图";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension extension) {
        //示例获取 会话类型、targetId、Context,此处可根据产品需求自定义逻辑，如:开启新的 Activity 等。
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();
        Intent intent = new Intent(fragment.getContext(), MainActivity.class);

        //只有通过 extension 中的 startActivityForPluginResult 才会返回到本类中的 onActivityResult
        extension.startActivityForPluginResult(intent,REQUEST_CODE_AUTOCOMPLETE, this);

    }

    @Override
    public void onActivityResult(int i, int i1, Intent data) {
        if (data != null) {
            double latitude = data.getDoubleExtra("latitude",0.00);
            double longitude = data.getDoubleExtra("longitude",0.00);
            String address = data.getStringExtra("address");
            String uri = data.getStringExtra("locuri");
            LocationMessage locationMessage = LocationMessage.obtain(latitude,longitude,address, Uri.parse(uri));
            RongIM.getInstance().sendLocationMessage(Message.obtain(targetId, conversationType, locationMessage), null, null, null);
        }
    }
}
