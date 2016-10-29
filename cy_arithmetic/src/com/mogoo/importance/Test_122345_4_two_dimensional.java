package com.mogoo.importance;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * 1 把问题归结为图结构的遍历问题。实际上6个数字就是六个结点，把六个结点连接成无向连通图，对于每一个结点求这个图形的遍历路径，
 * 所有结点的遍历路径就是最后对这6个数字的排列组合结果集。 2 显然这个结果集还未达到题目的要求。从以下几个方面考虑： 1.
 * 3，5不能相连：实际要求这个连通图的结点3，5之间不能连通, 可在构造图结构时就满足改条件，然后再遍历图。 2. 不能有重复:
 * 考虑到有两个2，明显会存在重复结果，可以把结果集放在TreeSet中过滤重复结果 3. 4不能在第三位: 仍旧在结果集中去除满足此条件的结果。
 * 
 * 采用二维数组定义图结构，最后的代码是：
 */
public class Test_122345_4_two_dimensional {

	private String[] b = new String[] { "1", "2", "2", "3", "4", "5" };
	private int n = b.length;
	private boolean[] visited = new boolean[n];
	private int[][] a = new int[n][n];
	private String result = "";
	private TreeSet set = new TreeSet();

	public static void main(String[] args) {
		new Test_122345_4_two_dimensional().start();
	}

	private void start() {

		// Initial the map a[][]
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					a[i][j] = 0;
				} else {
					a[i][j] = 1;
				}
			}
		}

		// 3 and 5 can not be the neighbor.
		a[3][5] = 0;
		a[5][3] = 0;

		// Begin to depth　search.
		for (int i = 0; i < n; i++) {
			this.depthFirstSearch(i);
		}

		// Print result treeset.
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String string = (String) it.next();
			// "4" can not be the third position.
			if (string.indexOf("4") != 2) {
				System.out.println(string);
			}
		}
	}

	private void depthFirstSearch(int startIndex) {
		visited[startIndex] = true;
		result = result + b[startIndex];
		if (result.length() == n) {
			// Filt the duplicate value.
			set.add(result);
		}
		for (int j = 0; j < n; j++) {
			if (a[startIndex][j] == 1 && visited[j] == false) {
				depthFirstSearch(j);
			} else {
				continue;
			}
		}

		// restore the result value and visited value after listing a node.
		result = result.substring(0, result.length() - 1);
		visited[startIndex] = false;
	}
}
