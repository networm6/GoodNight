package com.goodnight.everyone;

import android.content.Context;
import android.app.admin.DeviceAdminReceiver;
import android.content.Intent;
import android.kz.Toastkeeper;

public class ScreenOffAdminReceiver extends DeviceAdminReceiver {
    
	void toa(Context c,String in,int ty){
		Toastkeeper.getInstance()
			.createBuilder(c)
			.setMessage(in)
			.setGravity(ty)
			.show();
	}
    @Override
    public void onEnabled(Context context, Intent intent) {
        toa(context,
				  "设备管理器行使能力",Toastkeeper.GRAVITY_TOP);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        toa(context,
				  "设备管理器没有行使能力",Toastkeeper.GRAVITY_RIGHT);
    }

}

