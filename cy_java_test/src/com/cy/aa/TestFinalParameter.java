package com.cy.aa;

/**
 * 测试传入的对象用final修饰
 * result: final修饰传入的对象，可以改变其值
 * @author JLB6088
 *
 */
public class TestFinalParameter {

	public static void main(String[] args) {
		CY cy = new CY();
		cy.name = "cy";
		
		change(cy);
		
		System.out.println(cy.name);
	}
	
	public static void change(final CY cy) {
		cy.name = "minge";
	}
	
	public static class CY {
		public String name;
		public int ege;
		
	}
	
}
