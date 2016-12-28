package com.cy.uiframe.main.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class GameProgressBar extends ProgressBar {

    private static final int LOLLIPOP_PROGRESS_BAR_COLOR = Color.parseColor("#FFFF9000");

    public GameProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
	
	public GameProgressBar(Context context) {
		this(context,null);
	}

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList colorStateList = ColorStateList.valueOf(LOLLIPOP_PROGRESS_BAR_COLOR);
            setIndeterminateTintList(colorStateList);
            setSecondaryProgressTintList(colorStateList);
            setProgressTintList(colorStateList);
        }

    }
}
