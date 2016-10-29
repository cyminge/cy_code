package com.mogoo.importance.ok;

/**
 * 冒泡
 * 
 * @author cyminge
 */
public class BubbleUp {
	public static void main(String[] args) {

		// bubbleSort1();
		// bubbleSort2();
		bubbleSort3();
	}

	public static void bubbleSort3() {
		int[] myInt = new int[] { 37, 47, 23, -5, 19, 56 };
		int t = 0;// 比较运算时交换中间数
		final int len = myInt.length;
		for (int i = 0; i < len - 1; i++) {// 控制所有数字都被比较循环
			for (int j = 0; j < len - i - 1; j++) {// 控制每个数和数组里面其他数一一比较
				if (myInt[j] > myInt[j + 1]) {
					t = myInt[j];
					myInt[j] = myInt[j + 1];
					myInt[j + 1] = t;
				}
			}
		}
		for (int r = 0; r < len; r++) {// 控制输出结果
			System.out.println(myInt[r]);
		}
	}

	/**
	 * 这种方式效率不高
	 */
	public static void bubbleSort2() {
		int traverseCounts = 0;

		int[] list = { 1, 3, 4, 7, 8, 2, 6, 5, 9 };
		// int[] list = { 1, 3, 5, 4, 2 };
		int i, j, temp;
		boolean noChange = false;
		for (j = 0; j < list.length; j++) {
			noChange = false;
			for (i = list.length - 1; i > j; i--) {
				traverseCounts++;
				if (list[j] > list[i]) {
					temp = list[i];
					list[i] = list[j];
					list[j] = temp;

					noChange = true;

				}
			}
			// if(!noChange) {
			// break;
			// }
		}

		System.out.println("traverseCounts=" + traverseCounts);
		for (int k = 0; k < list.length; k++) {
			System.out.println(list[k]);
		}
	}

	public static void bubbleSort1() {

		int traverseCounts = 0;

		int[] r = { 1, 3, 4, 7, 8, 2, 6, 5, 9 };
		int i, j, temp = 0; // 交换标志
		boolean exchange = false;
		for (i = 0; i < r.length; i++) // 最多做R.Length-1趟排序
		{
			exchange = false; // 本趟排序开始前，交换标志应为假
			for (j = r.length - 2; j >= i; j--) {
				traverseCounts++;
				if (r[j + 1] < r[j])// 交换条件
				{
					temp = r[j + 1];
					r[j + 1] = r[j];
					r[j] = temp;
					exchange = true; // 发生了交换，故将交换标志置为真
				}
			}
			if (!exchange) // 本趟排序未发生交换，提前终止算法
			{
				break;
			}
		}
		System.out.println("traverseCounts=" + traverseCounts);
		for (int k = 0; k < r.length; k++) {
			System.out.println(r[k]);
		}
	}
}
