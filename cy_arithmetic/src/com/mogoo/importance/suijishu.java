package com.mogoo.importance;

import java.util.Arrays;
import java.util.Random;

public class suijishu {

	static int counts = 0;

	public static void main(String[] args) {

		for (int k = 0; k < 10000; k++) {
			// 要排序的一组数字
			int[] seed = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			int len = seed.length;
			int[] result = new int[len];
			Random random = new Random();

			for (int i = 0; i < len; i++) {
				
//				System.out.println();
				// 得到一个位置
				int r = random.nextInt(len-i);
				// 得到那个位置的数值
				result[i] = seed[r];
//				System.out.println("seed[r] = "+seed[r]);
				// 将最后一个未用的数字放到这里
				seed[r] = seed[len - 1 - i];
//				System.out.println("seed[r]-- = "+seed[r]);
				
//				System.out.println("seed[len - 1 - i] = "+seed[len - 1 - i]);
			}
			 System.out.println("result:" + Arrays.toString(result));

			 
			 int[] n = new int[10];
			 
			 for (int i = 0; i < 10; i++) {
					int r = random.nextInt(len-i);
					n[i] = seed[r];
					seed[r] = seed[len - 1 - i];
				}
			 
			for (int j = 0; j < n.length-5; j++) {
				if (result[j] == 10) {
					System.out.println("is in !");
					counts++;
				}
			}
			 
		}
		System.out.println("counts = " + counts);
	}

}
