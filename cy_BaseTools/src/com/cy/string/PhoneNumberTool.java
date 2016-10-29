
package com.cy.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author beyond
 * @time 2014-2-14
 */
public class PhoneNumberTool {

	
	/**
	 * 判断手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
//		Pattern p = Pattern.compile("^((1[3-8][0-9]))\\d{8}$");
		Pattern p = Pattern.compile("[0-9]{3,32}");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}
}
