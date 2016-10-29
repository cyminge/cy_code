package com.cy.animation.gallery.one;

import com.cy.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class GalleryOneActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_gallery);

		Integer[] images = { R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h };
		// ImageAdapter adapter = new ImageAdapter(this, images);
		// adapter.createReflectedImages();
		// GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01);
		// galleryFlow.setAdapter(adapter);

		ImageAdapter adapter = new ImageAdapter(this, images);
		adapter.createReflectedImages();// 创建倒影效果
		GalleryFlow galleryFlow = (GalleryFlow) this.findViewById(R.id.Gallery01);
		galleryFlow.setFadingEdgeLength(0);
//		galleryFlow.setSpacing(-100); // 图片之间的间距
		galleryFlow.setUnselectedAlpha(100); // 设置Gallery中未选中项的透明度(alpha)值
		galleryFlow.setAdapter(adapter);

		galleryFlow.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
			}
		});
		galleryFlow.setSelection(4);

	}
}