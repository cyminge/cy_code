package com.cy.uiframe.refactor.newsfeed;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.uiframe.refactor.recyclerview.GioneeRecyclerAdapter;
import com.cy.uiframe.refactor.recyclerview.GioneeRecyclerView;


/**
 * cyminge RecyclerView与RecyclerAdapter的关系应该是？ // TODO
 * Created by JLB6088 on 2017/12/22.
 */

public class BaseRecyclerView extends GioneeRecyclerView {

    public BaseRecyclerView(Context context) {
        super(context);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected GioneeRecyclerAdapter initAdapter() {
        return new GioneeRecyclerAdapter();
    }

    @Override
    protected void register(GioneeRecyclerAdapter adapter) {
//        adapter.register(ChunkSimpleBannerData.class, new );
    }
}
