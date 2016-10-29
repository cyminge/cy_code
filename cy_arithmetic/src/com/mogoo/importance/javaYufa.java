package com.mogoo.importance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class javaYufa {
	
	private static int x=100;
	public static void main(String[] args){
		
//		System.out.println();
		
//		javaYufa yufa1 = new javaYufa();
//		yufa1.x++;
//		javaYufa yufa2 = new javaYufa();
//		yufa2.x++;
//		yufa1 = new javaYufa();
//		yufa1.x++;
//		javaYufa.x--;
//		x++;
//		System.out.println("x = "+x);
		
		int s = test();
		
		List list = test2();
		
		if(list != null){
			System.out.println(list.get(0));
		}
		

//		System.out.println("s = "+s);
		
//		String s1 = "0.5";
//		String s2 = "12";
//		double x = Double.parseDouble(s1);
//		int y = Integer.parseInt(s2);
//		System.out.println(x+y);
		
//		double x = 1234.1234567;
//		DecimalFormat f = new DecimalFormat("000");
//		System.out.println("pi = "+f.format(x));
	}
	
//	public int localVar = 0;
//	public static int staticVar = 0;
//	
//	public static void TestOne(){
//		TestTwo();
//		localVar = localVar*2;
//	}
//	
//	public void TestTwo(){
//		staticVar = staticVar +1;
//	}
	
	static int test(){
		int x = 1;
		try{
			return x++;
		}
		
		finally{
			x = 3;
		}
	}
	
	@SuppressWarnings("rawtypes")
	static List test2(){
		System.out.println("测试finally修改对象类型");
	    List li = new ArrayList();
	    try {
	      return li;
	    } finally {
	      li.add("java2000.net");
	      li = null; // 注意，此处的li并不影响返回的li,他们是2个不同的引用
	      return li;
	    }
	}
//	
//	class AA {
//		public int i = 0;
//	}
	
}
