package com.cy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {

	public static void main(String[] args) {
		// boolean b = isRegularMatch_Let_sprit("</a>");
		// System.out.println(">>>> b = "+b);
		//
		// String s = parseLabel2Null("<a>");
		// System.out.println(">>>> s = "+s);
		// ========================================================================
		// ArrayList<CommentBean> list = getList();
		// ArrayList<CommentBean> listNew = removeListItem(list);
		// System.out.println("+++++ " + listNew.size());
		// ========================================================================
		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String ret =URLEncoder.encode(format.format(new Date()));
		// String ret =format.format(new Date());
		// System.out.println(" ret = "+ret);
		// ========================================================================
		// isContainGBK("fds你妈fds的但fds啊还有这cdddd样w的de事a");
		// ========================================================================
		// String str =
		// "\346\265\213\350\257\225\346\266\210\346\201\257\346\265\213\350\257\225\346\266\210\346\201\257";
		// System.out.println("str = "+str);
		// MessageOrBuilder.
		// int ram = (int)((Math.random()*10)%6+1);
		// System.out.println("ram = "+ram);
		// ========================================================================
		// // 获取当前月的第一天是星期几
		// Calendar current = Calendar.getInstance();
		// current.set(Calendar.DAY_OF_MONTH, 1);
		// System.out.println(current.get(Calendar.DAY_OF_WEEK) - 1);
		// // 获取设定月份有多少天
		// DateTime dt = new DateTime("2014-02-12 13:59:15");
		// int days=dt.getNumDaysInMonth();
		// System.out.println(">>>days = "+days);
		// // 获取设定月份有多少天
		// Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.YEAR,2014);
		// cal.set(Calendar.MONTH, 5-1);//Java月份才0开始算
		// int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
		// System.out.println(">>>dateOfMonth = "+dateOfMonth);

		// Date today = new Date();
		// Calendar c=Calendar.getInstance();
		// c.setTime(today);
		// int weekday=c.get(Calendar.DAY_OF_WEEK)- 1;
		// // System.out.println(weekday);
		//
		// System.out.println((127 >> 1)&1);
		// System.out.println(31&32);
		// System.out.println((((31 >> (2-1)) & 1) == 0));

		// String pTime = String.valueOf(System.currentTimeMillis());
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// Calendar c = Calendar.getInstance();
		// try {
		// c.setTime(format.parse(pTime));
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// int dayForWeek = 0;
		// if(c.get(Calendar.DAY_OF_WEEK) == 1){
		// dayForWeek = 7;
		// }else{
		// dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		// }
		// System.out.println(dayForWeek);

		// ========================================================================
//		 String str = "fdsa";
//		 System.out.println(">>> "+str.substring(str.length()-1));
		// try {
		// str = URLEncoder.encode(str, "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(">>> "+str);
		// System.out.println(">>> "+str.substring(2, 6));
		// System.out.println(">>> "+str.substring(6, 10));
		// System.out.println(">>> "+str.substring(10, 14));
		// System.out.println(">>> "+str.substring(0, str.length()-1));
		// System.out.println(">>> "+str.substring(str.length()-1));
		// ========================================================================
		// String str = "2014:0508122356";
		// System.out.println(">>> "+str.indexOf(":"));
		// System.out.println(">>> "+str.substring(0, str.indexOf(":")));
		// System.out.println(">>> "+str.substring(str.indexOf(":")+1, 10));
		// System.out.println(">>> "+str.substring(10, 14));
		// ========================================================================
		// Main main = new Main();
		// System.out.println(m.getClass());
		// System.out.println(Main.class); // 跟上面是一样的值
		// ========================================================================
//		ArrayList<Long> mUserUinBeanList = new ArrayList<Long>(); //
//		int[] i = { 10, 20, 30, 40, 50, 60, 70 };
//		for (int j = 0; j < i.length; j++) {
//			if (!mUserUinBeanList.contains((long) i[j])) {
//				mUserUinBeanList.add((long) i[j]);
//			}
//		}
//		System.out.println(mUserUinBeanList.subList(2, 3));
//		System.out.println(mUserUinBeanList.subList(3, 6));
		//
		// for (int k = 0; k < mUserUinBeanList.size(); k++) {
		// System.out.println(mUserUinBeanList.get(k));
		// }

		// for (int m = 0; m < mUserUinBeanList.size(); m++) {
		// if (mUserUinBeanList.contains((long)40)) {
		// mUserUinBeanList.remove((long) 40);
		// }
		// if (mUserUinBeanList.contains((long) 30)) {
		// mUserUinBeanList.remove((long) 30);
		// }
		// }
		// System.out.println("===================");
		// for (int k = 0; k < mUserUinBeanList.size(); k++) {
		// System.out.println(mUserUinBeanList.get(k));
		// }
		// ========================================================================
		// ArrayList<Integer> mUserUinBeanList = new ArrayList<Integer>(); //
		// int[] i = { 1, 50, 20, 30, 40, 20, 60, 30 };
		// for (int j = 0; j < i.length; j++) {
		// if (!mUserUinBeanList.contains(i[j])) {
		// mUserUinBeanList.add(i[j]);
		// }
		// }
		//
		// for (int k = 0; k < mUserUinBeanList.size(); k++) {
		// System.out.println(mUserUinBeanList.get(k));
		// }
		//
		// for (int m = 0; m < mUserUinBeanList.size(); m++) {
		// if (mUserUinBeanList.contains((Object) 40)) {
		// mUserUinBeanList.remove((Object) 40);
		// }
		// if (mUserUinBeanList.contains((Object) 30)) {
		// mUserUinBeanList.remove((Object) 30);
		// }
		// }
		// System.out.println("===================");
		// for (int k = 0; k < mUserUinBeanList.size(); k++) {
		// System.out.println(mUserUinBeanList.get(k));
		// }
		// ========================================================================
		// Main m = new Main();
		// User user1 = m.new User();
		// user1.uin = 20;
		// user1.uname = "cy1";
		//
		// User user2 = m.new User();
		// user2.uin = 40;
		// user2.uname = "cy2";
		//
		// User user3 = m.new User();
		// user3.uin = 30;
		// user3.uname = "cy1";
		//
		// User user4 = m.new User();
		// user4.uin = 50;
		// user4.uname = "cy2";
		//
		// User user5 = m.new User();
		// user5.uin = 20;
		// user5.uname = "cy3";
		// //
		// ArrayList<User> userList = new ArrayList<Main.User>();
		// if(!userList.contains(user1)){
		// userList.add(user1);
		// }
		// if(!userList.contains(user2)){
		// userList.add(user2);
		// }
		// if(!userList.contains(user3)){
		// userList.add(user3);
		// }
		// if(!userList.contains(user4)){
		// userList.add(user4);
		// }
		// if(!userList.contains(user5)){
		// userList.add(user5);
		// }
		// for(int k=0; k<userList.size(); k++){
		// System.out.println(userList.get(k).uin);
		// System.out.println(userList.get(k).uname);
		// }
		//
		// User user6 = m.new User();
		// user6.uin = 50;
		//
		// if(userList.contains(user6)){
		// userList.remove(user6);
		// }
		// System.out.println("=================");
		// for(int k=0; k<userList.size(); k++){
		// System.out.println(userList.get(k).uin);
		// System.out.println(userList.get(k).uname);
		// }
		// ========================================================================
		// String s = "12345";
		// s = s + 6;
		// System.out.println("s = "+s);
		// ========================================================================
		// String s = new String();
		// s.getBytes();
		// ========================================================================
		// String str = "r342qrfdsf4345resf\\.jpg";
		// String[] aa = str.split("\\.");
		// System.out.println(aa.length);
		// ========================================================================
		// String str = "r342/qrfd/sf4345resf.jpg";
		// System.out.println(str.substring(str.lastIndexOf("/")));

		// ========================================================================
		// // MD5加密

		// String str = "123456";
		// str = getMD5Str(str);
		// System.out.println(">>>> "+str);
		// str = "111111";
		// str = getMD5Str(str);
		// System.out.println(">>>> "+str);
		// ========================================================================
		// // 分割字符串
		// String str = "11,22,33,,,44";
		// String[] use = str.split(",");
		// System.out.println(use.length);
		// for (String u : use) {
		// if(u.equals("")){
		// continue;
		// }
		// Integer.parseInt(u);
		// System.out.println("u = "+u+"=aa");
		// }
		// =======================================================================
		// // 随机数
		// System.out.println((int)((Math.random()*9+1)*100000));
		// String random = String.valueOf((int) ((Math.random() * 9 + 1) *
		// 100000));
		// System.out.println("random = "+random);
		// Random r=new Random();
		// for(int i=0;i<50;i++) {
		// int temp=(r.nextInt(26) + 97);
		// System.out.println("temp = "+temp);
		// }
		//
		// int temp1= Math.abs(r.nextInt()) % 26 + 97;
		//
		// System.out.println("temp1 = "+temp1);
		// ======================================================================
		// String oriStr = "a,b,c,d,e,f,g,";
		// String splitStr = ",";
		// String addStr = "d";
		// System.out.println(split(oriStr, splitStr, addStr));
		// ======================================================================
		// 测试ArrayList containsAll 和equals方法
		// Main m = new Main();
		// User user1 = m.new User();
		// user1.uin = 20;
		// user1.uname = "cy1";
		//
		// User user2 = m.new User();
		// user2.uin = 40;
		// user2.uname = "cy2";
		//
		// User user3 = m.new User();
		// user3.uin = 30;
		// user3.uname = "cy1";
		//
		// User user4 = m.new User();
		// user4.uin = 20;
		// user4.uname = "cy1";
		//
		// User user5 = m.new User();
		// user5.uin = 40;
		// user5.uname = "cy2";
		//
		// User user6 = m.new User();
		// user6.uin = 30;
		// user6.uname = "cy1";
		//
		// ArrayList<User> userList1 = new ArrayList<Main.User>();
		// if(!userList1.contains(user1)){
		// userList1.add(user1);
		// }
		// if(!userList1.contains(user2)){
		// userList1.add(user2);
		// }
		// if(!userList1.contains(user3)){
		// userList1.add(user3);
		// }
		//
		// ArrayList<User> userList2 = new ArrayList<Main.User>();
		// if(!userList2.contains(user4)){
		// userList2.add(user4);
		// }
		// if(!userList2.contains(user5)){
		// userList2.add(user5);
		// }
		// if(!userList2.contains(user6)){
		// userList2.add(user6);
		// }
		// System.out.println("userList2.containsAll(userList1) = "+userList2.containsAll(userList1));
		// System.out.println("userList2.equals(userList1) = "+userList2.equals(userList1));
		// ======================================================================

		// HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
		// hash.put(1, 1);
		// String versionName = "v1.0.32";
		// int versionNameInt = Integer.parseInt(versionName.substring(1,
		// 2)+versionName.substring(3, 4)+versionName.substring(5));
		// System.out.println("versionNameInt = "+versionNameInt);

		// byte[] bytes = {"\u65b0"};
		// String string = new String(bytes, Charset.forName("UTF-8"));
		// System.out.println(string);
		// String myString = "\u65b0";
		// System.out.println(convert(myString));

		// System.out.println(">>1:"+formatGivenDate(1414740603));
		// System.out.println(">>2:"+formatGivenDate(1419302430*(long)1000));
		// System.out.println(">>3:"+formatGivenDate(1419306030*(long)1000));
		// System.out.println(">>4:"+1419306030);

		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// String ret = sdf.format(new Date());
		// System.out.println(ret);

		// System.out.println(">>5:"+System.currentTimeMillis()/1000);
		// System.out.println(">>6:"+formatGivenDate(1419580396*(long)1000));
		// int time = (int) (System.currentTimeMillis()/1000);
		// System.out.println(">>6:"+formatGivenDate(time*(long)1000));
//		System.out.println(">>7:" + formatGivenDate(1438320822 * (long) 1000));

		// try {
		// FileInputStream fileInputStream = new
		// FileInputStream("E:\\path.text");
		// boolean flag = fileInputStream.markSupported();
		// System.out.println(flag);
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// >>2:2014-12-23 11:40:00
		// >>3:2014-12-23 11:40:30

		// ======================================================================
		// String str = "我已阅读并同意《使用协议》";
		// String str1 = "我是中国人啊,你知不知道啊,同志,";
		// System.out.println(str.substring(0, str.indexOf("《")));
		// System.out.println(str1.substring(0, str1.lastIndexOf(",")));

		// String str =
		// "%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E5%85%AC%E5%8F%B8";
		// try {
		// System.out.println(URLDecoder.decode(str, "UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// readTxtFile("E:\\android_workspace\\cy_java_test\\assets\\string_exist.txt");

		// String str = "~!@#$%^&*?():/\\.";
		// System.out.println(str);
		// str = stringFilter(str);
		// System.out.println(str);

		// StringUtil.formatGivenDate(mLocMemberInfoList.get(item.getTitle()).upTime
		// * (long) 1000);

		// boolean a = true;
		// boolean b = false;
		// System.out.println(a != b);

		// System.out.println(7>>2);

		// List<Integer> list = new ArrayList<Integer>();
		// list.add(1);
		// list.add(0, 2);
		// list.add(3);
		// list.add(4);
		// list.add(0, 5);
		// list.add(0, 6);
		//
		// for(Integer i: list){
		// System.out.println(i);
		// }

		// String str = "bix98563227";
		// System.out.println(str.substring(str.length()-8, str.length()));

//		ArrayList<User> userList = new ArrayList<User>();
//		changeUserList(userList);
//		for (User user : userList) {
//			System.out.println(user.uname);
//		}

		// User user = new User();
		// user.uin = 3;
		//
		// User user1 = new User();
		// user1.uin = 6;
		//
		// for(int i=0; i<userList.size(); i++) {
		// if(userList.get(i).uin == 3) {
		// userList.remove(i);
		// i--;
		// } else if(userList.get(i).uin == 4) {
		// userList.remove(i);
		// i--;
		// } else if(userList.get(i).uin == 5) {
		// userList.remove(i);
		// }
		// }

//		int count = 0;
//		StringBuffer sb = new StringBuffer();
//		Iterator<User> iter = userList.iterator();
//		while (iter.hasNext()) {
//			User user = iter.next();
//			count ++;
//			if(iter.hasNext()) {
//				sb.append(user.uin+",");
//			} else {
//				sb.append(user.uin);
//			}
//			if (user.uin == 3) {
//				iter.remove();
//			} else if (user.uin == 4) {
//				iter.remove();
//			}
//		}
//		System.out.println("count:"+count);
//		System.out.println("sb:"+sb.toString());

		// String img = ",,,ccc";
		// String[] str = img.split(",");
		// System.out.println("length = "+str.length);
		// for(int i=0;i<str.length;i++) {
		// System.out.println("str["+i+"] = "+str[i]);
		// }

		// System.out.println(AD_SHOW_TYPE_NOTIFICATION & 14);
		// System.out.println(new Date());
		// System.out.println(System.currentTimeMillis());
		// System.out.println((int)(System.currentTimeMillis()/1000));

//		 Set<User> userSet = new TreeSet<User>();
//		 User user = new User();
//		 user.uin = 3;
//		 userSet.add(user);

		// TipAdvertiseGson g = new TipAdvertiseGson();
		// Advertise adTest = g.new Advertise();
		// adTest.adid = 20;
		// adTest.type = 31;
		// adTest.start = (int) (System.currentTimeMillis() / 1000);
		// adTest.end = (int) (System.currentTimeMillis() / 1000) + 4 * 3600;
		// adTest.action = 1;
		// adTest.url = "";
		// adTest.text = "";
		// adTest.frequency = 50;
		//
		// ImageBean b = g.new ImageBean();
		// b.banner = "group1/M00/00/D7/CqiwlFShCcuEEzMbAAAAALUOVjU855.jpg";
		// b.dialog = "group1/M00/00/D7/CqiwlFShCcuEEzMbAAAAALUOVjU855.jpg";
		// b.launch = "group1/M00/00/D7/CqiwlFShCcuEEzMbAAAAALUOVjU855.jpg";
		// // b.icon =
		// // "group1/M00/00/D7/CqiwlFShCcuEEzMbAAAAALUOVjU855.jpg";
		// adTest.image = b;
		//
//		 AdvertiseContent adContent = g.new AdvertiseContent();
//		 adContent.ad = adTest;
//		 g.content = adContent;
//		 g.vsn = "android";
//		 String str = "{\"content\":\"咯啊撸\",\"type\":\"text\",\"size\":0}";
//		 Gson gson = new Gson();
//		 MessageContentGson bean = gson.fromJson(str, MessageContentGson.class);
//		 System.out.println(bean.content);
//		 String Str = gson.toJson(g);
//		 System.out.println(Str);
		//
//		 TipAdvertiseRateGson reteGson = new TipAdvertiseRateGson();
		//
		// ArrayList<AdvertiseRate> rateTest = new
		// ArrayList<TipAdvertiseRateGson.AdvertiseRate>();
		// AdvertiseRate advertiseRate1 = reteGson.new AdvertiseRate();
		// advertiseRate1.type = ADVERTISEMENT_TIPS_RATE_TYPE_NOTIFYCATION;
		// advertiseRate1.times = 30;
		// advertiseRate1.seq = 1;
		// AdvertiseRate advertiseRate2 = reteGson.new AdvertiseRate();
		// advertiseRate2.type = ADVERTISEMENT_TIPS_RATE_TYPE_BANNER;
		// advertiseRate2.times = 30;
		// advertiseRate2.seq = 1;
		// AdvertiseRate advertiseRate3 = reteGson.new AdvertiseRate();
		// advertiseRate3.type = ADVERTISEMENT_TIPS_RATE_TYPE_APPDIALOG;
		// advertiseRate3.times = 30;
		// advertiseRate3.seq = 1;
		// AdvertiseRate advertiseRate4 = reteGson.new AdvertiseRate();
		// advertiseRate4.type = ADVERTISEMENT_TIPS_RATE_TYPE_PTP;
		// advertiseRate4.times = 30;
		// advertiseRate4.seq = 1;
		// AdvertiseRate advertiseRate5 = reteGson.new AdvertiseRate();
		// advertiseRate5.type = ADVERTISEMENT_TIPS_RATE_TYPE_LAUNCH;
		// advertiseRate5.times = 30;
		// advertiseRate5.seq = 1;
		// AdvertiseRate advertiseRate6 = reteGson.new AdvertiseRate();
		// advertiseRate6.type = ADVERTISEMENT_TIPS_RATE_TYPE_ICON;
		// advertiseRate6.times = 30;
		// advertiseRate6.seq = 1;
		// AdvertiseRate advertiseRate7 = reteGson.new AdvertiseRate();
		// advertiseRate7.type = ADVERTISEMENT_TIPS_RATE_TYPE_LOCK;
		// advertiseRate7.times = 30;
		// advertiseRate7.seq = 1;
		// AdvertiseRate advertiseRate8 = reteGson.new AdvertiseRate();
		// advertiseRate8.type = ADVERTISEMENT_TIPS_RATE_TYPE_SYSDIALOG;
		// advertiseRate8.times = 30;
		// advertiseRate8.seq = 1;
		//
		// rateTest.add(advertiseRate1);
		// rateTest.add(advertiseRate2);
		// rateTest.add(advertiseRate3);
		// rateTest.add(advertiseRate4);
		// rateTest.add(advertiseRate5);
		// rateTest.add(advertiseRate6);
		// rateTest.add(advertiseRate7);
		// rateTest.add(advertiseRate8);
		//
//		 AdvertiseRateContent rateContent = reteGson.new
//		 AdvertiseRateContent();
//		 rateContent.ad_rate = rateTest;
//		 reteGson.content = rateContent;
//		 reteGson.vsn = "android";
//		 Str = gson.toJson(reteGson);
//		 System.out.println(Str);

		// String test =
		// "{\"style\": [{ \"background_color\": \"000000\"}],\"action\": [{\"label\": \"Clicks\",\"type\": \"wap\",\"data\": \"http://c.vserv.pp=1\"}],\"menu\": [[{\"action\": 0}]]}";
		// String test =
		// "{\"style\": [{ \"background_color\": \"000000\"}],\"action\": [{\"label\": \"Clicks\",\"type\": \"wap\",\"data\": \"http://c.vserv.pp=1\"}],\"menu\": [[{\"action\": 0}]]}";
		// Gson gson = new Gson();
		// MANGE ge = gson.fromJson(test, MANGE.class);
		// System.out.println(ge.style.size());

//		 putToMap();
//		
//		 User testUser1 = map.get(1L);
//		 testUser1.uin = 4;
//		 testUser1.uname = "44";
//		
//		 System.out.println("1>"+map.get(1L).uin);
//		 System.out.println("1>"+map.get(1L).uname);
//		
//		 Map<Long, User> myMap = getUserMap();
//		 User testUser2 = myMap.get(1L);
//		 testUser2.uin = 4;
//		 testUser2.uname = "44";
//		 
//		 System.out.println("2>"+myMap.get(1L).uin);
//		 System.out.println("2>"+myMap.get(1L).uname);
//		 
//		 System.out.println(myMap.hashCode());
//		 System.out.println(map.hashCode());
//		 
//		 ArrayList<String> a = new ArrayList<String>();
//		 a.add("aaa");
//		 @SuppressWarnings("unchecked")
//		 ArrayList<String> b = (ArrayList<String>) a.clone();
//		 b.add("aaa");
//		 
//		 System.out.println(a.hashCode());
//		 System.out.println(b.hashCode());

		// System.out.println(1 & 5);

//		 Thread th1 = new Thread("我是3线程");
//         Thread th2 = new Thread("我是3线程");
//         Thread th3 = new Thread("我是3线程");
//         th1.start();
//         th2.start();
//         th3.start();
//         
//         System.out.println(th1.getId()+","+th1.hashCode());
//         System.out.println(th2.getId()+","+th2.hashCode());
//         System.out.println(th3.getId()+","+th3.hashCode()+","+th3.getName());
//         System.out.println(Thread.currentThread().hashCode());

//		 String str = "aaa";
//		 changeStr(str);
//		 System.out.println(str);
//		 
//		 Map<Long, User> haha = new HashMap<Long, User>();
//		 User u1 = new User(1,"11");
//		 User u2 = new User(2,"22");
//		 User u3 = new User(3,"33");
//		 haha.put(1L, u1);
//		 haha.put(2L, u2);
//		 haha.put(3L, u3);
////		 changeMap(haha);
//		 
//		 System.out.println("3>"+haha.get(1L).uin);
//		 System.out.println("3>"+haha.get(1L).uname);

//		 System.out.println(CHECK_COMPANY_RET.values());
//		 
//		 System.out.println(CHECK_COMPANY_RET.INEXIST.getRet());
//		 System.out.println(CHECK_COMPANY_RET.INEXIST.name());
//		 System.out.println(CHECK_COMPANY_RET.INEXIST.ordinal());
//		 System.out.println(CHECK_COMPANY_RET.INEXIST.toString());
//		 
//		 System.out.println(CHECK_COMPANY_RET.EXIST_NOT_FULL.getRet());
//		 System.out.println(CHECK_COMPANY_RET.EXIST_NOT_FULL.name());
//		 System.out.println(CHECK_COMPANY_RET.EXIST_NOT_FULL.ordinal());
//		 System.out.println(CHECK_COMPANY_RET.EXIST_NOT_FULL.toString());
//		 
//		 for(CHECK_COMPANY_RET ret : CHECK_COMPANY_RET.values()) {
//			 System.out.println(ret.ordinal());
//		 }
//		 
//		 
//		 System.out.println(Operation.PLUS.apply(1, 3));

//		 for(;;){
//	            System.out.println("ss");
//	        }

//		 String str = "\u6377\u514b\u8bed\u65b0\u589e\u529f\u80fd\u5982\u4e0b\uff1a 1\u3001\u6d4b\u8bd5 2\u3001\u6d4b\u8bd5";
//		 str = decodeUnicode(str);
//		 System.out.println(str);
//		 String str1 = "{"pt":"\"1. Added firend function, your can have yourself's friend;\"","fr":"\"1. Added firend function, your can have yourself's friend;\"","en":"\"1. Added firend function, your can have yourself's friend;\"","it":"\"1. Added firend function, your can have yourself's friend;\"","es":"\"1. Added firend function, your can have yourself's friend;\"","ru":"\"1. Added firend function, your can have yourself's friend;\"","de":"\"1. Added firend function, your can have yourself's friend;\"","zh":"1. \u65b0\u589e\u597d\u53cb\u529f\u80fd\uff0c\u60a8\u53ef\u4ee5\u62e5\u6709\u81ea\u5df1\u7684\u597d\u53cb\u4e86\uff1b","cs":"\"1. Added firend function, your can have yourself's friend;\"","":"\u6377\u514b\u8bed\u65b0\u589e\u529f\u80fd\u5982\u4e0b\uff1a 1\u3001\u6d4b\u8bd5 2\u3001\u6d4b\u8bd5"}";

//		Gson gson = new Gson();
//		
//		TestGson gson1 = new TestGson();
//		gson1.adid = 11L;
//		gson1.type = 22;
//		String str = gson.toJson(gson1);
//		System.out.println(str);
//		
//		TestGson1 gson2 = new TestGson1();
//		gson2 = gson.fromJson(str, TestGson1.class);
//		System.out.println(gson2.adid);

//		System.out.println(11%5);

		// 传递对象，修改了其中的值，则这个对象的值改变了，引用传递
//		User user1 = new Main.User();
//		user1.uin = 20;
//		user1.uname = "cy1";
//		System.out.println("uin = "+user1.uin);
//		changeUser(user1);
//		System.out.println("uin = "+user1.uin);
		 
		// 测试时间
//		System.out.println(new Date(1443438709L*1000));
//		System.out.println(new Date(1443438990700L));
//		System.out.println(System.currentTimeMillis());
//		System.out.println(System.currentTimeMillis()/1000);
		
//		Scanner input = new Scanner(System.in);
//		System.out.println("====");
//		input.next();
//        System.out.println("===="+input.nextInt());
//        System.out.println("===="+input.next());
//        input.close();
        
        
//		Scanner sc = new Scanner(System.in);
//		System.out.println("请输入你的姓名：");
//		String name = sc.nextLine();
//		System.out.println("请输入你的年龄：");
//		int age = sc.nextInt();
//		System.out.println("请输入你的工资：");
//		float salary = sc.nextFloat();
//		System.out.println("你的信息如下：");
//		System.out.println("姓名："+name+"\n"+"年龄："+age+"\n"+"工资："+salary);
		
//		 ArrayList<String> list = new ArrayList<String>();
//		 list.add("http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feaf5b1a6c.png.webp");
//		 list.add("http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feaf9ba720.png.webp");
//		 list.add("http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feafca1ca1.png.webp");
//		 list.add("http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feafce79d2.png.webp");
//		 list.add("http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feafece79a.png.webp");
//		 
//		 TestGson2 g2 = new TestGson2();
//		 g2.fullPicture = list;
//		 g2.pictureIndex = 2;
//		 gson.toJson(g2);
//		 String gStr = "{"
//		 		+ "\"fullPicture\":"
//		 		+ "["
//		 		+ "\"http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feaf5b1a6c.png.webp\","
//		 		+ "\"http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feaf9ba720.png.webp\","
//		 		+ "\"http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feafca1ca1.png.webp\","
//		 		+ "\"http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feafce79d2.png.webp\","
//		 		+ "\"http://s.game.3gtest.gionee.com/Attachments/dev/screens/2015/06/04/556feafece79a.png.webp\""
//		 		+ "],"
//		 		+"\"pictureIndex\":"+2
//		 		+ "}";
//		 TestGson2 g22 = gson.fromJson(gStr, TestGson2.class);
//		 for(String s :g22.fullPicture) {
//			 System.out.println("fullPicture = "+s);
//		 }
//		 System.out.println("pictureIndex"+g22.pictureIndex);
	    
//	    double red = 0.3;
//        double green = 0.59;
//        double blue = 0.11;
//        int corfinal;
//        
//        int r = 4;
//        int g = 67;
//        int b = 120;
//	    
//        corfinal = (int)(red*r+green*g+blue*b);
//        
//	    System.out.println(corfinal);
	    
//	    AdvertiseRateContent rateContent = reteGson.new
//        AdvertiseRateContent();
//        rateContent.ad_rate = rateTest;
//        reteGson.content = rateContent;
//        reteGson.vsn = "android";
//        Str = gson.toJson(reteGson);
//        System.out.println(Str);
	    
//	    UserRecall userRecall = new UserRecall();
//	    userRecall.gameId = 359;
//	    userRecall.packageName = "com.joym.xiongdakuaipao.gl.am";
//	    List<String> list = new ArrayList<String>();
//	    list.add("com.cy.xiongchumo");
//	    list.add("com.cy.xiaoxiaole");
//	    list.add("com.cy.haoziwei");
//	    userRecall.otherChannelGame = list;
//	    
//	    UserRecall userRecall1 = new UserRecall();
//        userRecall1.gameId = 359;
//        userRecall1.packageName = "com.joym.xiongdakuaipao.gl.am";
//        List<String> list1 = new ArrayList<String>();
//        userRecall1.otherChannelGame = list1;
//	    
//	    Gson gson = new Gson();
//	    
//	    List<UserRecall> packageList = new ArrayList<UserRecall>();
//	    packageList.add(userRecall1);
//	    packageList.add(userRecall);
//	    UserRecallList userRecallList = new UserRecallList();
//	    userRecallList.userRecall = packageList;
//	    userRecallList.version = 1;
//	    String retList = gson.toJson(userRecallList);
//        System.out.println("retList = "+retList);
	    
//	    USerRecall Gson String = {"gameId":359,"packageName":"com.joym.xiongdakuaipao.gl.am","packageNameList":["com.cy.xiongchumo","com.cy.xiaoxiaole","com.cy.haoziwei"]}
//	    retList = [{"gameId":359,"packageName":"com.joym.xiongdakuaipao.gl.am","packageNameList":["com.cy.xiongchumo","com.cy.xiaoxiaole","com.cy.haoziwei"]},{"gameId":359,"packageName":"com.joym.xiongdakuaipao.gl.am","packageNameList":["com.cy.xiongchumo","com.cy.xiaoxiaole","com.cy.haoziwei"]}]
	
//	    ActivationCodeSubInfo subInfo = new ActivationCodeSubInfo();
//	    subInfo.bannerUrl = "https://www.baidu.com";
//	    subInfo.codeCounts = 1000;
//	    subInfo.jumpUrl = "https://www.baidu.com";
//	    subInfo.codeVip = "VIP5";
//	    subInfo.codeCounts = 0;
//	    
//	    ActivationCodeInfo info = new ActivationCodeInfo();
//	    info.sign = "GameSDK";
//	    info.data = subInfo;
//	    
//	    Gson gson = new Gson();
//	    String retGson = gson.toJson(info);
//	    System.out.println(retGson);
	    
	    ArrayList<User> list = new ArrayList<User>();
	    User user1 = new User();
	    user1.uin = 11;
	    User user2 = new User();
        user2.uin = 22;
        User user3 = new User();
        user3.uin = 33;
        User user4 = new User();
        user4.uin = 44;
        list.add(user1);
//        list.add(user2);
//        list.add(user3);
//        list.add(user4);
	    
//	    for(User user : list) {
//	        System.out.println(user.uin);
//	    }
//	    for (int i = 0; i < list.size(); i++) {
//	        System.out.println("--> "+i);
//	        if(list.get(i).uin == 11) {
//	            list.remove(i);
//	            i--;
//	        }
////            PackageInfo p = Utils.getPackageInfoByName(sAppInfo.get(i).mPackageName);
////            if (p == null || p.versionCode >= sAppInfo.get(i).mNewVersionCode) {
////                sAppInfo.remove(i);
////                i--;
////            }
//        }
	    
	    String id = null;
        String gainType = null;
        System.out.println(combine(id, gainType));
	}
	
	public static String combine(String... source) {
        StringBuffer buffer = new StringBuffer();
        for (String s : source) {
            buffer.append(s).append("_");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }
	
	public static class ActivationCodeSubInfo {
	    public String bannerUrl;
	    public int codeCounts;
	    public String jumpUrl;
	    public String codeVip;
	    public int codeStatus;
	}
	
	public static class ActivationCodeInfo {
	    public String sign;
	    public ActivationCodeSubInfo data;
	    
//	    1、sign = "GameSDK"
//	    2、data = 
//	        [
//	        3、bannerUrl       banner图片url
//	        4、codeCounts    激活码总数
//	        5、jumpUrl          要跳转到的激活码
//	        6、codeVip          激活码需要达到的vip等级
//	        7、codeStatus （0:能领取；1:领取完；2:等级不够）
//	        ]
	}
	
	public static class UserRecallList {
	    int version;
	    List<UserRecall> userRecall;
	}
	
	public static class UserRecall {
	    long gameId;
	    String packageName;
	    List<String> otherChannelGame;
	}
	
	public static class TestGson2 {
		public ArrayList<String> fullPicture;
		public int pictureIndex;
	}
	
	public static void changeUser(User user) {
		user.uin = 21;
	}

	public static class TestGson {
		public long adid;               // 广告ID
		public int type;               // 数字内容或运算，表示2|4，Banner+App弹窗
	}

	public static class TestGson1 {
		public long adid;               // 广告ID
		public int type;               // 数字内容或运算，表示2|4，Banner+App弹窗
	}

	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx    
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();

	}

	public enum CHECK_COMPANY_RET {
		INEXIST(-1), EXIST_NOT_FULL(-2), EXIST_FULL(-3), REQUEST_ERROR(-4), REQUEST_ILLEGAL(-5), IS_PAYER(-6);
		private int ret;

		private CHECK_COMPANY_RET(int ret) {
			this.ret = ret;
		}

		public int getRet() {
			return ret;
		}
	}

	public enum Operation {
		PLUS {
			double apply(double x, double y) {
				return x + y;
			}
		},
		MINUS {
			double apply(double x, double y) {
				return x - y;
			}
		};

		abstract double apply(double x, double y);
	}

	public static void changeMap(Map<Long, User> map) {
		User u1 = new User(4, "44");
		User u2 = new User(5, "55");
		User u3 = new User(6, "66");
		map.put(1L, u1);
		map.put(2L, u2);
		map.put(3L, u3);
	}

	public static void changeStr(String str) {
		str = "bbb";
	}

	public static void putToMap() {
		User u1 = new User(1, "11");
		User u2 = new User(2, "22");
		User u3 = new User(3, "33");
		map.put(1L, u1);
		map.put(2L, u2);
		map.put(3L, u3);
	}

	static Map<Long, User> map = new HashMap<Long, User>();

	public static Map<Long, User> getUserMap() {

//		User u1 = new User(1, "11");
//		User u2 = new User(2, "22");
//		User u3 = new User(3, "33");
//		map.put(1L, u1);
//		map.put(2L, u2);
//		map.put(3L, u3);

		return map;
	}

	public <T> void go(T t) {
		System.out.println("generic function" + t);
	}

	public void go(String str) {
		System.out.println("normal function" + str);
	}

	public static class Fruit {
		String name;
		int type;
	}

	public static class Apple extends Fruit {
		String name;
		int type;
	}

	// private static final String ADVERTISEMENT_TIPS_RATE_TYPE_NOTIFYCATION =
	// "notification";
	// private static final String ADVERTISEMENT_TIPS_RATE_TYPE_BANNER =
	// "banner";
	// private static final String ADVERTISEMENT_TIPS_RATE_TYPE_APPDIALOG =
	// "appDialog";
	// private static final String ADVERTISEMENT_TIPS_RATE_TYPE_PTP = "ptp";
	// private static final String ADVERTISEMENT_TIPS_RATE_TYPE_LAUNCH =
	// "launch";
	// private static final String ADVERTISEMENT_TIPS_RATE_TYPE_ICON = "icon";
	// private static final String ADVERTISEMENT_TIPS_RATE_TYPE_LOCK = "lock";
	// private static final String ADVERTISEMENT_TIPS_RATE_TYPE_SYSDIALOG =
	// "sysDialog";
	//
	// private static final int AD_SHOW_TYPE_NOTIFICATION = (1 << 0);
	// private static final int AD_SHOW_TYPE_BANNER = (1 << 1);
	// private static final int AD_SHOW_TYPE_APP_DIALOG = (1 << 2);
	// private static final int AD_SHOW_TYPE_PTP = (1 << 3);
	// private static final int AD_SHOW_TYPE_LAUNCH = (1 << 4);
	// private static final int AD_SHOW_TYPE_ICON = (1 << 5);
	// private static final int AD_SHOW_TYPE_LOCK = (1 << 6);
	// private static final int AD_SHOW_TYPE_SYS_DIALOG = (1 << 7);

	public static void changeUserList(ArrayList<User> userList) {
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.uin = i;
			user.uname = "str" + i;
			userList.add(user);
		}
	}

	public static String stringFilter(String str) throws PatternSyntaxException {

		String regEx = "[/\\:*%?<>|\"\n\t]";

		Pattern p = Pattern.compile(regEx);

		Matcher m = p.matcher(str);

		return m.replaceAll("");

	}

	/**
	 *      * 功能：Java读取txt文件的内容      * 步骤：1：先获得文件句柄      *
	 * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取      * 3：读取到输入流后，需要读取生成字节流      *
	 * 4：一行一行的输出。readline()。      * 备注：需要考虑的是异常情况      * @param filePath      
	 */
	public static void readTxtFile(String filePath) {
		ArrayList<String> list = readTidyTxtFile("E:\\android_workspace\\cy_java_test\\assets\\string_all.txt");
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// System.out.println(lineTxt);
					lineTxt = lineTxt.replace(" ", "");
					lineTxt = lineTxt.replace("\t", "");
					if (!lineTxt.startsWith("<string")) {
						continue;
					}
					String[] all = lineTxt.split("\"");
					// System.out.println(all[1]);
					if (!list.contains(all[1])) {
						System.out.println(lineTxt);
					}
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件1");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错1");
		}
	}

	public static ArrayList<String> readTidyTxtFile(String filePath) {
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				ArrayList<String> list = new ArrayList<String>();
				while ((lineTxt = bufferedReader.readLine()) != null) {
					lineTxt = lineTxt.replace(" ", "");
					lineTxt = lineTxt.replace("\t", "");
					// if(!lineTxt.startsWith("<string")){
					// continue;
					// }
					// System.out.println(lineTxt);
					list.add(lineTxt);
				}
				read.close();
				return list;
			} else {
				System.out.println("找不到指定的文件2");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错2");
		}
		return null;
	}

	public static String formatGivenDate(long givenDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ret = sdf.format(new Date(givenDate));
		return ret;
	}

//	public static String decodeUnicode(String theString) {
//		char aChar;
//		int len = theString.length();
//		StringBuffer outBuffer = new StringBuffer(len);
//		for (int x = 0; x < len;) {
//			aChar = theString.charAt(x++);
//			if (aChar == '\\') {
//				aChar = theString.charAt(x++);
//				if (aChar == 'u') {
//					// Read the xxxx
//					int value = 0;
//					for (int i = 0; i < 4; i++) {
//						aChar = theString.charAt(x++);
//						switch (aChar) {
//						case '0':
//						case '1':
//						case '2':
//						case '3':
//						case '4':
//						case '5':
//						case '6':
//						case '7':
//						case '8':
//						case '9':
//							value = (value << 4) + aChar - '0';
//							break;
//						case 'a':
//						case 'b':
//						case 'c':
//						case 'd':
//						case 'e':
//						case 'f':
//							value = (value << 4) + 10 + aChar - 'a';
//							break;
//						case 'A':
//						case 'B':
//						case 'C':
//						case 'D':
//						case 'E':
//						case 'F':
//							value = (value << 4) + 10 + aChar - 'A';
//							break;
//						default:
//							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
//						}
//					}
//					outBuffer.append((char) value);
//				} else {
//					if (aChar == 't')
//						aChar = '\t';
//					else if (aChar == 'r')
//						aChar = '\r';
//					else if (aChar == 'n')
//						aChar = '\n';
//					else if (aChar == 'f')
//						aChar = '\f';
//					outBuffer.append(aChar);
//				}
//			} else
//				outBuffer.append(aChar);
//		}
//		return outBuffer.toString();
//
//	}

	public static String convert(String utfString) {
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = utfString.indexOf("\\", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if (i + 5 < utfString.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
			}
		}

		return sb.toString();
	}

	// private static String split(String oriStr, String splitStr, String
	// addStr) {
	// String[] str = oriStr.split(splitStr);
	// for (String s : str) {
	// if ("".equals(s)) {
	// continue;
	// }
	// if (addStr.equals(s)) {
	// continue;
	// }
	// oriStr = oriStr + addStr + splitStr;
	// break;
	// }
	// return oriStr;
	// }

	private static String split(String oriStr, String splitStr, String addStr) {
		String[] str = oriStr.split(splitStr);
		boolean isRepeat = false;
		for (String s : str) {
			if ("".equals(s)) {
				isRepeat = true;
				continue;
			}
			if (addStr.equals(s)) {
				isRepeat = true;
				continue;
			}
		}
		if (!isRepeat) {
			oriStr = oriStr + addStr + splitStr;
		}
		return oriStr;
	}

	/**
	 * MD5 加密
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	public static class User {
		public int uin;
		public String uname;

		public User() {

		}

		public User(int uin, String uname) {
			this.uin = uin;
			this.uname = uname;
		}

		// @Override
		// public boolean equals(Object obj) {
		// if (obj instanceof User) {
		// User bean = (User) obj;
		// return uname.equals(bean.uname);
		// }
		// return super.equals(obj);
		// }

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof User) {
				User bean = (User) obj;
				return uin == bean.uin; // && uname.equals(bean.uname)
			}
			return super.equals(obj);
		}
	}

	/**
	 * 删除ArrayList元素需要用到迭代器
	 * 
	 * @param list
	 * @return
	 */
	public static ArrayList<CommentBean> removeListItem(ArrayList<CommentBean> list) {
		Iterator<CommentBean> iter = list.iterator();
		while (iter.hasNext()) {
			CommentBean bean = iter.next();
			if (bean.sztype == 7) {
				iter.remove();
			} else if (bean.sztype == 6) {
				iter.remove();
			}
		}
		return list;
	}

	public static ArrayList<CommentBean> getList() {
		ArrayList<CommentBean> list = new ArrayList<CommentBean>();
		CommentBean bean1 = new CommentBean();
		bean1.sztype = 1;
		list.add(bean1);
		CommentBean bean2 = new CommentBean();
		bean2.sztype = 2;
		list.add(bean2);
		CommentBean bean3 = new CommentBean();
		bean3.sztype = 7;
		list.add(bean3);
		CommentBean bean4 = new CommentBean();
		bean4.sztype = 7;
		list.add(bean4);
		CommentBean bean5 = new CommentBean();
		bean5.sztype = 6;
		list.add(bean5);

		return list;
	}

	public static boolean isContainGBK(String str) {
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5a]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		System.out.println("共有 " + count + "个 ");
		return m.matches();
	}

	/**
	 * 解析html标签
	 * 
	 * @param str
	 * @return
	 */
	public static String parseLabel2Null(String str) {
		str = str.replace("^</?[a-z]+>$", "");
		return str;
	}

	/**
	 * 匹配汉字数字字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isRegularMatch_Ch_num_Let(String str) {
		String regular = "^[\u4e00-\u9fa5a-zA-Z0-9]+$";
		Pattern p = Pattern.compile(regular);
		Matcher m = p.matcher(str);

		return m.matches();
	}

	/**
	 * 匹配字母斜杠
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isRegularMatch_Let_sprit(String str) {
		String regular = "^</?[a-z]+>$";
		Pattern p = Pattern.compile(regular);
		Matcher m = p.matcher(str);

		return m.matches();
	}

	public static String indexof(String content) {
		content = "154;fdsjioj;54fdsklaf;fdsfsd";
		content = content.substring(content.indexOf(";") + 1);
		System.out.println(">>> content = " + content);

		return content;
	}

}
