package com.cy.frame.downloader.util;

/**
 * url转换
 * 
 * @author JLB6088
 * 
 */
public class UrlTranslator {

	private static final String GIONEE = ".gionee";
	private static final String GAME = "game.";
	private static final String TEST_PATH = ".gtest";

	private UrlTranslator() {

	}

	/**
	 * 转换url，如果是测试环境，则url需要加上.gtest表示，如果url里不包含.gionee或者 game. 直接返回
	 * @param url
	 * @return
	 */
	public static String translateUrl(String url) {

		// if (!Utils.isTestEnv()) { // 是否测试环境
		// return url;
		// }

		if (!(url.contains(GIONEE) && url.contains(GAME))) {
			return url;
		}

		if (url.contains(TEST_PATH)) {
			return url;
		}

		int index = url.indexOf(GIONEE);
		StringBuffer buffer = new StringBuffer(url);
		buffer.insert(index, TEST_PATH);
		return buffer.toString();

	}

}
