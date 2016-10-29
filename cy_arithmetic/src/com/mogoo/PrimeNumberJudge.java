package com.mogoo;

import java.util.Scanner;

/**
 * 判断一个数是否为质数
 * 
 * 思路：一个数从2开始取余，如果为0则不是质数
 * @author wkl
 *
 */
public class PrimeNumberJudge {
	public static void main(String[] args) {
		System.out.println("======》打印1～n间的所有质数《======");
		System.out.print("请输入一个数字(整数)：");
		Scanner sc = new Scanner(System.in);// 获取文本扫描器
		int x = sc.nextInt();// 使输入整数保存在x
		int[] zhishu = new int[5];
		int n = 1;
		System.out.println("1--" + x + "的质数如下：");
		for (int i = 1; i <= x; i++) {
			// 调用isZhiShu（int number）方法，返回true，进行打印操作。
			if (isZhiShu(i)) {
				if (n % 5 == 0) {
					System.out.println("  ");
					System.out.println(i + "\t");
					n++;
				} else {
					System.out.print(i + "\t");
					n++;
				}
			}
		}
		
//		boolean flag = isZhiShu(8);
//		System.out.println(flag);
	}

	public static boolean isZhiShu(int number) {// 判断是否是质数的方法
		if (number == 1) {
			return false;
		}
		int i = 2;
		for (i = 2; i < number; i++) {
			if (number % i == 0) {
				return false;
			}
		}
//		System.out.println(i);
		return true;
	}
}
