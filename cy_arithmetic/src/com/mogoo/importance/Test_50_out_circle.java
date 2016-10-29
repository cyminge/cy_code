package com.mogoo.importance;

import java.util.LinkedList;
import java.util.List;

/**
 * 50个人围成一圈数到三和三的倍数时出圈，问剩下的人是谁？在原来的位置是多少？
 * 
 * @author wkl
 */
public class Test_50_out_circle {

	public static void test() {

		// 定义数组并编号
		int[] array = new int[10];
		for (int i = 0; i < array.length; i++) {
			array[i] = i + 1;
		}

		// 当前数数人的编号
		int id = 0;
		// 当前要数的数字
		int number = 1;
		// 数组中值为0的元素个数
		int count = 0;

		for (;;) {
			if (number % 3 == 0) {
				array[id] = 0;
				count++;
				if (count == array.length - 1) {
					break;
				}
			}
			number++;
			while (true) {
				if (id < array.length - 1) {
					id++;
				} else {
					id = 0;
				}
				if (array[id] != 0)
					break;
			}
		}

		for (int i = 0; i < array.length; i++) {
			if (array[i] != 0) {
				System.out.println("最终留下的会是第" + array[i] + "个人");
			}
		}

	}

	public static int cycle(int total, int k) { // 功能方法
		List<Integer> dataList = new LinkedList<Integer>();// 创建链表对象
		for (int i = 0; i < total; i++)
			// 添加数据元素
			dataList.add(new Integer(i + 1));
		int index = -1; // 定义下标，模拟已经去掉一个元素，因此从-1开始
		while (dataList.size() > 1) { // 一直循环去除数据，直到只剩下一个元素
			index = (index + k) % dataList.size();// 得到应该出局的下标
			System.out.print(index);
			System.out.print("---");
			System.out.println(dataList.get(index));
			
			dataList.remove(index--); // 去除元素
		}
		return ((Integer) dataList.get(0)).intValue();// 返回它的值
	}

	// 主方法
	public static void main(String[] args) {
		System.out.println("该数字原来的位置是：" + cycle(10, 3));
//		test();
	}
}
