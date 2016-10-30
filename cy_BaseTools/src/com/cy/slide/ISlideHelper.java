package com.cy.slide;

import java.util.List;

public interface ISlideHelper<T> {

	public void init(List<T> slideData);

	public boolean update(List<T> slideData);
	
	public T getItem(int index);

	public boolean isEmpty();

	public boolean isSingle();
	
	public int getCount();

	public int getCurrIndex();

	public int getPreIndex();

	public int getNextIndex();
	
	public void switchImage(boolean isLeft);
	
	public void switchNext();

	/**
	 * if you want to clear cache data, When the Activity in the onStop state,
	 * use this method. for example : bitmap data. but not recommended to use
	 * it.
	 */
	public void recycle();

	public void destroy();

}
