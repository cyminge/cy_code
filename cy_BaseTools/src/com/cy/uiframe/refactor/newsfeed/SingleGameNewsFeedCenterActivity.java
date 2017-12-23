package com.cy.uiframe.refactor.newsfeed;


import java.util.List;

import android.os.Bundle;

import com.cy.global.BaseActivity;

/**
 * Created by JLB6088 on 2017/12/21.
 */

public class SingleGameNewsFeedCenterActivity extends BaseActivity {

//    private MultiTypeAdapter adapter;
    private List<Object> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list);
//        RecyclerView recyclerView = findViewById(R.id.list);
//
//        adapter = new MultiTypeAdapter();
//        adapter.register();
////        adapter.register(TextItem.class, new TextItemViewBinder());
////        adapter.register(ImageItem.class, new ImageItemViewBinder());
////        adapter.register(RichItem.class, new RichItemViewBinder());
////        recyclerView.setAdapter(adapter);
////
////        TextItem textItem = new TextItem("world");
////        ImageItem imageItem = new ImageItem(R.mipmap.ic_launcher);
////        RichItem richItem = new RichItem("小艾大人赛高", R.mipmap.avatar);
////
////        items = new ArrayList<>();
////        for (int i = 0; i < 20; i++) {
////            items.add(textItem);
////            items.add(imageItem);
////            items.add(richItem);
////        }
////        adapter.setItems(items);
//        adapter.notifyDataSetChanged();
    }
}
