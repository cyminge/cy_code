package com.cy.uiframetest.main;

import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.cy.frame.downloader.util.JsonConstant;
import com.cy.slide.SlideView;
import com.cy.slide.SlideViewHelper;
import com.cy.slide.SlideViewHelper.DataParsedListener;
import com.cy.test.R;
import com.cy.uiframe.main.LaunchActivityHelper;
import com.cy.uiframe.main.load.AbstractLoadDataHelper;
import com.cy.uiframe.main.load.ILoadDataHelper;
import com.cy.uiframe.main.load.IUrlBean;
import com.cy.uiframe.recyclerview.AbstractRecyclerView;
import com.cy.uiframetest.receclerview.BaseBean;
import com.cy.utils.device.DeviceUtil;

@SuppressWarnings("unchecked")
public class UIFrameLaunchActivityHelper extends LaunchActivityHelper {
	
	private static final String SLIDE_VIEW_SHARE_PREF_KEY = "from_UIFrameLaunchActivityHelper";
	private AbstractRecyclerView<BaseBean> mAbstractRecyclerView;
	private SlideViewHelper mSlideViewHelper;
	SlideView mSlideView;

	public UIFrameLaunchActivityHelper(Context context, String url, int layoutId) {
		super(context, url, layoutId);
	}
	
	@Override
	protected AbstractLoadDataHelper createLoadDataHelper(IUrlBean urlBean, ILoadDataHelper loadDataHelper) {
		return new UIFrameLoadDataHelper(urlBean, loadDataHelper);
	}
	
	@Override
	protected void prepareView(View contentView) {
		mAbstractRecyclerView = (AbstractRecyclerView<BaseBean>) contentView.findViewById(R.id.recycle_view);
		mAbstractRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		View headerView = prepareHeaderView();
		if(null != headerView) {
			mAbstractRecyclerView.addHeaderView(headerView);
		}
	}

	protected View prepareHeaderView() {
		View headerView = LayoutInflater.from(mContext).inflate(R.layout.activity_slideview_main, null);
		mSlideView = (SlideView) headerView.findViewById(R.id.slide_view);
		mSlideViewHelper = new SlideViewHelper(mSlideView, mContext);
		return headerView;
	}
	
	private String testSlideJsonStr = "{\"sign\":\"GioneeGameHall\",\"msg\":\"\",\"success\":true,\"data\":{\"slideItems\":[{\"viewType\":\"Link\",\"title\":\"\u53cc\u5341\u4e00\u72c2\u6b22\u4e13\u573a\",\"content\":\"\u53cc\u5341\u4e00\u72c2\u6b22\u4e13\u573a\",\"imageUrl\":\"http://assets.gionee.com/attachs/game/ad/201611/582451e4ccb3b.jpg\",\"param\":{\"url\":\"http://game.gionee.com/client/festival_doubleeleven/index?intersrc=ad11\",\"contentId\":\"\",\"gameId\":\"\",\"title\":\"\u53cc\u5341\u4e00\u72c2\u6b22\u4e13\u573a\"},\"source\":\"\",\"ad_id\":\"2792\"},{\"viewType\":\"GameDetailView\",\"title\":\"\u65f6\u7a7a\u67aa\u6218\",\"content\":\"\u65f6\u7a7a\u67aa\u6218\",\"imageUrl\":\"http://assets.gionee.com/attachs/game/ad/201611/582409f0e4016.jpg\",\"param\":{\"url\":\"\",\"contentId\":\"8353\",\"gameId\":\"8353\",\"title\":\"\u65f6\u7a7a\u67aa\u6218\",\"package\":\"com.yinhan.game.skqz.am\"},\"source\":\"\",\"ad_id\":\"2786\",\"gameId\":\"8353\",\"gameid\":8353,\"name\":\"\u65f6\u7a7a\u67aa\u6218\uff08\u9001M6Plus\uff09\",\"resume\":\"\u706b\u7206\u5f39\u5e55\uff0c\u6fc0\u723d\u67aa\u6218\uff0c\u8001\u53f8\u673a\u4e0a\u8f66\",\"package\":\"com.yinhan.game.skqz.am\",\"img\":\"http://s.dev.gionee.com/Attachments/dev/icons/2016/11/10/5823ffb8e0fc4.png.144.png\",\"link\":\"http://gamedl.gionee.com/Attachments/dev/apks/2016/10/26/147746503222_signed.apk\",\"size\":\"165.71M\",\"category\":\"\u52a8\u4f5c\u5192\u9669\",\"hot\":0,\"commonGift\":1,\"vipGift\":1,\"score\":5,\"freedl\":\"\",\"attach\":1,\"reward\":\"\",\"downloadCount\":\"10.6\u4e07\u4e0b\u8f7d\"},{\"viewType\":\"TopicDetailView\",\"title\":\"\u53cc\u5341\u4e00\u9650\u65f65\u6298\",\"content\":\"\u53cc\u5341\u4e00\u9650\u65f65\u6298\",\"imageUrl\":\"http://assets.gionee.com/attachs/game/ad/201611/58244e6200b16.jpg\",\"param\":{\"url\":\"\",\"contentId\":\"569\",\"gameId\":\"\",\"title\":\"\u53cc\u5341\u4e00\u9650\u65f65\u6298\"},\"source\":\"\",\"ad_id\":\"2790\"},{\"viewType\":\"ActivityDetailView\",\"title\":\"\u5929\u5929\u6597\u5730\u4e3b\",\"content\":\"\u5929\u5929\u6597\u5730\u4e3b\",\"imageUrl\":\"http://assets.gionee.com/attachs/game/ad/201611/58244fcc8ddb2.jpg\",\"param\":{\"url\":\"\",\"contentId\":\"1623\",\"gameId\":\"6311\",\"title\":\"\u5929\u5929\u6597\u5730\u4e3b\",\"package\":\"com.zengame.hlddztzs.p5gjinli\"},\"source\":\"\",\"ad_id\":\"2791\",\"gameId\":\"6311\",\"gameid\":6311,\"name\":\"\u5929\u5929\u6597\u5730\u4e3b\uff08\u771f\u4eba\u7248\uff09\",\"resume\":\"\u5343\u4e07\u7cbe\u82f1\u6597\u5730\u4e3b\uff0c\u5929\u5929\u5927\u5956\u62ff\u4e0d\u505c\",\"package\":\"com.zengame.hlddztzs.p5gjinli\",\"img\":\"http://s.dev.gionee.com/Attachments/dev/icons/2016/11/08/58219b1d05c40.png.144.png\",\"link\":\"http://gamedl.gionee.com/Attachments/dev/apks/2016/10/29/1477712033987.apk\",\"size\":\"20.33M\",\"category\":\"\u68cb\u724c\u5929\u5730\",\"hot\":0,\"commonGift\":1,\"vipGift\":1,\"score\":4,\"freedl\":\"\",\"attach\":1,\"reward\":\"\",\"downloadCount\":\"307.9\u4e07\u4e0b\u8f7d\"}]}}";
	
	@Override
	public boolean onParseData(String data) {
//		 JSONObject sourceObject = new JSONObject(data);
//		 JSONObject dataOjb =  sourceObject.getJSONObject("data");
		
//		try {
//            String str = new JSONObject(data).getString("data");
//            mTabView = Utils.getInflater().inflate(R.layout.tab_viewpager_content, null);
//            mViewPageHelper = new ViewPagerHelper(mActivity, mTabView, data);
//            addView(mTabView);
//
//            return true;
//        } catch (Exception e) {
//            ExceptionSendUtils.sendDataErrorInfo(TAG, jsonData, e);
//            return false;
//        }
		
		setData2SlideView(testSlideJsonStr);
		
		return true;
	}
	
	// LayoutManager.canScrollHorizontally()
	protected boolean setData2SlideView(String slideJsonStr) {
		JSONObject slideObj = null;
		try {
			JSONObject sourceObject = new JSONObject(slideJsonStr);
			slideObj = sourceObject.getJSONObject(JsonConstant.DATA);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
        
		mSlideViewHelper.setDataSource(SLIDE_VIEW_SHARE_PREF_KEY, slideObj, new DataParsedListener() {
			
			@Override
			public void onSlideParseResult(boolean succ) {
				Log.e("cyTest", "getWidth: "+mSlideView.getWidth()+", getHeight: "+mSlideView.getHeight());
				mSlideView.setVisibility(succ ? View.VISIBLE : View.GONE);
				mSlideView.update();
			}
		});
		
		return true;
    }
	
}
