package com.mogoo;

public class NineNineMulitTable {

	public static void main(String[] args) {
		nineNineMulitTable();
	}

	/**
	 * 打印九九乘法口诀表
	 */
	public static void nineNineMulitTable() {
//		for (int i = 1, j = 1; j <= 9; i++) {
//			System.out.print(i + "*" + j + "=" + i * j + " ");
//			if (i == j) {
//				i = 0;
//				j++;
//				System.out.println();
//			}
//		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		for(int i=1, j=1; j<=9;i++) {
			System.out.print(i+"*"+j+"="+i*j+",");
			if(i == j) {
				i = 0;
				j++;
				System.out.println();
			}
		}
		
	}
}
