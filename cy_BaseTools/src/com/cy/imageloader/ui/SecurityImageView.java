package com.cy.imageloader.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cy.R;

public class SecurityImageView extends ImageView {
    private static final String TAG = "SecurityImageView";
    private static final String CONTENT_DESCRIPTION = "contentDescription";
    private String mContentDescription;

    public SecurityImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int attributeCount = attrs.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = attrs.getAttributeName(i);
            if (CONTENT_DESCRIPTION.equals(attributeName)) {
                int attributeResourceValue = attrs.getAttributeResourceValue(i, 0);
                mContentDescription = getResources().getString(attributeResourceValue);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            setDefaultImage();
//            LogUtils.loge(TAG, "SecurityImageView->onDraw ", e);
//            ExceptionSendUtils.appendLogFile("SecurityImageView exception, des = " + mContentDescription);
        }
    }

    protected void setDefaultImage() {
        setImageResource(R.drawable.default_icon);
    }

}
