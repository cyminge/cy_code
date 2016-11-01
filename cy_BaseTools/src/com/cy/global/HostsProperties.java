package com.cy.global;

import java.io.IOException;
import java.util.Properties;

import android.util.Log;

public class HostsProperties {
	
	private static final String TAG = "HostsProperties";
	
	private static final Properties mProperties = new Properties();
	
	public static final String PART_OFFICIAL_DOWNLOAD_URL = "PartOfficialDownloadUrl";
	
	static {
		try {
			Log.e("cyTest", "mProperties:"+mProperties);
			mProperties.load(HostsProperties.class.getResourceAsStream("/assets/hosts.properties"));
		} catch (IOException e) {
			Log.e(TAG, "HostsPropertiesOldman load property failed");
		}
	}
	
	public static String getOfficialDownloadUrlMark() {
		return mProperties.getProperty(PART_OFFICIAL_DOWNLOAD_URL);
	}
}
