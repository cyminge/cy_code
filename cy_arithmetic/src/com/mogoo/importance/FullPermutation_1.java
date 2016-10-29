package com.mogoo.importance;

/**
 * 全排列
 * @author wkl
 *
 */
public class FullPermutation_1 {
	String[] list;
	StringBuffer sb;
	int len, start;

	FullPermutation_1(String in) {
		len = Integer.parseInt(in);
		list = new String[len];
		for (int i = 0; i < len; i++)
			list[i] = String.valueOf(i + 1);
		permutation(0, len - 1);
	}

	/** 交换 */
	void swap(int c1, int c2) {
		String temp = list[c1];
		list[c1] = list[c2];
		list[c2] = temp;
	}

	/** 递归 */
	void permutation(int m, int n) {
		if (m == n) {
			sb = new StringBuffer();
			for (int i = 0; i < len; i++)
				sb.append(list[i]);
			sb.append(" ");
			System.out.println(sb);
		} else {
			for (int i = m; i <= n; i++) {
				swap(m, i);// 交换
				permutation(m + 1, n);
				swap(m, i);// 还原
			}
		}
	}

	public static void main(String args[]) {
		new FullPermutation_1("3");
	}
}
