package com.cy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.statis.StatisValue;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager.UpgradeAppInfo;
import com.cy.frame.downloader.util.LaunchAppUtil;
import com.cy.global.BaseApplication;
import com.cy.global.WatchDog;
import com.cy.utils.storage.FileUtils;
import com.cy.utils.storage.GNStorageUtils;

@SuppressLint("NewApi")
public class Utils {

	private static boolean sIsAmigo = false;

	private static final String UPDATE_LAUNCH_ACTION = "com.gionee.intent.action.navil.NEWREMIND";
	private static final String PACKAGE_NAME = "packageName";
	private static final String REMIND_COUNT = "remindCount";
	private static final String LAUNCH_ACTIVITY = "launchActivity";

	private static final String COLORFUL_LAUNCHER_INFOS_KEY = "miss_infos";
	private static final String GAMEHALL_LAUNCH_ACTIVITY = "GNMainActivity";

	private static final String GAME_HALL_PKG = "gn.com.android.gamehall";
	private static final String AMIGAME_PKG = "com.android.amigame";

	private static final int NULL_FLAGS = 0;

	private static final int VERSION_CODES_LOLLIPOP = 21;
	private static final boolean IS_GIONEE_BRAND = false;

	private static final boolean TEST_ENV;
	private static final String TEST_FILE = "game1234567890test"; // test page

	private static final int MIN_WVGA_HEIGHT = 700;
	private static final int WVGA_HEIGHT = 800;
	private static final int MIN_HD_HEIGHT = 1180;
	private static final int HD_HEIGHT = 1280;

	static { // cyminge modify
	// String brand = FrameworkUtil.getPhoneBrand();
	// IS_GIONEE_BRAND = brand.trim().equalsIgnoreCase("GiONEE");

		TEST_ENV = Utils.isFileExisting(TEST_FILE);

	}

	public static boolean isTestEnv() {
		return TEST_ENV;
	}

	public static boolean isSDCardLowSpace() {
		return checkSDCard(Constant.LOW_SPACE_SIZE) == Constant.SD_LOW_SPACE;
	}

	public static boolean isWifiNet() {
		return isWifiNet(getNetworkType());
	}

	public static boolean isWifiNet(int networkType) {
		return networkType == ConnectivityManager.TYPE_WIFI;
	}

	public static boolean renameFile(String homeDir, String srcName, String destName) {
		if (!GNStorageUtils.isSDCardMounted()) {
			return false;
		}
		String srcFilePath = homeDir + File.separator + srcName;
		File srcFile = new File(srcFilePath);
		if (srcFile.exists()) {
			String destFilePath = homeDir + File.separator + destName;
			File destFile = new File(destFilePath);
			if (srcFile.renameTo(destFile)) {
				return true;
			}
		}
		return false;
	}

	public static String getPercentText(long progress, long total) {
		String percentText;
		if (progress == 0 || total <= 0) {
			percentText = "0%";
		} else if (progress < total) {
			percentText = (progress * 100 / total) + "%";
		} else {
			percentText = "99%";
		}
		return percentText;
	}

	public static int getSDCardState(String fileSize) { // fileSize:MB
		float size = calculateFileSizeMb(fileSize);
		return checkSDCard((long) size * Constant.MB);
	}

	public static RemoteViews getRemoteViews(String title, String detail) {
		final RemoteViews expandedView = new RemoteViews(BaseApplication.getAppContext().getPackageName(),
				R.layout.notification_item);
		expandedView.setTextViewText(R.id.notify_title, title);
		expandedView.setTextViewText(R.id.notify_detail, detail);
		expandedView.setImageViewResource(R.id.notify_icon, getGameHallIconId());
		return expandedView;
	}

	public static int getGameHallIconId() {
		try {
			ApplicationInfo info = BaseApplication.getAppContext().getPackageManager()
					.getApplicationInfo(BaseApplication.getAppContext().getPackageName(), 0);
			return info.icon;
		} catch (NameNotFoundException e) {
		}
		return 0;
	}

	public static boolean isLollipop() {
		return Build.VERSION.SDK_INT >= VERSION_CODES_LOLLIPOP;
	}

	public static boolean isMobileNet() {
		return isMobileNet(getNetworkType());
	}

	public static int getNetworkType() {
		NetworkInfo networkInfo = getActiveNetworkInfo();
		if (networkInfo == null) {
			return Constant.NETWORK_NO_NET;
		}
		return networkInfo.getType();
	}

	public static boolean isMobileNet(int networkType) {
		switch (networkType) {
		case ConnectivityManager.TYPE_MOBILE:
		case ConnectivityManager.TYPE_MOBILE_DUN:
		case ConnectivityManager.TYPE_MOBILE_HIPRI:
		case ConnectivityManager.TYPE_MOBILE_MMS:
		case ConnectivityManager.TYPE_MOBILE_SUPL:
			return true;

		default:
			return false;
		}
	}

	public static String combineGameUpgradeSource(String pkgName, String preSource) {
		if (GamesUpgradeManager.hasNewVersion(pkgName)) {
			if (GamesUpgradeManager.isIncreaseType(pkgName)) {
				preSource = StatisValue.combine(preSource, StatisValue.SPLIT);
			} else {
				preSource = StatisValue.combine(preSource, StatisValue.UPDATE);
			}
		}
		return preSource;
	}

	public static boolean isLogin() {
		// return AccountManager.getLoginStatus();
		return true;
	}

	public static boolean checkDownloadEnvironment(DownloadArgs args) {
		if (!hasNetwork()) {
			// ToastUtils.showLimited(R.string.no_net_msg);
			return false;
		}

		if (!GNStorageUtils.isSDCardMounted()) {
			// ToastUtils.showLimited(R.string.sdcard_error);
			return false;
		}
		if (!checkSpaceForRetry(args)) {
			// ToastUtils.showLimited(R.string.sdcard_low_space);
			return false;
		}

		if (Utils.needShowMobileHint()) {
			// BgDialogActivity.show(BgDialogActivity.DIALOG_MOBILE_HINT);
			return false;
		}

		return true;
	}

	private static boolean checkSpaceForRetry(DownloadArgs args) {
		int status = ButtonStatusManager.getButtonStatus(args);
		if (status == ButtonStatusManager.BUTTON_STATUS_FAILED) {
			DownloadInfo info = DownloadInfoMgr.getNormalInstance().getDownloadInfo(args.packageName);
			if (info != null && checkSDCard(info.mTotalSize) == Constant.SD_LOW_SPACE) {
				return false;
			}
		}
		return true;
	}

	public static int checkSDCard(long size) {
		int state = checkSDCardSpace(size);
		if (state != Constant.SD_LOW_SPACE) {
			return state;
		}

		if (!GNStorageUtils.switchSDCard()) {
			return Constant.SD_LOW_SPACE;
		}

		int newState = checkSDCardSpace(size);
		if (newState == Constant.SD_ENOUGH_ROOM) {
		}
		return newState;
	}

	@SuppressWarnings("deprecation")
	private static int checkSDCardSpace(long size) {
		String sdcardDir = GNStorageUtils.getSDCardDir();
		if (null == sdcardDir) {
			return Constant.SD_NOT_MOUNTED;
		}

		try {
			StatFs stat = new StatFs(sdcardDir);
			int blockSize = stat.getBlockSize();
			if ((double) stat.getAvailableBlocks() * (double) blockSize > Constant.MIN_SPACE_REQUIRED + (int) size) {
				return Constant.SD_ENOUGH_ROOM;
			}
		} catch (Exception e) {
			return Constant.SD_NOT_MOUNTED;
		}
		return Constant.SD_LOW_SPACE;
	}

	public static boolean needShowMobileHint() {
		// return !SettingUtils.getAllowByMobileNet() && Utils.isMobileNet();
		return false;
	}

	public static void delAllfiles(String gamePackage) {
		deleteFile(gamePackage + Constant.APK);
		deleteFile(gamePackage + ".apk.gntmp");
		deleteFile(gamePackage + ".patch");
		deleteFile(gamePackage + ".patch.gntmp");
	}

	public static boolean permitSilentInstall(String pkgName) {
		return Utils.isGioneeBrand() && !Utils.isClientSelf(pkgName);
	}

	public static boolean isGioneeBrand() {
		return IS_GIONEE_BRAND;
	}

	public static boolean isSamePackage(String fileName, String gamePackage) {
		return isSamePackage(GNStorageUtils.getHomeDirAbsolute(), fileName, gamePackage);
	}

	public static boolean isSamePackage(String homeDir, String fileName, String gamePackage) {
		boolean match = false;
		String filePath = homeDir + File.separator + fileName;
		try {
			PackageInfo info = getPackageInfoByPath(filePath);
			if (info != null && info.packageName != null && info.packageName.equals(gamePackage)) {
				match = true;
			}
		} catch (Exception e) {
			match = false;
		}
		return match;
	}

	public static PackageInfo getPackageInfoByPath(String path) {
		return getPackageInfoByPath(path, PackageManager.GET_ACTIVITIES);
	}

	public static PackageInfo getPackageInfoByPath(String path, int flags) {
		PackageManager packageManger = BaseApplication.getAppContext().getPackageManager();
		try {
			return packageManger.getPackageArchiveInfo(path, flags);
		} catch (Exception e) {
		}
		return null;
	}

	public static boolean isClientSelf(String packageName) {
		return getClientPkg().equals(packageName);
	}

	public static int getPkgTotalByte(String pkgName, String gameSize) {
		float totalByte = 0;
		if (GamesUpgradeManager.isIncreaseType(pkgName)) {
			UpgradeAppInfo info = GamesUpgradeManager.getOneAppInfo(pkgName);
			totalByte = calculateFileSizeMb(info.mPatchSize);
		} else {
			totalByte = calculateFileSizeMb(gameSize);
		}

		return (int) totalByte * Constant.MB;
	}

	private static float calculateFileSizeMb(String fileSize) {
		float size = 0;
		fileSize = trimSize(fileSize);
		try {
			size = Float.parseFloat(fileSize);
		} catch (NumberFormatException e) {
		}
		return size;
	}

	public static String getClientPkg() {
		return BaseApplication.getAppContext().getPackageName();
	}

	public static ArrayList<String> getPackageNames() {
		ArrayList<PackageInfo> res = getPackageInfo();
		ArrayList<String> packageNames = new ArrayList<String>();
		for (PackageInfo r : res) {
			packageNames.add(r.packageName);
		}
		return packageNames;
	}

	public static ArrayList<String> getDownloadedNames() {
		ArrayList<String> packageNames = new ArrayList<String>();
		String gameDir = GNStorageUtils.getHomeDirAbsolute();
		if (gameDir == null) {
			return packageNames;
		}

		File gameDirFile = new File(gameDir);
		File[] files = gameDirFile.listFiles();
		if (files == null) {
			return packageNames;
		}

		for (File file : files) {
			String fileName = file.getName();
			if (fileName.endsWith(Constant.APK)) {
				packageNames.add(getPackageName(fileName));
			}
		}
		return packageNames;
	}

	private static String getPackageName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf(Constant.APK));
	}

	public static boolean isSameClient(String pkgName) {
		// return GAME_HALL_PKG.equals(pkgName) || AMIGAME_PKG.equals(pkgName);
		return false;
	}

	public static String getPackageSignatureByPath(PackageManager pm, String path) {
		try {
			PackageInfo pkgInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_SIGNATURES);
			return pkgInfo.signatures[0].toCharsString();
		} catch (Exception e) {
		}
		return null;
	}

	public static String getFileMd5(File file) {
		byte[] digest = null;
		FileInputStream in = null;
		if (file == null) {
			return Constant.NULL;
		}
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			byte[] bytes = new byte[8192];
			in = new FileInputStream(file);
			int byteCount;
			while ((byteCount = in.read(bytes)) > 0) {
				digester.update(bytes, 0, byteCount);
			}
			digest = digester.digest();
		} catch (Exception cause) {
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception e) {
				}

			}
		}
		return (digest == null) ? null : byteArrayToString(digest);
	}

	private static String byteArrayToString(byte[] bytes) {
		StringBuilder ret = new StringBuilder(bytes.length << 1);
		for (byte aByte : bytes) {
			ret.append(Character.forDigit((aByte >> 4) & 0xf, 16));
			ret.append(Character.forDigit(aByte & 0xf, 16));
		}
		return ret.toString();
	}

	public static String getAppName(String packageName) {
		try {
			PackageManager pm = BaseApplication.getAppContext().getPackageManager();
			PackageInfo info = getPackageInfo(packageName);
			return info.applicationInfo.loadLabel(pm).toString();
		} catch (NameNotFoundException e) {
		} catch (Exception e) {
		}
		return Constant.EMPTY;
	}

	public static ArrayList<PackageInfo> getPackageInfo() {
		ArrayList<PackageInfo> packageInfos = getPackageInfos(PackageManager.GET_UNINSTALLED_PACKAGES);
		PackageInfo gameHallInfo = getPackageInfoByName(BaseApplication.getAppContext().getPackageName());
		packageInfos.add(gameHallInfo);
		return packageInfos;
	}

	public static ArrayList<PackageInfo> getPackageInfos(int flags) {
		PackageManager packageManger = BaseApplication.getAppContext().getPackageManager();
		ArrayList<PackageInfo> res = new ArrayList<PackageInfo>();
		try {
			List<PackageInfo> packs = packageManger.getInstalledPackages(flags);

			for (PackageInfo p : packs) {
				if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
					res.add(p);
				}
			}
		} catch (Exception e) {
		}
		return res;
	}

	public static PackageInfo getPackageInfoByName(String packageName) {
		return getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
	}

	public static PackageInfo getPackageInfo(String packageName, int flags) {
		PackageManager pm = BaseApplication.getAppContext().getPackageManager();
		try {
			return pm.getPackageInfo(packageName, flags);
		} catch (Exception e) {
		}
		return null;
	}

	private static PackageInfo getPackageInfo(String packageName) throws NameNotFoundException {
		return BaseApplication.getAppContext().getPackageManager().getPackageInfo(packageName, NULL_FLAGS);
	}

	public static void refreshCornerIcon(int num) {
		if (!Utils.isAmigo()) {
			return;
		}
		Context context = BaseApplication.getAppContext();
		String packageName = context.getPackageName();

		refreshNaviLauncherCorner(context, packageName, num);
		refreshColorfulLauncherCorner(context, packageName, num);
	}

	private static void refreshNaviLauncherCorner(Context context, String packageName, int num) {
		Intent intent = new Intent(UPDATE_LAUNCH_ACTION);
		intent.putExtra(PACKAGE_NAME, packageName);
		intent.putExtra(REMIND_COUNT, num);
		intent.putExtra(LAUNCH_ACTIVITY, packageName + "." + GAMEHALL_LAUNCH_ACTIVITY);
		context.sendBroadcast(intent);
	}

	private static void refreshColorfulLauncherCorner(Context context, String packageName, int num) {
		String key = packageName + "_" + packageName + "." + GAMEHALL_LAUNCH_ACTIVITY + "."
				+ COLORFUL_LAUNCHER_INFOS_KEY;
		try {
			Settings.System.putInt(context.getContentResolver(), key, num);
		} catch (Exception e) {
		}

		// try {
		// amigo.provider.AmigoSettings.putInt(context.getContentResolver(),
		// key, num);
		// } catch (Exception e) {
		// LogUtils.loge(TAG, LogUtils.getFunctionName(), e);
		// }
	}

	public static boolean isAmigo() {
		return sIsAmigo;
	}

	private static final int DEFAULT_RANDOM_HOUR = 4;

	public static boolean isUrlInvalid(String url) {
		return !isUrlValid(url);
	}

	public static boolean isUrlValid(String url) {
		return url != null && (url.startsWith(Constant.HTTP) || url.startsWith(Constant.HTTPS));
	}

	public static Resources getResources() {
		return WatchDog.getContext().getResources();
	}

	public static boolean hasNetwork() {
		return getActiveNetworkInfo() != null;
	}

	private static NetworkInfo getActiveNetworkInfo() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			return connectivity.getActiveNetworkInfo();
		} catch (Exception e) {
			return null;
		}
	}

	private static Object getSystemService(String service) {
		return WatchDog.getContext().getSystemService(service);
	}

	public static String getExceptionInfo(Throwable ex) {
		Writer errorWriter = new StringWriter();
		PrintWriter pw = new PrintWriter(errorWriter);
		ex.printStackTrace(pw);
		pw.close();
		return errorWriter.toString();
	}

	public static long getRandomTimeMs() {
		long max = DEFAULT_RANDOM_HOUR * DateUtils.HOUR_IN_MILLIS;
		return getRandomValue(max, 0);
	}

	public static long getRandomTimeMs(long maxMillis) {
		return getRandomValue(maxMillis, 0);
	}

	private static long getRandomValue(long max, long min) {
		return ((long) (Math.random() * (max - min))) + min;
	}

	// ==============================================================================================
	// download use

	public static String trimSize(String fileSize) {
		Matcher matcher = Pattern.compile("[M*B*]").matcher(fileSize);
		if (matcher.find()) {
			fileSize = fileSize.substring(0, matcher.start());
		}
		return fileSize;
	}

	public static String[] getDownloadBtnText() {
		return getResources().getStringArray(R.array.game_list_button_status);

	}

	public static String getUTF8Code(String value) {
		try {
			return URLEncoder.encode(value, Constant.UTF_8);
		} catch (UnsupportedEncodingException e) {
			return Constant.NULL;
		} catch (Exception e) {
			return Constant.NULL;
		}
	}

	public static String getStringMd5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes(Constant.UTF_8));
		} catch (Exception cause) {
			return Constant.EMPTY;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) {
				hex.append("0");
			}
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * 获取imei
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionCode(Context context) {
		String clientVersion = null;
		PackageInfo pInfo = null;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			if (pInfo != null) {
				clientVersion = String.valueOf(pInfo.versionCode);
			}
		} catch (NameNotFoundException e) {
			clientVersion = null;
		}
		return clientVersion;
	}

	public static String getPublicStatisArgs(boolean needCache) {
		// StringBuffer buffer = new StringBuffer();
		// String network = Constant.NULL;
		// if (!needCache) {
		// network = getSubNetwork();
		// }
		// buffer.append(FrameworkUtil.getDeviceModel()).append("_").append(getGameHallVersion()).append("_")
		// .append(FrameworkUtil.getGioneeRomVersion()).append("_")
		// .append(FrameworkUtil.getAndroidVersion()).append("_").append(getPhonePixels()[0])
		// .append("*").append(getPhonePixels()[1]).append("_").append(getChannel()).append("_")
		// .append(network).append("_").append(getEncodeIMEI());
		// return buffer.toString();
		return "";
	}

	public static void launchGame(String packageName) {
		try {
			Context context = BaseApplication.getAppContext();
			PackageManager pm = context.getPackageManager();
			Intent targetIntent = pm.getLaunchIntentForPackage(packageName);
			targetIntent.setPackage(null);
			LaunchAppUtil.startActivity(context, targetIntent);
		} catch (Exception e) {
//			ToastUtil.showLimited("应用不存在或不可用");
		}
	}

	public static boolean isFileExisting(String fileName) {
		return isFileExisting(GNStorageUtils.getHomeDirAbsolute(), fileName);
	}

	public static boolean isFileExisting(String homeDir, String fileName) {
		File file = getFile(homeDir, fileName);
		return file != null && file.exists();
	}

	public static File getFile(String fileName) {
		return getFile(GNStorageUtils.getHomeDirAbsolute(), fileName);
	}

	private static File getFile(String homeDir, String fileName) {
		if (!fileName.isEmpty() && GNStorageUtils.isSDCardMounted()) {
			String filePath = homeDir + File.separator + fileName;
			return new File(filePath);
		}
		return null;
	}

	public static boolean deleteFile(String fileName) {
		if (!GNStorageUtils.isSDCardMounted()) {
			return false;
		}
		return deleteFile(GNStorageUtils.getHomeDirAbsolute(), fileName);
	}

	public static boolean deleteFile(String parentFolder, String fileName) {
		File file = new File(parentFolder + File.separator + fileName);
		if (!file.exists()) {
			return false;
		}
		return FileUtils.delete(file);
	}

	public static int[] getPhonePixels() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int curWidth = metrics.widthPixels;
		int curHeight = metrics.heightPixels;
		if (curHeight >= MIN_WVGA_HEIGHT && curHeight <= WVGA_HEIGHT) {
			curHeight = WVGA_HEIGHT;
		}
		if (curHeight >= MIN_HD_HEIGHT && curHeight <= HD_HEIGHT) {
			curHeight = HD_HEIGHT;
		}
		return new int[] { curWidth, curHeight };
	}

}
