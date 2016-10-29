package com.mogoo.importance;

import java.util.Scanner;

/**
 * 原题如下：用1、2、2、3、4、5这六个数字，用java写一个main函数，打印出所有不同的排列，如：512234、412345等，要求："4"
 * 不能在第三位，"3"与"5"不能相连.
 * 思路是这样的，对于任意一个串利用递归进行排列时，我们是循环串中的每个字符到第一个字符进行递归。如果串中字符出现重复的话
 * ，则重复的字符只可以利用递归算法一次，即只要与前面相同的字符循环到第一个字符时不调用递归就可以避免重复，为此，我们只需要按如下方式修改算法：
 */
public class Test_122345_2 {

	static int count = 0;

	public static void main(String[] arg) {
		Scanner r = new Scanner(System.in);
		String s = r.nextLine();
		Pailie(s, "");
		System.out.println("Total:" + count);
	}

	static void Pailie(String s, String p) {
		if (s.length() < 1) {
			System.out.println(p);// 字符串长度小于1，换行
			count++;
		} else {
			int index[] = new int[s.length()];
			for (int i = 0; i < s.length(); i++)
				// 该循环将所有字符的第一次出现的位置记录在数组index中
				index[i] = s.indexOf(s.charAt(i));
			for (int i = 0; i < s.length(); i++) {
				if (i == index[i])// 只有当循环数与第一次记录数相等时才递归，保证相同字符中的第一个调用
					Pailie(s.substring(1), p + s.substring(0, 1));// 递归，打印其它字符
				s = s.substring(1) + s.substring(0, 1);// 循环移位
			}
		}
	}

}
