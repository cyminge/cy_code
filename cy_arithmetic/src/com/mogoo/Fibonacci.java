package com.mogoo;

public class Fibonacci {

	public static void main(String[] args) {

		long a = System.nanoTime();
		long one = computeIteratively(92);
		long b = System.nanoTime();
		long two = computeIteratively(93);
		long c = System.nanoTime();
		long three = computeIteratively2(94);
		long d = System.nanoTime();
		
		System.out.println("one = "+ one);
		System.out.println("  two = "+ two);
		System.out.println("three = "+ three);
		System.out.println("digui = "+(b - a));
		System.out.println("diedai_1 = "+(c - b));
		System.out.println("diedai_2 = "+(d - c));
		
	}

	// 递归：消耗大量栈空间
	public static long computeRecursivelyWithLoop(int n) {
		if (n > 1) {
			long result = 1;
			do {
				result += computeRecursivelyWithLoop(n - 2);
				n--;
			} while (n > 1);
			return result;
		}
		return n;
	}

	// 迭代 ：返回long的话只能计算到92项
	public static long computeIteratively(int n) {
		if (n > 1) {
			long a = 0, b = 1;
			do {
				long temp = b;
				b += a;
				a = temp;
			} while (--n > 1);
			return b;
		}
		return n;
	}

	// 迭代：返回long的话只能计算到92项
	public static long computeIteratively2(int n) {
		if (n > 1) {
			long a = 1, b = 1;
			n--;
			a = n & 1;
			n /= 2;
			while (n-- > 0) {
				a += b;
				b += a;
			}
			return b;
		}
		return n;
	}

}
