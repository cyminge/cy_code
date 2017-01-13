package com.cy;

/**
 * 测试强制转换
 * @author JLB6088
 *
 */
public class TestCast {

	public static void main(String[] args) {
		Parent p = new Son();
		System.out.println(p instanceof Son);
		System.out.println((Son)p != null);
		
		Parent pp = new Parent();
		
		Son ss = new Son();
		
		System.out.println(ss instanceof Parent);
		System.out.println(pp instanceof Son);
		
		System.out.println(pp = ss);
//		System.out.println(ss = pp);
	}
	
	
	static class Parent {
		String name;
	}
	
	static class Son extends Parent {
		String name;
		String age;
	}
}
