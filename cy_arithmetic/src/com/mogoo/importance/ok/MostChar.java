package com.mogoo.importance.ok;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 求一段字符串中最长的字符--ok
 * 
 * @author cy
 * 
 */
public class MostChar {
	public static void main(String[] args) {
		String s = "aaedaebdbaeedabebedeaeba";
		Map<Character, Integer> map = new HashMap<Character, Integer>();

		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);
			if (!map.containsKey(c)) {
				map.put(c, 1);
			} else {
				map.put(c, map.get(c) + 1);
			}
		}

		int max = 0;
		char cc = ' ';
		Iterator<Entry<Character, Integer>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Character, Integer> en = iter.next();
			int count = en.getValue();
			if (count > max) {
				max = count;
				cc = en.getKey();
			}
		}

		System.out.println(cc + ":" + max);
	}
}
