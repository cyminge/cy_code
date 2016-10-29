package com.cy.string;

public interface FullNameStyle {
	public static final int UNDEFINED = 0;
    public static final int WESTERN = 1;
    /***
     * Used if the name is written in Hanzi/Kanji/Hanja and we could not determine
     * which specific language it belongs to: Chinese, Japanese or Korean.
     */
    public static final int CJK = 2;
    public static final int CHINESE = 3;
    public static final int JAPANESE = 4;
    public static final int KOREAN = 5;
}
