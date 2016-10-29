package com.mogoo;

/**
 * 递归
 * @author cyminge
 *
 */
public class Recursion {
	// 0 1 1 2 3 5 8 13
	public static void main(String[] args) {
		System.out.println(fibonacci(7));
	}
	public static long fibonacci(long m) {
		if (m == 0 || m == 1)
			return m;
		else
			return fibonacci(m - 1) + fibonacci(m - 2);
	}

}
