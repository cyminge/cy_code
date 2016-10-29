package com.cy;

public class Main4 {

    public static void main(String[] args) {
        changeJson();
    }

    static void changeJson() {

        int i = 11;
        
        System.out.println(i%2==0?"偶数":"奇数");
        System.out.println(i%2==1?"奇数":"偶数");


        // 大作预约
        // String subscribe =
        // "{\"viewType\":\"GameSubscribeListView\",\"listItemType\":\"SubscribeGames\",\"title\":\"\u9884\u7ea6\u9001\u91d1\u7acbS8\u624b\u673a\",\"total\":\"10\",\"param\":{\"title\":\"\u5927\u4f5c\u9884\u7ea6\"},\"subscribeItems\":[{\"id\":\"36\",\"name\":\"\u795e\u66f2\u4e4b\u7b26\u6587\u82f1\u96c4\",\"resume\":\"\u5973\u795e\u5218\u4ea6\u83f2\u4ee3\u8a00SRPG\u9b54\u5e7b\u624b\u6e38\u5927\u4f5c\",\"package\":\"com.changic.jl_rh.am\",\"img\":\"http://assets.gionee.com/attachs/game/reservedactivity/201607/577b029b3b133.png\",\"images\":[\"http://assets.gionee.com/attachs/game/reservedactivity/201607/57765266de1f2.png_480x800.png\",\"http://assets.gionee.com/attachs/game/reservedactivity/201607/5776526e40ec4.png_480x800.png\",\"http://assets.gionee.com/attachs/game/reservedactivity/201607/57765274ccd52.png_480x800.png\",\"http://assets.gionee.com/attachs/game/reservedactivity/201607/5776527c109fa.png_480x800.png\",\"http://assets.gionee.com/attachs/game/reservedactivity/201607/5776528286cfb.png_480x800.png\"],\"time\":\"1467856800\",\"subscribeCount\":\"432\",\"subscribe\":false}]}";
    }
    
    public static class Const {
        public static int RAND_CONST = 11;
    }
}
