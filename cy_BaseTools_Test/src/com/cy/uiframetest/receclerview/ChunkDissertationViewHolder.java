package com.cy.uiframetest.receclerview;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.entity.GameBean;
import com.cy.frame.downloader.ui.IProgressButton;
import com.cy.global.WatchDog;
import com.cy.imageloader.ImageLoader;
import com.cy.imageloader.ui.AlphaAnimImageView;
import com.cy.test.R;
import com.cy.uiframe.recyclerview.AbstractRecyclerViewHolder;
import com.cy.uiframetest.bean.ChunkDissertationData;
import com.cy.uiframetest.receclerview.ParallaxScrollView.OnTouchDispatcher;
import com.cy.utils.Utils;

@SuppressLint("ClickableViewAccessibility")
public class ChunkDissertationViewHolder extends AbstractRecyclerViewHolder<ChunkDissertationData> {

	private static final int HOLDER_FIRST_VIEW_ID = 1000;

	private static final int ITEM_VISIBLE_COUNT = 2;
	public static final int DISSERTATION_ITEM_MAX_COUNT = 8;

	private TextView mTitle;
	private View mMore;
	private ImageView mBgView;
	private ParallaxScrollView mImageScrollView;
	private ParallaxScrollView mScrollView;

	protected ArrayList<AbstractRecyclerViewHolder<GameBean>> mSubHolder = new ArrayList<AbstractRecyclerViewHolder<GameBean>>();

	public ChunkDissertationViewHolder(View itemView) {
		super(itemView);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
	}

	@Override
	public void initItemView(View itemView) {
		mTitle = (TextView) itemView.findViewById(R.id.title);
		mMore = itemView.findViewById(R.id.view_more);
		mMore.setOnClickListener(mHolderClickListener);
		mBgView = (ImageView) itemView.findViewById(R.id.dissertation_bg_view);

		mScrollView = (ParallaxScrollView) itemView.findViewById(R.id.home_dissertation_scrollview);
		// mScrollView.setOnScrollChangedListener(new OnScrollChangedListener()
		// {
		//
		// @Override
		// public void onScrollChanged(int x) {
		// BrickDissertationData dissertationData = (BrickDissertationData)
		// mScrollView.getTag();
		// dissertationData.mX = x;
		// }
		// });

		mImageScrollView = (ParallaxScrollView) itemView
				.findViewById(R.id.home_dissertation_image_scrollview);

		mScrollView.setOnTouchListener(new OnTouchDispatcher(mImageScrollView, false));
		mImageScrollView.setOnTouchListener(new OnTouchDispatcher(null, true));

		initSubItem(itemView);
	}

	private void initSubItem(View convertView) {
		LayoutInflater inflater = LayoutInflater.from(WatchDog.getContext());
		LinearLayout parent = (LinearLayout) convertView.findViewById(R.id.dissertation_info_parent);
		int itemMargin = computeItemMargin();

		View placeHolderView = new View(WatchDog.getContext());
		placeHolderView.setLayoutParams(new LayoutParams((int) WatchDog.getContext().getResources()
				.getDimension(R.dimen.chosen_dissertation_place_holder_view_width), (int) WatchDog
				.getContext().getResources().getDimension(R.dimen.chosen_dissertation_game_info_height)));
		parent.addView(placeHolderView);

		for (int index = 0; index < DISSERTATION_ITEM_MAX_COUNT; index++) {
			View item = inflater.inflate(R.layout.uiframe_dissertation_game_info, parent, false);
			LinearLayout.LayoutParams param = createLayoutParams(item, index, itemMargin);
			item.setId(getCurViewId(index));
			parent.addView(item, param);
			initSubHolder(item);
		}
	}

	/**
	 * 这个有什么用？ 用于点击的时候判断是否是属于专题里的ID，这个感觉不需要这样子做，要怎么改？
	 * 
	 * @param offset
	 * @return
	 */
	private int getCurViewId(int offset) {
		return HOLDER_FIRST_VIEW_ID + offset;
	}

	private void initSubHolder(View view) {
		DissertationItemHolder holder = new DissertationItemHolder(view);
		holder.initItemView(view);
		if(null == mSubHolder) {
			mSubHolder = new ArrayList<AbstractRecyclerViewHolder<GameBean>>();
		}
		mSubHolder.add(holder);
	}

	private int computeItemMargin() {
		int viewWidth = Utils.getPhonePixels()[0];
		int itemWidth = (int) WatchDog.getContext().getResources()
				.getDimension(R.dimen.chosen_dissertation_game_info_width);
		int placeHolderWidth = (int) WatchDog.getContext().getResources()
				.getDimension(R.dimen.chosen_dissertation_place_holder_view_width);
		return ((viewWidth - placeHolderWidth - itemWidth * ITEM_VISIBLE_COUNT - itemWidth * 2 / 3) / ITEM_VISIBLE_COUNT);
	}

	private LinearLayout.LayoutParams createLayoutParams(View item, int offset, int itemSpan) {
		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) item.getLayoutParams();
		if (offset > Constant.ZERO_NUM) {
			param.leftMargin = itemSpan;
		}
		return param;
	}

	@Override
	public void setItemView(final ChunkDissertationData data) {
		// final BrickDissertationData dissertationData =
		// (BrickDissertationData) data;
		// mdissertationData = dissertationData;
		// mScrollView.setTag(dissertationData);
		// GNApplication.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// mScrollView.scrollTo((int) dissertationData.mX, (int)
		// mScrollView.getY());
		// }
		// }, Constant.MILLIS_30);
		//
		// super.setItemView(position, data);
		mScrollView.setTag(data);
		WatchDog.postDelayed(new Runnable() {

			@Override
			public void run() {
				mScrollView.scrollTo((int) data.mX, (int) mScrollView.getY());
			}
		}, Constant.MILLIS_30);
		mTitle.setText(data.title);

		mMore.setVisibility(data.hasMore() ? View.VISIBLE : View.GONE);
		mMore.setTag(data);

		ImageLoader.INSTANCE.displayImage(data.itemBg, mBgView, R.drawable.uiframe_icon_big_rectangle_bg);

		setSubItemView(data.subItems);
	}

	private void setSubItemView(ArrayList<GameBean> data) {
		int holderCount = mSubHolder.size();
		int dataCount = data.size();
		for (int index = 0; index < holderCount; index++) {
			AbstractRecyclerViewHolder<GameBean> subHolder = mSubHolder.get(index);
			GameBean subData;
			if (index < dataCount) {
				subData = data.get(index);
			} else {
				subData = null;
			}
			subHolder.setItemView(subData);
		}
	}

	public static class DissertationItemHolder extends AbstractRecyclerViewHolder<GameBean> {

		private View mItemView;
		
		protected ImageView mSubscript;
	    private ImageView mGiftIcon;

	    protected AlphaAnimImageView mGameIcon;
	    protected TextView mGameName;
	    protected IProgressButton mButton;
	    
		public DissertationItemHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void initItemView(View itemView) {
			mItemView = itemView;
			mItemView.setOnClickListener(mHolderClickListener);
			
			mSubscript = (ImageView) itemView.findViewById(R.id.game_list_subscript);
	        mGiftIcon = (ImageView) itemView.findViewById(R.id.game_list_gift);
	        
	        mGameIcon = (AlphaAnimImageView) itemView.findViewById(R.id.alpha_anim_icon);
	        mGameName = (TextView) itemView.findViewById(R.id.game_list_name);
	        mButton = (IProgressButton) itemView.findViewById(R.id.game_list_button);
	        mButton.setOnClickListener(mHolderClickListener);
		}

		@Override
		public void setItemView(GameBean data) {
			if (null == data) {
				mItemView.setVisibility(View.GONE);
				return;
			}

			mItemView.setVisibility(View.VISIBLE);
			mItemView.setTag(data);
			
			setSubscript(data.hot);
	        setGiftIcon(data.hasGift());
	        
	        ImageLoader.INSTANCE.displayImage(data.img, mGameIcon, R.drawable.icon_samll_round_bg);
	        if (mGameName != null) {
	            mGameName.setText(data.packageName);
	        }
	        setButtonState(data);
		}
		
	    public void setButtonState(GameBean data) {
	    	data.mStatus = getButtonStatus(data);
	        mButton.setButton(data, data.mStatus, ButtonStatusManager.getProgress(data));
	    }
	    
	    protected int getButtonStatus(GameBean data) {
	        return ButtonStatusManager.getButtonStatus(data);
	    }
		
		protected void setSubscript(int type) {
	        if (mSubscript == null) {
	            return;
	        }
	        
	        mSubscript.setVisibility(View.GONE);

//	        if (SubscriptManager.isTypeValid(type) && SubscriptManager.setSubscript(type, mSubscript)) {
//	            mSubscript.setVisibility(View.VISIBLE);
//	        } else {
//	            mSubscript.setVisibility(View.GONE);
//	        }
	    }

	    private void setGiftIcon(boolean hasGift) {
	        if (hasGift) {
	            mGiftIcon.setImageResource(R.drawable.uiframe_game_list_gift_icon);
	            mGiftIcon.setVisibility(View.VISIBLE);
	        } else {
	            mGiftIcon.setVisibility(View.GONE);
	        }
	    }
	    
	}

}
