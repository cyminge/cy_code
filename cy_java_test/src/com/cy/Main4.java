package com.cy;

public class Main4 {

    public static void main(String[] args) {
        changeJson();
    }

    static void changeJson() {

//        int i = 11;
//        
//        System.out.println(i%2==0?\\"偶数\\":\\"奇数\\");
//        System.out.println(i%2==1?\\"奇数\\":\\"偶数\\");

//        String testJsonStr = \\"{\\\"sign\\\":\\\"GioneeGameHall\\\",\\\"msg\\\":\\\"\\\",\\\"success\\\":true,\\\"data\\\":{\\\"slideItems\\\":[{\\\"viewType\\\":\\\"Link\\\",\\\"title\\\":\\\"\u53cc\u5341\u4e00\u72c2\u6b22\u4e13\u573a\\\",\\\"content\\\":\\\"\u53cc\u5341\u4e00\u72c2\u6b22\u4e13\u573a\\\",\\\"imageUrl\\\":\\\"http://assets.gionee.com/attachs/game/ad/201611/582451e4ccb3b.jpg\\\",\\\"param\\\":{\\\"url\\\":\\\"http://game.gionee.com/client/festival_doubleeleven/index?intersrc=ad11\\\",\\\"contentId\\\":\\\"\\\",\\\"gameId\\\":\\\"\\\",\\\"title\\\":\\\"\u53cc\u5341\u4e00\u72c2\u6b22\u4e13\u573a\\\"},\\\"source\\\":\\\"\\\",\\\"ad_id\\\":\\\"2792\\\"},{\\\"viewType\\\":\\\"GameDetailView\\\",\\\"title\\\":\\\"\u65f6\u7a7a\u67aa\u6218\\\",\\\"content\\\":\\\"\u65f6\u7a7a\u67aa\u6218\\\",\\\"imageUrl\\\":\\\"http://assets.gionee.com/attachs/game/ad/201611/582409f0e4016.jpg\\\",\\\"param\\\":{\\\"url\\\":\\\"\\\",\\\"contentId\\\":\\\"8353\\\",\\\"gameId\\\":\\\"8353\\\",\\\"title\\\":\\\"\u65f6\u7a7a\u67aa\u6218\\\",\\\"package\\\":\\\"com.yinhan.game.skqz.am\\\"},\\\"source\\\":\\\"\\\",\\\"ad_id\\\":\\\"2786\\\",\\\"gameId\\\":\\\"8353\\\",\\\"gameid\\\":8353,\\\"name\\\":\\\"\u65f6\u7a7a\u67aa\u6218\uff08\u9001M6Plus\uff09\\\",\\\"resume\\\":\\\"\u706b\u7206\u5f39\u5e55\uff0c\u6fc0\u723d\u67aa\u6218\uff0c\u8001\u53f8\u673a\u4e0a\u8f66\\\",\\\"package\\\":\\\"com.yinhan.game.skqz.am\\\",\\\"img\\\":\\\"http://s.dev.gionee.com/Attachments/dev/icons/2016/11/10/5823ffb8e0fc4.png.144.png\\\",\\\"link\\\":\\\"http://gamedl.gionee.com/Attachments/dev/apks/2016/10/26/147746503222_signed.apk\\\",\\\"size\\\":\\\"165.71M\\\",\\\"category\\\":\\\"\u52a8\u4f5c\u5192\u9669\\\",\\\"hot\\\":0,\\\"commonGift\\\":1,\\\"vipGift\\\":1,\\\"score\\\":5,\\\"freedl\\\":\\\"\\\",\\\"attach\\\":1,\\\"reward\\\":\\\"\\\",\\\"downloadCount\\\":\\\"10.6\u4e07\u4e0b\u8f7d\\\"},{\\\"viewType\\\":\\\"TopicDetailView\\\",\\\"title\\\":\\\"\u53cc\u5341\u4e00\u9650\u65f65\u6298\\\",\\\"content\\\":\\\"\u53cc\u5341\u4e00\u9650\u65f65\u6298\\\",\\\"imageUrl\\\":\\\"http://assets.gionee.com/attachs/game/ad/201611/58244e6200b16.jpg\\\",\\\"param\\\":{\\\"url\\\":\\\"\\\",\\\"contentId\\\":\\\"569\\\",\\\"gameId\\\":\\\"\\\",\\\"title\\\":\\\"\u53cc\u5341\u4e00\u9650\u65f65\u6298\\\"},\\\"source\\\":\\\"\\\",\\\"ad_id\\\":\\\"2790\\\"},{\\\"viewType\\\":\\\"ActivityDetailView\\\",\\\"title\\\":\\\"\u5929\u5929\u6597\u5730\u4e3b\\\",\\\"content\\\":\\\"\u5929\u5929\u6597\u5730\u4e3b\\\",\\\"imageUrl\\\":\\\"http://assets.gionee.com/attachs/game/ad/201611/58244fcc8ddb2.jpg\\\",\\\"param\\\":{\\\"url\\\":\\\"\\\",\\\"contentId\\\":\\\"1623\\\",\\\"gameId\\\":\\\"6311\\\",\\\"title\\\":\\\"\u5929\u5929\u6597\u5730\u4e3b\\\",\\\"package\\\":\\\"com.zengame.hlddztzs.p5gjinli\\\"},\\\"source\\\":\\\"\\\",\\\"ad_id\\\":\\\"2791\\\",\\\"gameId\\\":\\\"6311\\\",\\\"gameid\\\":6311,\\\"name\\\":\\\"\u5929\u5929\u6597\u5730\u4e3b\uff08\u771f\u4eba\u7248\uff09\\\",\\\"resume\\\":\\\"\u5343\u4e07\u7cbe\u82f1\u6597\u5730\u4e3b\uff0c\u5929\u5929\u5927\u5956\u62ff\u4e0d\u505c\\\",\\\"package\\\":\\\"com.zengame.hlddztzs.p5gjinli\\\",\\\"img\\\":\\\"http://s.dev.gionee.com/Attachments/dev/icons/2016/11/08/58219b1d05c40.png.144.png\\\",\\\"link\\\":\\\"http://gamedl.gionee.com/Attachments/dev/apks/2016/10/29/1477712033987.apk\\\",\\\"size\\\":\\\"20.33M\\\",\\\"category\\\":\\\"\u68cb\u724c\u5929\u5730\\\",\\\"hot\\\":0,\\\"commonGift\\\":1,\\\"vipGift\\\":1,\\\"score\\\":4,\\\"freedl\\\":\\\"\\\",\\\"attach\\\":1,\\\"reward\\\":\\\"\\\",\\\"downloadCount\\\":\\\"307.9\u4e07\u4e0b\u8f7d\\\"}]}}\\";
        // 大作预约
//         String subscribe =
//         \\"{\\\"viewType\\\":\\\"GameSubscribeListView\\\",\\\"listItemType\\\":\\\"SubscribeGames\\\",\\\"title\\\":\\\"\u9884\u7ea6\u9001\u91d1\u7acbS8\u624b\u673a\\\",\\\"total\\\":\\\"10\\\",\\\"param\\\":{\\\"title\\\":\\\"\u5927\u4f5c\u9884\u7ea6\\\"},\\\"subscribeItems\\\":[{\\\"id\\\":\\\"36\\\",\\\"name\\\":\\\"\u795e\u66f2\u4e4b\u7b26\u6587\u82f1\u96c4\\\",\\\"resume\\\":\\\"\u5973\u795e\u5218\u4ea6\u83f2\u4ee3\u8a00SRPG\u9b54\u5e7b\u624b\u6e38\u5927\u4f5c\\\",\\\"package\\\":\\\"com.changic.jl_rh.am\\\",\\\"img\\\":\\\"http://assets.gionee.com/attachs/game/reservedactivity/201607/577b029b3b133.png\\\",\\\"images\\\":[\\\"http://assets.gionee.com/attachs/game/reservedactivity/201607/57765266de1f2.png_480x800.png\\\",\\\"http://assets.gionee.com/attachs/game/reservedactivity/201607/5776526e40ec4.png_480x800.png\\\",\\\"http://assets.gionee.com/attachs/game/reservedactivity/201607/57765274ccd52.png_480x800.png\\\",\\\"http://assets.gionee.com/attachs/game/reservedactivity/201607/5776527c109fa.png_480x800.png\\\",\\\"http://assets.gionee.com/attachs/game/reservedactivity/201607/5776528286cfb.png_480x800.png\\\"],\\\"time\\\":\\\"1467856800\\\",\\\"subscribeCount\\\":\\\"432\\\",\\\"subscribe\\\":false}]}\\";
    
//    	String cache = \\"fdjfsdiof78\\"+UUID_DIVIDER+\\"abcdefg\\";
//    	System.out.println(cache);
//    	System.out.println(\\"\\".equals(null));
        
//        String message = "{\"success\":true,\"msg\":\"\",\"sign\":\"GioneeGameHall\",\"data\":{\"ordertag\":\"gamehallorder\",\"action\":\"gamehall.noti.msg.custom\",\"version\":\"1.5.9.a\",\"param\":{\"id\":\"17391\",\"type\":\"custom\",\"extraType\":\"subscribeDownload\",\"viewType\":\"GameDetailView\",\"title\":\"\u848b\u8363\u4e5010\",\"content\":\"\u848b\u8363\u4e5010\u9884\u7ea6PUSH\u4e0b\u8f7d\",\"param\":{\"url\":\"\",\"contentId\":\"465\",\"gameId\":\"465\",\"title\":\"\u848b\u8363\u4e5010\",\"package\":\"com.jiangrongle10.jinli\",\"hashTime\":\"1\"},\"timeStamp\":\"1487988303\",\"startTime\":\"1487988303\",\"source\":\"GameDetailView\",\"uname\":\"13926568240\"}}}";
//        String message = "{\"success\":true,\"msg\":\"\",\"sign\":\"GioneeGameHall\",\"data\":{\"ordertag\":\"gamehallorder\",\"action\":\"gamehall.noti.msg.custom\",\"version\":\"1.5.9.a\",\"param\":{\"id\":\"257\",\"type\":\"custom\",\"extraType\":\"\",\"viewType\":\"link\",\"title\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u94fe\u63a5\",\"content\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u94fe\u63a5\uff0c\u4e00\u822cimei\u53f7\u4e0a\u4f20\u6587\u4ef6\uff0c\u4e00\u822cimei\u53f7\u624b\u52a8\u586b\u5199\",\"param\":{\"url\":\"http://game.gionee.com/client/reservedgames/detail?id=95\",\"contentId\":\"\",\"gameId\":\"\",\"title\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u94fe\u63a5\",\"hashTime\":\"1\"},\"timeStamp\":1487988060,\"startTime\":1487988060,\"source\":\"link\"}}}";
//        String message = "{\"success\":true,\"msg\":\"\",\"sign\":\"GioneeGameHall\",\"data\":{\"ordertag\":\"gamehallorder\",\"action\":\"gamehall.noti.msg.custom\",\"version\":\"1.5.9.a\",\"param\":{\"id\":\"256\",\"type\":\"custom\",\"extraType\":\"\",\"viewType\":\"GiftDetailView\",\"title\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u793c\u5305\",\"content\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u793c\u5305\",\"param\":{\"url\":\"\",\"contentId\":\"344\",\"gameId\":\"355\",\"title\":\"\u98de\u673a\u5927\u62183\u65b0\u624b\u793c\u5305\",\"hashTime\":\"1\"},\"timeStamp\":1487987880,\"startTime\":1487987880,\"source\":\"GiftDetailView\"}}}";
//        String message = "{\"success\":true,\"msg\":\"\",\"sign\":\"GioneeGameHall\",\"data\":{\"ordertag\":\"gamehallorder\",\"action\":\"gamehall.noti.msg.custom\",\"version\":\"1.5.9.a\",\"param\":{\"id\":\"255\",\"type\":\"custom\",\"extraType\":\"\",\"viewType\":\"ActivityDetailView\",\"title\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u6d3b\u52a8\",\"content\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u6d3b\u52a8\",\"param\":{\"url\":\"\",\"contentId\":\"139\",\"gameId\":\"66\",\"title\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u6d3b\u52a8\",\"hashTime\":\"1\"},\"timeStamp\":1487987820,\"startTime\":1487987826,\"source\":\"ActivityDetailView\"}}}";
//        String message = "{\"success\":true,\"msg\":\"\",\"sign\":\"GioneeGameHall\",\"data\":{\"ordertag\":\"gamehallorder\",\"action\":\"gamehall.noti.msg.custom\",\"version\":\"1.5.9.a\",\"param\":{\"id\":\"254\",\"type\":\"custom\",\"extraType\":\"\",\"viewType\":\"GameDetailView\",\"title\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u6e38\u620f\",\"content\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u6e38\u620f\",\"param\":{\"url\":\"\",\"contentId\":\"456\",\"gameId\":\"456\",\"title\":\"\u5415\u8c46\u8c46\u6307\u5b9aIMEI\u53f7\u6d4b\u8bd5-\u6e38\u620f\",\"package\":\"com.lvdoudou01.jinli\",\"hashTime\":\"1\"},\"timeStamp\":1487987760,\"startTime\":1487987760,\"source\":\"GameDetailView\"}}}";
    }
    
    private static final String UUID_DIVIDER = "U_U";
    
    public static class Const {
        public static int RAND_CONST = 11;
    }
}
