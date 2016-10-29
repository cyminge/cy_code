package com.mogoo.importance;

/**
 * 快速排序
 * @author wkl
 *
 */
public class QuickSort {
	public static void main(String[] a) {
		int[] list = { 5, 2, 9, 3, 8, 4, 0, 1, 6, 7 };

		System.out.println("Before quick sort:");
		for (int i = 0; i < list.length; i++) {
			System.out.print(list[i] + " ");
		}

		quickSort(list);

		System.out.println("\nAfter quick sort:");
		for (int i = 0; i < list.length; i++) {
			System.out.print(list[i] + " ");
		}
	}

	private static void quickSort(int[] list) {
		if (list.length > 1) {
			quickSort(list, 0, list.length - 1);
		}
	}

	private static void quickSort(int[] list, int first, int last) {
		if (last > first) {
			int pivotIndex = partition(list, first, last);
			quickSort(list, first, pivotIndex - 1);
			quickSort(list, pivotIndex + 1, last);
		}
	}

	private static int partition(int[] list, int first, int last) {
		// 将第一个元素作为主元（pivot）
		int pivot = list[first];

		// 向前遍历的索引，开始时指向第二个元素
		int low = first + 1;

		// 向后遍历的索引,开始时指向数组最后一个元素
		int high = last;

		while (high > low) {
			// 从左开始向前遍历
			while (low <= high && list[low] <= pivot)
				low++;
			// 从右开始向后遍历
			while (low <= high && list[high] > pivot)
				high--;

			// 交换数组中的两个元素
			if (high > low) {
				int temp = list[high];
				list[high] = list[low];
				list[low] = temp;
			}
		}

		while (high > first && list[high] >= pivot)
			high--;

		// 和list[high]交换主元
		if (pivot > list[high]) {
			list[first] = list[high];
			list[high] = pivot;
			return high;
		} else
			return first;
	}
}
