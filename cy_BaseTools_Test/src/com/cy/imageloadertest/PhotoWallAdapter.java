package com.cy.imageloadertest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cy.imageloader.ImageLoader;
import com.cy.test.R;

public class PhotoWallAdapter extends BaseAdapter {

    private Context mContext;
    private ImageLoader mImageLoader;

    public PhotoWallAdapter(Context context, ImageLoader iconManager) {
        mContext = context;
        mImageLoader = iconManager;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // if (null == memberInfoList || memberInfoList.size() <= position) {
        // return null;
        // }

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.photo_layout, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        mImageLoader.displayImage(Images.imageThumbUrls[position], holder.imageView, R.drawable.default_icon);
        
//        notifyDataSetChanged();
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }

    @Override
    public int getCount() {
        return Images.imageThumbUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
