package com.cy.frame.downloader.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.cy.constant.Constant;
import com.cy.global.BaseApplication;
import com.cy.utils.Utils;
import com.cy.utils.sharepref.SharePrefUtil;

public class DifferenceUtils {

    private static final String AMI_PKG = "com.android.amigame";

    private static final String HALL_FOLDER = "youxi";
    private static final String AMI_FOLDER = "game";

    private static final String HALL_STATIS_ID = "18A182EB65CB5BCE";
    private static final String AMI_STATIS_ID = "ABEF68B016560F68";

    private static final String HALL_PREFERENCE_NAME = "accoutShare";
    private static final String AMI_PREFERENCE_NAME = "ami_accoutShare";

    private static final String SHARE_SDK_PREFERENCE_TEST = "account";
    private static final String SHARE_SDK_PREFERENCE = "pro_account";

    private static boolean sIsAmi = true;

    static {
        init(BaseApplication.getAppContext());
    }

    private static void init(Context context) {
        String pkgName = context.getPackageName();
        sIsAmi = AMI_PKG.equals(pkgName);
    }

    public static boolean isAmi() {
        return sIsAmi;
    }

    public static String getGameFolder() {
        if (isAmi()) {
            return AMI_FOLDER;
        }
        return HALL_FOLDER;
    }

    public static String getStatisId() {
        if (isAmi()) {
            return AMI_STATIS_ID;
        }
        return HALL_STATIS_ID;
    }

    public static boolean isFlowTipConfirmed() {
        if (isAmi() || !Utils.isGioneeBrand()) {
            return true;
        }
        return SharePrefUtil.getBoolean(Constant.FLOW_TIP, false);
    }


    public static String getPreferenceName() {
        if (isAmi()) {
            return AMI_PREFERENCE_NAME;
        }

        return HALL_PREFERENCE_NAME;
    }

    public static String getSdkSharePreference() {
        if (Utils.isTestEnv()) {
            return SHARE_SDK_PREFERENCE_TEST;
        }

        return SHARE_SDK_PREFERENCE;
    }
}
