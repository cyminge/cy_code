package com.mogoo;

/**
 * 对字母进行累加
 * 
 * @author wkl
 * 
 */
public class LetterAccumulate {
	
	int accumutate(int n) {
		if (n == 'a') {
			return 'a';
		}
		return n + accumutate(n - 1);
	}

	public static void main(String args[]) {
		System.out.println(new LetterAccumulate().accumutate('c'));//
		System.out.println('x' + 'y' + "..........");
	}
}
