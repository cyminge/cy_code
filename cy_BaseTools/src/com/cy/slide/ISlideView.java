package com.cy.slide;

public interface ISlideView {
    public boolean postDelayed(Runnable action, long delayMillis);

    public void onSingleTapUp();

    public void onBeginScroll();

    public int getViewWidth();

    public boolean switchImage(boolean isLeft);

    public int getCurrentIndex();

    public int getCount();

    /**
     * 更新UI
     */
    public void update();
}
