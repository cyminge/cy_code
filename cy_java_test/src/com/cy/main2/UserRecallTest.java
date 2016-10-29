package com.cy.main2;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * 用户召回测试用例
 * @author JLB6088
 *
 */
public class UserRecallTest {
    
    public static String getJsonStr(int dataVersion) {
        
        UserRecall userRecall1 = new UserRecall();
        userRecall1.gameId = 1511;
        userRecall1.packageName = "com.happyelements.AndroidAnimal";
        List<String> list1 = new ArrayList<String>();
        list1.add("com.baidu.appsearch");
        userRecall1.otherChannelGame = list1;
        
        UserRecall userRecall2 = new UserRecall();
        userRecall2.gameId = 5389;
        userRecall2.packageName = "com.pokercity.bydrqp.jl";
        List<String> list2 = new ArrayList<String>();
        list2.add("com.qihoo.appstore");
        userRecall2.otherChannelGame = list2;
        
        UserRecall userRecall3 = new UserRecall();
        userRecall3.gameId = 5486;
        userRecall3.packageName = "com.gzwz.jufengzhanhun.jinli";
        List<String> list3 = new ArrayList<String>();
        list3.add("com.sogou.appmall");
        userRecall3.otherChannelGame = list3;
        
        UserRecall userRecall4 = new UserRecall();
        userRecall4.gameId = 7071;
        userRecall4.packageName = "com.youmeng.foodadventure.jinli";
        List<String> list4 = new ArrayList<String>();
        list4.add("com.wandoujia.phoenix2");
        userRecall4.otherChannelGame = list4;
        
        UserRecall userRecall5 = new UserRecall();
        userRecall5.gameId = 3940;
        userRecall5.packageName = "com.sg.atmpk.jinli";
        List<String> list5 = new ArrayList<String>();
        list5.add("com.hiapk.marketpho");
        userRecall5.otherChannelGame = list5;
        
        UserRecall userRecall6 = new UserRecall();
        userRecall6.gameId = 7041;
        userRecall6.packageName = "com.sg.hlw.jinli";
        List<String> list6 = new ArrayList<String>();
        list6.add("com.tencent.android.qqdownloader");
        userRecall6.otherChannelGame = list6;
        
        UserRecall userRecall7 = new UserRecall();
        userRecall7.gameId = 7150;
        userRecall7.packageName = "pailedi.klddz2016.jinli";
        List<String> list7 = new ArrayList<String>();
        list7.add("com.sogou.androidtool");
        userRecall7.otherChannelGame = list7;
        
        UserRecall userRecall8 = new UserRecall();
        userRecall8.gameId = 5840;
        userRecall8.packageName = "com.cybt.xcmtjms.jinli";
        List<String> list8 = new ArrayList<String>();
        list8.add("com.pp.assistant");
        userRecall8.otherChannelGame = list8;
        
        UserRecall userRecall9 = new UserRecall();
        userRecall9.gameId = 6892;
        userRecall9.packageName = "com.lemuellabs.fireworks2.jinli";
        List<String> list9 = new ArrayList<String>();
        list9.add("com.diguayouxi");
        userRecall9.otherChannelGame = list9;
        
        UserRecall userRecall10 = new UserRecall();
        userRecall10.gameId = 6866;
        userRecall10.packageName = "com.lxd.xcmzcldzw.jinli";
        List<String> list10 = new ArrayList<String>();
        list10.add("com.muzhiwan.market");
        userRecall10.otherChannelGame = list10;
        
        Gson gson = new Gson();
        List<UserRecall> packageList = new ArrayList<UserRecall>();
        packageList.add(userRecall1);
        packageList.add(userRecall2);
        packageList.add(userRecall3);
        packageList.add(userRecall4);
        packageList.add(userRecall5);
        packageList.add(userRecall6);
        packageList.add(userRecall7);
        packageList.add(userRecall8);
        packageList.add(userRecall9);
        packageList.add(userRecall10);
        UserRecallList userRecallList = new UserRecallList();
        userRecallList.userRecall = packageList;
        userRecallList.version = dataVersion;
        
        UserRecallALL userRecallALL = new UserRecallALL();
        userRecallALL.data = userRecallList;
//        {"success":true,"msg":"","data":{"version":1464169159,"userRecall":[{"gameId":"1511","otherChannelGame":["com.baidu.appsearch"],"packageName":"com.happyelements.AndroidAnimal"}]}}
        
        String retList = gson.toJson(userRecallALL);
        System.out.println("retJson = "+retList+"--dataVersion = "+dataVersion);
        return retList;
    }
    
    public static class UserRecallALL {
        UserRecallList data;
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
}
