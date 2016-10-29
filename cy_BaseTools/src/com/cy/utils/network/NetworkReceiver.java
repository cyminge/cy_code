package com.cy.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cy.tracer.Tracer;

import de.greenrobot.event.EventBus;

public class NetworkReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (null != manager) {
				NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				
				boolean bGprs = (null != gprs && gprs.isConnected());
				boolean bWifi = (null != wifi && wifi.isConnected());
				
				if (!bGprs && !bWifi) {
					Tracer.i("NetworkReceiver", "no connected network");
					EventBus.getDefault().post(new NetworkEvent(NetworkEvent.NWK_NONE));
				} else if (bWifi) {
					Tracer.i("NetworkReceiver", "wifi");
					EventBus.getDefault().post(new NetworkEvent(NetworkEvent.NWK_WIFI));
				} else if (bGprs) {
					Tracer.i("NetworkReceiver", "mobile");
					EventBus.getDefault().post(new NetworkEvent(NetworkEvent.NWK_MOBILE));
				}
			}
			
		} catch (Exception e) {
			Tracer.debugException(e);
		}
	}
}
