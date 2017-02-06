package com.cy;

/**
 * 测试强制转换
 * @author JLB6088
 *
 */
public class TestCast {

	public static void main(String[] args) {
//		Parent p = new Son();
//		System.out.println(p instanceof Son);
//		System.out.println((Son)p != null);
//		
//		Parent pp = new Parent();
//		
//		Son ss = new Son();
//		
//		System.out.println(ss instanceof Parent);
//		System.out.println(pp instanceof Son);
//		
//		System.out.println(pp = ss);
		
		Parent parent = new Parent();
		Son son = (Son) parent;
		
		
		Son ss = new Son();
		Parent pp = (Parent)ss;
	}
	
	
	static class Parent {
		String name;
	}
	
	static class Son extends Parent {
		String name;
		String age;
	}
}
