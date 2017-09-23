package com.cy.Syntax;

public class SyntaxTest {

    public static void main(String[] args) {
        int mActivityCount = 1;
        boolean isOnForeground = mActivityCount == 0 ? false : true;
        System.out.println(isOnForeground);
        isOnForeground = mActivityCount > 0;
        System.out.println(isOnForeground);
    }
}
