package com.mogoo;

/**
 * 获得任意一个整数的阶乘
 * 
 * @param n
 * @returnn!
 */
public class Factorial {

	public int factorial(int num) {
		// 递归
		if (num == 1) {
			return 1;
		}
		return num * factorial(num - 1);
	}
}
