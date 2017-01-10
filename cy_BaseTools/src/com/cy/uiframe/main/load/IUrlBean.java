package com.cy.uiframe.main.load;

import java.util.HashMap;

/**
 * IUrlBean 还需承担数据有效性检查的任务 =============== 
 * 
 * 是否请求数据有效， 对请求数据的通用结构进行解析。
 * 比如是否有数据，是否来自正确的请求地址。
 * （为了防公共wifi、运营商网络劫持，一般数据会加一个sign表示这是来自哪里的数据，如果没有这个sign就应该是被劫持了。）
 * （如果数据有加密还需要先进行解密再做判断）
 * @author JLB6088
 *
 */
public interface IUrlBean {
	 public String getCacheKey();

     public String postData(HashMap<String, String> map);
}
