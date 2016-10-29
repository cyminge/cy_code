package com.mogoo;

/**
 * 对char类型数组进行排序 
 * 思路：冒泡排序
 * @author wkl
 *
 */
public class CharArraySort {
	public static void sortStringArray(char[] arrStr) {
		char temp;
		for (int i = 0; i < arrStr.length; i++) {
			for (int j = arrStr.length - 1; j > i; j--) {
				if (arrStr[i] > arrStr[j]) {
					temp = arrStr[i];
					arrStr[i] = arrStr[j];
					arrStr[j] = temp;
				}
			}
		}
	}

	public static void main(String[] args) {

		char[] arrStr = { 'j', 'a', 'v', 'a', '1', '5' };

		sortStringArray(arrStr);

		for (int i = 0; i < arrStr.length; i++) {
			System.out.println(arrStr[i]);
		}

	}
}
