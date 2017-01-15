package com.cy.uiframetest.receclerview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cy.constant.Constant;
import com.cy.global.WatchDog;
import com.cy.test.R;
import com.cy.uiframe.recyclerview.AbstractRecyclerViewHolder;
import com.cy.uiframetest.bean.ChunkDissertationData;
import com.cy.uiframetest.receclerview.ParallaxScrollView.OnTouchDispatcher;
import com.cy.utils.Utils;

@SuppressLint("ClickableViewAccessibility") 
public class ChunkDissertationViewHolder extends AbstractRecyclerViewHolder<ChunkDissertationData> {

	private static final int ITEM_VISIBLE_COUNT = 2;
	public static final int DISSERTATION_ITEM_MAX_COUNT = 8;
	
	private TextView mTitle;
    private View mMore;
    private ImageView mBgView;
    private ParallaxScrollView mImageScrollView;
    private ParallaxScrollView mScrollView;
    
//    protected ArrayList<?> mSubHolder = new ArrayList<?>();

	public ChunkDissertationViewHolder(View itemView) {
		super(itemView);
	}
    
	@Override
	public void initItemView(View itemView) {
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mMore = itemView.findViewById(R.id.view_more);
        mMore.setOnClickListener(mHolderClickListener);
        mBgView = (ImageView) itemView.findViewById(R.id.dissertation_bg_view);
        
        mScrollView = (ParallaxScrollView) itemView.findViewById(R.id.home_dissertation_scrollview);
//        mScrollView.setOnScrollChangedListener(new OnScrollChangedListener() {
//
//            @Override
//            public void onScrollChanged(int x) {
//                BrickDissertationData dissertationData = (BrickDissertationData) mScrollView.getTag();
//                dissertationData.mX = x;
//            }
//        });

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
                .getDimension(R.dimen.chosen_dissertation_place_holder_view_width), (int) WatchDog.getContext().getResources().getDimension(R.dimen.chosen_dissertation_game_info_height)));
        parent.addView(placeHolderView);

        for (int index = 0; index < DISSERTATION_ITEM_MAX_COUNT; index++) {
//            View item = inflater.inflate(R.layout.uiframe_dissertation_game_info, parent, false);
//            LinearLayout.LayoutParams param = createLayoutParams(item, index, itemMargin);
//            item.setId(getCurViewId(index));
//            parent.addView(item, param);
//            initSubHolder(item);
        }
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
	public void setItemView(ChunkDissertationData t) {
		
	}

}
