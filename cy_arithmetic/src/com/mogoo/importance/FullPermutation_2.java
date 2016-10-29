package com.mogoo.importance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FullPermutation_2 {
	private static int NUM = 4;

	/**
	 * 递归算法：将数据分为两部分，递归将数据从左侧移右侧实现全排列
	 */
	private static void sort(List<String> datas, List<String> target) {
		if (target.size() == NUM) {
			for (Object obj : target) {
				System.out.print(obj);
			}
			System.out.println();
			return;
		}
		for (int i = 0; i < datas.size(); i++) {
			List<String> newDatas = new ArrayList<String>(datas);
			List<String> newTarget = new ArrayList<String>(target);
			newTarget.add(newDatas.get(i));
			newDatas.remove(i);
			sort(newDatas, newTarget);
		}
	}

	public static void main(String[] args) {
		String[] datas = new String[] { "a", "b", "c", "d" };
		sort(Arrays.asList(datas), new ArrayList<String>());
	}
}
