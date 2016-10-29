package com.mogoo.importance.ok;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * 随机产生20个字符并且排序
 * 思路：用无序不重复的Set存储元素，然后通过TreeSet进行排序--ok
 * @author wkl
 * 
 */
public class RadomAndSort {
	
	/**
	 * 随机产生20个字符串并且字符串不能重复 且进行排序
	 * 
	 * @param random
	 * @param len
	 * @return
	 */
	public Set<Character> getChar() {
		
		int counts = 0;
		
		Set<Character> numberSet01 = new HashSet<Character>();
		Random rdm = new Random();
		char ch;
		while (numberSet01.size() < 20) {
//			int rdGet = Math.abs(rdm.nextInt()) % 26 + 97;// 产生97到122的随机数a-z值
			int rdGet=(rdm.nextInt(26) + 97);
			ch = (char) rdGet;
			numberSet01.add(ch);
			// Set中是不能放进重复的值的，当它有20个时，就满足你的条件了
			
			counts ++;
		}
		System.out.println("counts="+counts);
		return numberSet01;
	}

	public static void main(String[] args) {
		RadomAndSort rd = new RadomAndSort();
		Set<Character> numberSet01 = rd.getChar();

		Set<Character> numberSet = new TreeSet<Character>();
		numberSet.addAll(numberSet01);
		for (Iterator<Character> it = numberSet01.iterator(); it.hasNext();) {
			System.out.print(it.next());
		}
		System.out.println();
		for (Iterator<Character> it = numberSet.iterator(); it.hasNext();) {
			System.out.print(it.next());
		}

	}
}
