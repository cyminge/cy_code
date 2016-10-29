package com.mogoo;

/**
 * 输出时是左上直角三角形
 * 
 * @author wkl
 * 
 */
public class UpperLeftTriangle {

	public static void main(String[] args) {
		String b = new String("123456");
		char[] d = b.toCharArray();
		int a = d.length;
		int t = d.length;
		String sd = "";
		for (int j = 0; j < t; j++) {
			for (int i = 0; i < a; i++) {
				sd += d[i];
			}
			a = a - 1;
			System.out.println(sd);
			sd = "";
		}
	}
}
