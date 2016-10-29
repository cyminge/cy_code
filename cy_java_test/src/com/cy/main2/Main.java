package com.cy.main2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        // ChatItem item1 = new ChatItem();
        // item1.mMsgId = 11L;
        // ChatItem item2 = new ChatItem();
        // item2.mMsgId = 11L;
        //
        // Set<ChatItem> set = new HashSet<ChatItem>();
        //
        // set.add(item1);
        //
        // if(!set.contains(item1)) {
        // set.add(item1);
        // }
        //
        // System.out.println(set.size());
        //
        // String aa = new String("11");
        // String bb = new String("11");
        //
        // Set<String> set1 = new HashSet<String>();
        // set1.add(aa);
        // System.out.println(set1.contains(bb));

        // 娴嬭瘯 instanceof 鐨勬�ц兘

        // Timer timer = new Timer();
        // Man man = new Man();
        // Son son = new Son();
        // int count = 2000000000;
        //
        // timer.reset();
        // for(int i = 0; i < count; i++){
        // doSomeThing(true);
        // }
        // timer.printDrift();//62姣
        //
        // timer.reset();
        // for(int i = 0; i < count; i++){
        // doSomeThing(man instanceof IPerson);
        // }
        // timer.printDrift();//94姣
        //
        // timer.reset();
        // for(int i = 0; i < count; i++){
        // doSomeThing(man instanceof Man);
        // }
        // timer.printDrift();//78姣
        //
        // timer.reset();
        // for(int i = 0; i < count; i++){
        // doSomeThing(son instanceof Man);
        // }
        // timer.printDrift();//94姣
        //
        // timer.reset();
        // for(int i = 0; i < count; i++){
        // doSomeThing(man.getClass().isAssignableFrom(IPerson.class));
        // }
        // timer.printDrift();//4453姣
        //
        // timer.reset();
        // for(int i = 0; i < count; i++){
        // doSomeThing(man.getClass().equals(Man.class));
        // }
        // timer.printDrift();//375姣
        //
        // timer.reset();
        // for(int i = 0; i < count; i++){
        // doSomeThing(son.getClass().isInstance(man));
        // }
        // timer.printDrift();//4093姣

        // 娴嬭瘯 enum 鐨勬瀯閫犲嚱鏁扮殑鎵ц鏃堕棿

        // for(int i=0; i<100000; i++) {
        // EnumTest.init();
        // }
        //
        // Thread1 t1 = new Thread1();
        // t1.start();
        //
        // Thread2 t2 = new Thread2();
        // t2.start();
        //
        // Thread3 t3 = new Thread3();
        // t3.start();

        // 娴嬭瘯 寮曠敤浼犻�掑拰缃负null鐨勭粨鏋�
        // Timer timer1 = new Timer();
        // timer1.start = 11L;
        // Timer timer2 = timer1;
        // timer2 = null;
        // System.out.println(timer2);
        // System.out.println(timer1);
        //
        // ArrayList<Timer> tList1 = new ArrayList<Main.Timer>();
        // tList1.add(timer1);
        // ArrayList<Timer> tList2 = tList1;
        // tList2.get(0).start = 12L;
        // tList2 = null;
        // System.out.println(tList1.get(0).start);
        // System.out.println(tList2);

        // 娴嬭瘯鏋烽攣
        // testObjLock();

        // for(int i = 0 ; i < 6 ;i++) {
        // System.out.println(Integer.parseInt(skus[i][1]));
        // }

        // 娴嬭瘯HashMap
        // Map<Integer, Integer> hm = new Hashtable<Integer, Integer>();
        // hm.put(1, null);
        //
        // System.out.println(hm.get(3));

        // MSG msg = new MSG();
        // SonMSG sonMsg = new SonMSG();
        // // setMsg(msg);
        // // System.out.println(msg.str);
        //
        // System.out.println(msg instanceof MSG);
        // System.out.println(sonMsg instanceof MSG);
        // System.out.println(msg instanceof SonMSG);
        // System.out.println(sonMsg instanceof SonMSG);
        //
        // System.out.println((MSG) sonMsg);
        // System.out.println((SonMSG) msg);

        // int i1 = 50;
        // int i2 = 60;
        // List<Integer> mPushMsgCache = new ArrayList<Integer>();
        // mPushMsgCache.add(i1);
        // mPushMsgCache.add(i2);
        //
        // System.out.println(mPushMsgCache.contains(i1));
        // System.out.println(mPushMsgCache.remove((Object)i1));
        // System.out.println(mPushMsgCache.contains(i1));

        // long j1 = 50;
        // long j2 = 60;
        // List<Long> mPushMsgCacheLong = new ArrayList<Long>();
        // mPushMsgCacheLong.add(50L);
        // mPushMsgCacheLong.add(60L);
        //
        // System.out.println(mPushMsgCacheLong.contains(j1));
        // System.out.println(mPushMsgCacheLong.remove(j1));
        // System.out.println(mPushMsgCacheLong.contains(j1));

        // ArrayList<Integer> mUserUinBeanList = new ArrayList<Integer>();
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

        // count(9000000);
        // Wed Feb 04 17:47:58 CST 2015
        // Wed Feb 04 17:48:06 CST 2015

        // count2(9000000);
        // Wed Feb 04 17:48:24 CST 2015
        // 3鍒嗗閽熻繕娌＄粨鏉燂紝灏卞仠姝㈢▼搴忎簡銆�

        // count2(3000000);
        // Wed Feb 04 17:54:16 CST 2015
        // Wed Feb 04 17:54:17 CST 2015

        // int[] main = new int[3];
        // System.out.println(main.length);
        //
        // ChannelView[] mChannelViews = new ChannelView[4];
        // System.out.println(mChannelViews.length);

        // char a = '我';
        // int i = (int)a;
        // System.out.println(" -->"+i);
        //
        // char ch = '我';
        // int n = (int)ch - (int)'0';
        // System.out.println(" -->"+n);

        // Car car = new Car(22000,"silver");
        // WeakReference<Car> weakCar = new WeakReference<Car>(car);
        //
        // int i=0;
        //
        // while(true){
        // if(weakCar.get()!=null){
        // i++;
        // System.out.println("Object is alive for "+i+" loops - "+weakCar);
        // }else{
        // System.out.println("Object has been collected.");
        // break;
        // }
        // }
        // System.out.println("car = "+car);

        // long timeStart = System.currentTimeMillis();
        // for(int i=0; i< 1000; i++) {
        // new Thread() {
        // public void run() {
        // testSync11();
        // };
        // }.start();
        // }
        // long timeEnd = System.currentTimeMillis();
        // System.out.println("timeEnd-timeStart = "+(timeEnd-timeStart));
        // for(int i=0; i< 1000; i++) {
        // new Thread() {
        // public void run() {
        // testSync22();
        // };
        // }.start();
        // }
        // long timeEnd1 = System.currentTimeMillis();
        // System.out.println("timeEnd1-timeEnd = "+(timeEnd1-timeEnd));

        // TimeoutChecker checker = new TimeoutChecker() {
        // @Override
        // public String getData() {
        // return "initial_data";
        // }
        //
        // @Override
        // public void onSuccess(String data) {
        // System.out.println("onSuccess:" + data);
        //
        // }
        //
        // @Override
        // public void onTimeout() {
        // System.out.println("onTimeout");
        // }
        // };
        // checker.start(3000);

        // int start = 7;
        // int end = 6;
        //
        // int result = (start-end)/2;
        // System.out.println("result-->" + result);
        // float result1 = result;
        // System.out.println("result1-->" + result1);
        // float result2 = ((float)(start-end))/2;
        // System.out.println("result2-->" + result2);

        // String ss = "puuid=69D97CDCF8844DBA83BF57FD2C3FDB28&
        // apk_info=com.muratgumus.Enton.wallpaper%3A21%3A1.0%3A7c11858dd7c297048436a4f243f36749%7Ccom.muratgumus.Enton.home%3A21%3A1.0%3Ab91879febb4c339856e6a3fefe39d1e9%7Ccom.muratgumus.Enton.appicon%3A21%3A1.0%3A283767fe53bde193869ceda375acfbc5%7Ccn.com.sec.Space.common.appicon%3A341%3Av4.7_RC10%3Ac445c33806efdc21cac54239ac36a54e%7Ccom.samsung.festival.chinadefault.theme51207914.wallpaper%3A11%3A1.0%3A313d48459ec09afc172fb0eb8f664b8e%7Ctheme4.tenbyten%3A21%3A1.0%3Afd3f87bbf882180e5f7caaf0946b0b9f%7Ccom.samsung.festival.chinadefault.theme13840795.wallpaper%3A11%3A1.0%3Aebbdf351362719f2e231b8c634cc4bf8%7Ccn.com.sec.Paperfun.common.wallpaper%3A13%3A1.0%3Ad3624332963f13eb8d127cd02413e1c0%7Ctheme4.tenbyten.home%3A21%3A1.0%3A4519e6778fbf71e74852e50689862497%7Ccom.csair.mbp%3A20160323%3A2.7.6%3A0fb3d45d6ef94b3f526493f6489c029e%7Ccom.xiaomi.gamecenter.sdk.service%3A44033%3A4.4.33%3A5c2f099c99a9717548f64c9fec6b20fb%7Ccom.qq.ac.android%3A75%3A5.3.9%3A15eee10a68c6ad075471f4d2c11932e4%7Ccom.lietou.mishu%3A336%3A2.23.1%3A3f042d65fa33d4d8eb1da16752b94133%7Ccom.wandoujia.phoenix2.usbproxy%3A6216%3A3.52.1%3A7c99d4da57a3ce25b64d056d427a69b9%7Ccn.com.sec.Space.common.wallpaper%3A341%3Av4.7_RC10%3Af383715c1bdeae685524554504d6bbee%7Ccom.ubisoft.adventure.valiant_hearts%3A54%3A1.0.0%3Aad40280eaef3c976d6be1ee21dec48d9%7Ccom.diguayouxi%3A760%3A7.6_fix%3A1ac73ad340b4a83e0df45fc755a46280%7Ccom.sec.android.app.voicenote%3A2018079000%3A20.1.80-79%3Acff9b522f80aeb04132abf75aadc7eb7%7Ccom.MobileTicket%3A101%3A2.3%3Afa9320c4fb5519533f8862420521d6b6%7Ccom.duolingo%3A298%3A3.17.3%3Aa5616ff2a204c8495d1a8e607cbe41bc%7Ccom.joybits.doodlegod_mysdk_blitz.NonCMCC%3A1%3A1.0.1%3A8ae5d14c50919c00e962a6512ed9e3e2%7Ccom.microsoft.office.excel%3A2000431018%3A16.0.6430.1018%3A033b8acea07535dc153a3334030a25f6%7Ccom.squareenixmontreal.lcgo%3A53878%3A2.0.53878%3Aa315250fb853121d7272e507e74c037c%7Ccom.muratgumus.Enton%3A21%3A1.0%3A3474a56e45d5dc4e1510bf7938bb8120%7Ccom.singingcicada.poolworld.mi%3A1%3A1.01.01%3A1e99cbf599df8cc8bf2beb952edb52f9%7Ccn.com.sec.Paperfun.common.appicon%3A13%3A1.0%3A3dbf55d914baabce4a4f7da4f5932469%7Ccom.douguo.recipe%3A271%3A6.1.1.2%3A3eeaee5ffc9eb78b5d2f517cfa5118ff%7Ccom.supercell.clashroyale.am%3A76%3A1.2.6%3Ac18c5f43bace433f080f07f1b8c18be6%7CCheilPengtai.C110Lemon%3A1%3A1.0%3Acfa0f660f29e62c363044adae80a17e7%7Ccom.tencent.research.drop%3A438%3A3.2.0.438%3A119b38699af7ee193fdbcb50101ad938%7Ccom.zhaopin.social%3A610%3A6.1.0%3Aa7d7153f2ce9c55bae22413c96557b3e%7Ccom.samsung.upsmtheme.home%3A33%3A2.00.22%3A701ec15df94f1bf955e770b4d5bc5287%7Ccomimageillust.facebook.httpswww.withcoff.wallpaper%3A1%3Awithcoffee_0%3A53b9d1b476503f36700439e58bf6e132%7Ccom.tencent.qqpim%3A1240%3A6.3.0%3A764b8b4bcd7d3761216247bc93a69f97%7Ccom.samsung.android.app.memo%3A1600000132%3A16.0.00-132%3A5af5e51d80812e5592698eea49bd6c2b%7Ccom.samsung.multidevicecloud%3A10127001%3A1.1.27%3A7997beaa7cd0df77e5a6931407640093%7Ccom.vancl.activity%3A457%3A4.4.4%3A74bdb40d7ec009f17eb43b64ba04188c%7Ccom.samsung.festival.chinadefault.theme22841066.wallpaper%3A11%3A1.0%3A038ef79d41dd4feef74f00034e6e7339%7Ccom.cis.cathospital.wdj%3A6%3A1.0.7.2%3Ad908b3adc7143888dc18f92ff74e642d%7Ccom.newdadabus%3A160425%3A2.3.0%3Ab8186c45814020216d3d9cd62c703c1c%7Ccomimageillust.facebook.httpswww.withcoff.appicon%3A1%3Awithcoffee_0%3Aed71fecbc45cdcd1f412993d738921f7%7CCheilPengtai.C988MusicPanda%3A11%3A1%3A249962716d0690f6a23579fb6ae63e80%7Ccom.tencent.tmgp.seal%3A110%3A1.0.19%3A2fcbd2f81f8299f7a785745a4030856f%7Ccom.samsung.festival.chinadefault.theme4944957.wallpaper%3A11%3A1.0%3Af20842228dd243ddbf96cdb4436496eb%7CCheilPengtai.C988MusicPanda.wallpaper%3A11%3A1%3Ae396d15b414a744a2c17cc0ce52b456d%7Ccom.tencent.pocket%3A391%3A2.4.0%3Aa18aa16aeaa7fe7ae0fe9e1fc2791927%7Ccom.netease.l10.am%3A7%3A1.0.7%3A1337e3232dbe63a86d6a0687f627c438%7Ccom.pp.assistant%3A1433%3A3.7.1%3A5193f189af6cbd991203af554c161bb7%7Ccom.samsung.festival.chinadefault.theme1314015.wallpaper%3A11%3A1.0%3Afbe98b27801b6025bc95e63550216672%7Ccom.estrongs.android.pop%3A507%3A4.0.4.5%3A644dc28122fcc8c3a1eef0adbc37a7b3%7Ccom.samsung.festival.chinadefault.theme4018554.wallpaper%3A11%3A1.0%3Acd5758038aecd502443a70e0ee3e4575%7Ccom.cubic.autohome%3A545%3A5.4.5%3A709964efc43244711a9f803b379b955d%7Ccom.samsung.festival.chinadefault.theme12747841.wallpaper%3A11%3A1.0%3Ad02bbbc478d767b63e9cc3b007bfb716%7Ccom.mx.browser%3A2947%3A4.5.7.1000%3A80655cb22b3ed79acea243905ad2f038%7Ccom.microsoft.office.word%3A2000431018%3A16.0.6430.1018%3Ab5b0fc0ca2fa96f6331f0ae35d945c7e%7Ccom.samsung.festival.chinadefault.theme57841614.wallpaper%3A11%3A1.0%3A650096b83d8d56ee1dba03777961b580%7Ccom.microsoft.office.powerpoint%3A2000431018%3A16.0.6430.1018%3A1d4c45a6a1b48710279a6f34fff12bb1%7Ccom.android.chrome%3A256409500%3A48.0.2564.95%3A70db36495d7f7a18fea5c6845586e244%7Ccom.myzaker.ZAKER_Phone%3A228%3A6.5.2%3A04cc11899ff980158292c6513dd35609%7Ccom.google.android.gsf%3A16%3A4.1.2-509230%3Aa777241b02c1ccdf2eeeba2979cada7c%7Ccom.sonyericsson.extras.liveware%3A50727672%3A5.7.27.672%3Ab0762f9e5ce7c73d7b44796adc63eabd%7Ccom.elong.hotel.ui%3A991%3A9.9.1%3A636be8694d552016c656f6367ccb3c9d%7Ccom.samsung.festival.chinadefault.theme6300983.wallpaper%3A11%3A1.0%3Ab791e9a764d92a37fdea22c0cf0ecfc0%7Ctheme4.tenbyten.wallpaper%3A21%3A1.0%3Aa9ffdb2776086ba85bead9a14492719b%7Ccn.com.sec.Paperfun.common.home%3A13%3A1.0%3A140aa460ea7df7fdd8155d459f768a66%7Ccom.gotokeep.keep%3A2925%3A3.2.1%3Ad62fbd5d9536ac3eb3df63b6076c58d4%7Ccom.samsung.android.app.sreminder%3A330012%3A3.3.0.12%3A10b195aedf14a66b8fa70fdd3fa4461c%7Ctheme4.tenbyten.appicon%3A21%3A1.0%3Ab55bf799afe2db33a3d1d61d5fd25487%7Ccom.samsung.festival.chinadefault.theme7260690.wallpaper%3A11%3A1.0%3A8915c2ccf6827ceac76ea0f67503cec3%7Ccom.wandoujia.phoenix2%3A12005%3A5.13.1%3Af11d3dd1de7c70d6b985072619ce9395%7Ccom.ubercab%3A31173%3A3.92.1%3Aa91b58a486d53273240444d29836aecd%7Ccom.samsung.festival.chinadefault.theme40032995.wallpaper%3A11%3A1.0%3A9435d7f9f2cd8321cd763886ae9794c0%7Ccn.ninegame.gamemanager%3A784%3A4.4.10.4%3Abe6e7fd984544aa615d2d26f763fd916%7Ccom.samsung.festival.chinadefault.theme2581871.wallpaper%3A11%3A1.0%3A7268187f0a5987cafc20f32ba88d053a%7Ccom.samsung.festival.chinadefault.theme29510374.wallpaper%3A11%3A1.0%3Ac0d1381b283eac86e88f08e2f05e27a0%7Ccom.facebook.katana%3A23966367%3A66.0.0.33.73%3A4b36ce174f429d52ff0efd91af0deed4%7Ccom.zdworks.android.zdclock%3A561%3A4.9.561%3Abd9a6c27ed9d1d3b3c9f96190dd74d63%7Ccom.soufun.app%3A99%3A8.1.0%3A7cb659180df9364bea89ce447bd66478%7Ccom.nyamyam.tengamipackage%3A12%3A1.12.0%3A74228bb3ba34cad3d3110a3ad3e1bf5a%7Ccom.sonymobile.smartconnect.hostapp.ellis%3A10500488%3A1.5.0.488%3Aa4c667daea21b4fb21f93c9b15ff6a7c%7Cpl.idreams.skyforcehd%3A28%3A1.38%3Ab33b9261df751d9ede93a68a64569db8%7Ccom.sec.android.app.popupcalculator%3A505104000%3A5.0.51%3A8c170b8937ebf5772d1f3d3f3b5c6501%7CCheilPengtai.C110Lemon.wallpaper%3A1%3A1.0%3Ae06c85ed0ce8e651ea6989e4cadbda57%7Ccom.nuomi%3A161%3A6.3.0%3A89c7f7695a3bc02594efeaac064ec048%7Ccom.samsung.android.voc%3A160101000%3A1.6.01%3Ab167d3f6062e7738f069e67e19950f64%7Ccom.sec.android.app.shealth%3A4620019%3A4.6.2.0019%3Afad6b0cacc4048d1daa4ec0d5b3fdd78%7CCheilPengtai.C110Lemon.home%3A1%3A1.0%3A02eda87f2e979fc797f1ebb46e3ca561%7Ccom.evernote%3A1076098%3A7.6%3A880f478f68d1ce8fe056a90c61b72636%7Ccom.netease.my.am%3A10740%3A1.74.0%3Aeb668ce1b9802e146cb982196c5346ab%7Ccom.picooc%3A42%3A2.7.0%3Aa6bb009bb3a435c5d29e3af8d4eb8c8b%7Ccomimageillust.facebook.httpswww.withcoff.home%3A1%3Awithcoffee_0%3Aca28d029a63910cf3b10bed0007af526%7Ccom.fujifilm_dsc.app.remoteshooter%3A6%3A2.0.1%28Build%3A2.0.1.1%29%3Ac5b6d85db3798278eafb8313aac98c65%7Ccom.samsung.festival.chinadefault.theme31713777.wallpaper%3A11%3A1.0%3A4f015fc95ec30165099585d9d74366e7%7Ccom.sec.android.easyMover%3A320200023%3A3.2.02-23%3A88c0d5577145b7338256d0bae2bb560f%7Cctrip.android.view%3A129%3A6.14.4%3A6199972922f8c4ebb7c3c3bc163e474d%7Ccom.noodlecake.altosadventure%3A36%3A1.2%3Accb4a724f4a0014b476494750069a854%7Cflipboard.cn%3A2825%3A3.3.19.1%3Ab44f8c3e67faa6bbad0aa539e12f05cb%7CCheilPengtai.C110Lemon.appicon%3A1%3A1.0%3A2b53ca00e5b65adb0eea8e6f92166e8f%7CCheilPengtai.C988MusicPanda.home%3A11%3A1%3Ac8566ec48601e3bb455b55ef7b101a3f%7Ccom.gionee.gsp%3A30101003%3A3.1.1.d%3A209d6e2912fbf2fba44eb6ce6ba3bb6e%7Ccn.com.sec.Space.common%3A341%3Av4.7_RC10%3Acfdd633a1dab8467aead67f426a28815%7Ccomimageillust.facebook.httpswww.withcoff%3A1%3Awithcoffee_0%3A03df7128ec70f396b8872771f8be05b1%7Ccom.samsung.upsmtheme.appicon%3A33%3A2.00.22%3A8f43661a424808c8d5fdf2256476eb62%7Ccn.com.sec.Space.common.home%3A341%3Av4.7_RC10%3Ae7d8f29e7fca505b4eabf8c88873c952%7Ccom.ironhidegames.android.kingdomrushorigins%3A1416403090%3A1.0.0%3A55d2847e1785be6cb72e565be7e41b35%7Cgn.com.android.gamehall%3A1020002502%3A1.6.4.b%3Ab5560be9779d7eb1d82bd4a9c82e9611%7CCheilPengtai.C988MusicPanda.appicon%3A11%3A1%3Aa661a0d4a43ad61c02b651b7a7048dd3%7Ccom.samsung.festival.chinadefault.theme2681785.wallpaper%3A11%3A1.0%3Ad0d436536f89d85944ab916ac7f47e2b%7Ccom.samsung.festival.chinadefault.theme34382874.wallpaper%3A11%3A1.0%3Ad83273363846b58e718e243da0ed922c%7Cgn.com.android.gamehall%3A1020002502%3A1.6.4.b%3Ab5560be9779d7eb1d82bd4a9c82e9611%7Ccom.gionee.gsp%3A30101003%3A3.1.1.d%3A209d6e2912fbf2fba44eb6ce6ba3bb6e%7C6.0.1%7C1080*1920&clientId=09450bf79d00791645dd92444cddb3c3&phone_ram=2817&serverId=9435B64002605F4A6E78DE66754E96FBED79B0BDC0F5658C9FE0E4601CD8E811B5FD7B05CB40468B25BFFAE28E101A3D&client_pkg=gn.com.android.gamehall&imei=73B14EC7C02B0449B0202FA5006E70CA&brand=samsung&sp=SM-A9000_1.6.4.b_null_Android6.0.1_1080*1920_T99999_wifi_73B14EC7C02B0449B0202FA5006E70CA&version=1.6.4.b&uname=18588271066";

//        for (int i = 0; i < 10000; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Random ra =new Random();
//                    try {
//                        Thread.sleep(ra.nextInt(1));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    
//                    if(ra.nextInt(2) == 0) {
//                        add();
//                    } else {
//                        update();
//                    }
//                    
//                    System.out.println("--"+list.size());
//                }
//            }).start();
//        }
        
        
//        LockUtils.getInstance().lock();
//        LockUtils.getInstance().release();
//        LockUtils.getInstance().lock();
//        LockUtils.getInstance().release();
//        LockUtils.getInstance().lock();
//        LockUtils.getInstance().release();
//        LockUtils.getInstance().lock();
//        LockUtils.getInstance().release();
//        Thread11 thread11 = new Thread11();
//        Thread22 thread22 = new Thread22();
////        Thread33 thread33 = new Thread33();
//        thread11.start();
//        thread22.start();
////        thread33.start();
//        
//        thread11.release();
//        thread22.release();
////        thread33.release();
//        thread11.lock();
        
//        UserRecallTest.getJsonStr(10);
        
        User user = null;
        
        System.out.println("wo" == user.name);
    }
    
    private static class User {
        String name;
    }
    
    private static class Thread11 extends Thread {
        @Override
        public void run() {
            try {
                LockUtils.getInstance().lock11();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        public void release() {
            LockUtils.getInstance().unlock11();
        }
        
        public void lock() {
            try {
                LockUtils.getInstance().lock11();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class Thread22 extends Thread {
        @Override
        public void run() {
            try {
                LockUtils.getInstance().lock11();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        public void release() {
            LockUtils.getInstance().unlock11();
        }
        
        public void lock() {
            try {
                LockUtils.getInstance().lock11();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class Thread33 extends Thread {
        @Override
        public void run() {
            try {
                LockUtils.getInstance().lock11();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        public void release() {
            LockUtils.getInstance().unlock11();
        }
        
        public void lock() {
            try {
                LockUtils.getInstance().lock11();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static volatile int counts = 0;

    private static void update() {
        synchronized (list) {
            list.clear();
            add();
        }
    }

    private static void add() {
        synchronized (list) {
            list.add("cyminge");
            list.add("zf");
        }
    }

    private static ArrayList<String> list = new ArrayList<String>();
    private static boolean isInit = false;

    public static void testSync11() {
        synchronized (list) {
            // if(isInit) {
            // return;
            // }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isInit = true;
        }
    }

    public static void testSync22() {
        // if(isInit) {
        // return;
        // }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // synchronized (list) {
        // if(isInit) {
        // return;
        // }
        // try {
        // Thread.sleep(100);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // isInit = true;
        // }
    }

    public static final int aaa = 0xFFFD;

    public class ChannelView {
        public String str = "";
    }

    public static void count(int count) {
        System.out.println(System.currentTimeMillis());
        List<Integer> ilist = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            ilist.add(i);
        }
        System.out.println(System.currentTimeMillis());
    }

    public static void count2(int count) {
        System.out.println(System.currentTimeMillis());
        Map<Integer, Integer> m = new HashMap<Integer, Integer>();
        for (int i = 0; i < count; i++) {
            m.put(i, i);
        }
        System.out.println(System.currentTimeMillis());
    }

    public static void setMsg(final MSG msg) {
        msg.str = "haha";
    }

    static class MSG {
        String str = "AA";
    }

    static class SonMSG extends MSG {
        String str = "BB";
        String sonStr = "bb";
    }

    private static String[][] skus = { { "id_1", "0" }, { "id_2", "1" }, { "id_3", "2" }, { "id_4", "3" },
            { "id_5", "5" }, { "id_6", "6" } };

    private static Object objLock = new Object();

    private static void testObjLock() {
        synchronized (objLock) {
            System.out.println("aaa");
            printBBB();
        }
    }

    private static void printBBB() {
        synchronized (objLock) {
            System.out.println("bbb");
        }
    }

    static class Thread1 extends Thread {
        @Override
        public void run() {
            System.out.println("Thread1");
            for (int i = 0; i < 10000000; i++) {
                EnumTest.init();
            }
        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {
            System.out.println("Thread2");
            for (int i = 0; i < 10000000; i++) {
                EnumTest.init();
            }
        }
    }

    static class Thread3 extends Thread {
        @Override
        public void run() {
            System.out.println("Thread3");
            for (int i = 0; i < 10000000; i++) {
                EnumTest.init();
            }
        }
    }

    public enum EnumTest {
        INSTANCE;

        private static boolean isDone = false;

        private EnumTest() {
            System.out.println("---");
        }

        public static void init() {
            // System.out.println(INSTANCE.getClass().isEnum());
            if (isDone) {
                return;
            }
            isDone = true;

            INSTANCE.startInit();
        }

        private void startInit() {
            System.out.println("===");
        }
    }

    private static void doSomeThing(boolean bool) {
    }

    public static class Timer {
        private long start = 0l;

        public void reset() {
            start = System.currentTimeMillis();
        }

        public void printDrift() {
            System.out.println("---" + (System.currentTimeMillis() - start));
        }
    }

    public static class Man implements IPerson {

    }

    public static class Son extends Man {

    }

    public interface IPerson {

    }

}
