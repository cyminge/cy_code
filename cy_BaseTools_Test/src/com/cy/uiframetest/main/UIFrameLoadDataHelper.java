package com.cy.uiframetest.main;

import com.cy.global.WatchDog;
import com.cy.threadpool.NormalThreadPool;
import com.cy.uiframe.main.BaseLoadDataHelper;
import com.cy.uiframe.main.load.ILoadDataHelper;
import com.cy.uiframe.main.load.IUrlBean;

public class UIFrameLoadDataHelper extends BaseLoadDataHelper {

	public UIFrameLoadDataHelper(IUrlBean urlBean, ILoadDataHelper loadDataHelper) {
		super(urlBean, loadDataHelper);
	}
	
	@Override
	public void startLoad() {
		clearCacheData(); // 测试用，每次都清空数据，验证数据加载流程
		super.startLoad();
	}
	
	@Override
	public void startCheck() {
		NormalThreadPool.getInstance().post(new Runnable() {
            @Override
            public void run() {
                String data = doPost();
                data = "[{\"reward\":\"\",\"downloadCount\":\"2.1万下载\",\"link\":\"http://s.game.gtest.gionee.com/Attachments/dev/apks/2016/01/05/1451982803107.apk\",\"package\":\"com.joym.xiongdakuaipao.gl.am\",\"img\":\"http://s.game.gtest.gionee.com/Attachments/dev/icons/2016/01/05/568b84d2d263b.png.144.png\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":359,\"size\":\"3.23M\",\"vipGift\":0,\"resume\":\"8最专业的熊出没之熊大快跑攻略！\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"熊出没之熊大快跑终极攻略\",\"hot\":2,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"453下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/06/12/1434100549425_signed.apk\",\"package\":\"com.lxd.meishitegongdui.jinli\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2015/06/13/557c02cc49ac2.png.144.png\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":307,\"size\":\"11.64M\",\"vipGift\":0,\"resume\":\"3rrrrrrrrrr\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"美食特工队\",\"hot\":0,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"1742下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/06/13/1434167627903.apk\",\"package\":\"com.huntdreams.t1.am\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2015/06/13/557babd188d83.png.144.png.webp\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":306,\"size\":\"0.95M\",\"vipGift\":0,\"resume\":\"这是简述\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"专门测试应用状态\",\"hot\":0,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"111下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/05/20/1432109644385.apk\",\"package\":\"com.mobilefish.pig.nanjingyixun.jinli\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2015/06/17/5580ef94ab884.png.144.png\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":281,\"size\":\"12.17M\",\"vipGift\":0,\"resume\":\"2222222222222\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"猪小弟塔防\",\"hot\":0,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"1156下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/06/13/1434190081868_signed.apk\",\"package\":\"com.yunwang.xxlkxtgb.jd\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2015/06/13/557bd6683f33f.png.144.png.webp\",\"score\":5,\"viewType\":\"GameDetailView\",\"gameid\":278,\"size\":\"4.90M\",\"vipGift\":0,\"resume\":\"3rrrrrrrrr\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"消消乐开心糖果版\",\"hot\":0,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"34下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/06/12/1434109291920.apk\",\"package\":\"com.xiaoao.riskSnipe\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2014/12/12/548a9c091113c.png.144.png.webp\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":277,\"size\":\"16.55M\",\"vipGift\":0,\"resume\":\"asdfffffffffffffff\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"全民枪神\",\"hot\":0,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"2.6万下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/06/08/1433750377568.apk\",\"package\":\"com.forgame.puzzlebobble\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2015/06/08/5575473aba56e.png.144.png.webp\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":275,\"size\":\"39.75M\",\"vipGift\":0,\"resume\":\"3\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"泡泡龙官方正版\",\"hot\":0,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"1938下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/06/04/1433398743915.apk\",\"package\":\"project.race2.jinli\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2015/06/04/556feb0d0649f.png.144.png.webp\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":273,\"size\":\"15.54M\",\"vipGift\":0,\"resume\":\"3D终极车神2\",\"category\":\"\",\"commonGift\":0,\"attach\":0,\"name\":\"3D终极车神2\",\"hot\":0,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"3876下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/06/02/1433214069994.apk\",\"package\":\"com.xsjme.fancy.am\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2015/06/02/556d155dd3293.png.144.png.webp\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":272,\"size\":\"113.35M\",\"vipGift\":0,\"resume\":\"eeeeeeeeeeeeee\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"Rievia Senki\",\"hot\":0,\"freedl\":\"\"},{\"reward\":\"\",\"downloadCount\":\"1428下载\",\"link\":\"http://s.game.3gtest.gionee.com/Attachments/dev/apks/2015/05/26/1432626946515.apk?n=热血群侠1.0.202.apk\",\"package\":\"com.gamejazz.rxqx.am\",\"img\":\"http://s.game.3gtest.gionee.com/Attachments/dev/icons/2015/05/26/55642748da809.png.144.png\",\"score\":0,\"viewType\":\"GameDetailView\",\"gameid\":270,\"size\":\"23.72M\",\"vipGift\":0,\"resume\":\"1\",\"category\":\"破解游戏\",\"commonGift\":0,\"attach\":0,\"name\":\"热血群侠\",\"hot\":0,\"freedl\":\"\"}]";
                if (isRequestDataSucc(data)) {
                	onLoadDataSucc(data);
                } else {
                    WatchDog.post(new Runnable() {
                        @Override
                        public void run() {
                        	onLoadDataError();
                        }
                    });
                }
            }
        });
	}
	
}
