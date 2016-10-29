package com.cy.imageloadertest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.cy.global.BaseActivity;
import com.cy.imageloader.ImageLoader;
import com.cy.imageloader.listener.PauseOnScrollListener;
import com.cy.test.R;


public class MainActivity extends BaseActivity {
    
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mImageLoader = new ImageLoader(this);
        
        GridView photo_wall = (GridView) findViewById(R.id.photo_wall);
        PhotoWallAdapter adapter = new PhotoWallAdapter(this, mImageLoader);
        photo_wall.setAdapter(adapter);
        photo_wall.setOnScrollListener(new PauseOnScrollListener(mImageLoader, true, true));
        
        LruCache<String, Bitmap> lru = new LruCache<String, Bitmap>(8);
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
