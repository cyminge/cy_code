package com.mogoo;

/**
 * 求坐标点
 * @author work
 *
 */
public class Coordinate {

	public static void main(String[] args){
		int r1, r2;
		int x, y;
		r1 = 80;r2=215;
		y = (int) ((int)(Math.sqrt(r2*r2 -r1*r1)+r1)/(Math.sqrt(2)));
		x = y - (int)(Math.sqrt(2)*r1);
		System.out.println("y = "+y);
		System.out.println("x = "+x);
		
		// 弧度=角度*Math.PI/180  角度=弧度*180/Math.PI 
		int angle1 = (int) (Math.asin((double)y/r2)*180/Math.PI);
		System.out.println("angle = "+angle1);
		int angle2 = (int) (Math.asin((double)x/r2)*180/Math.PI);
		System.out.println("angle = "+angle2);
		
		double xx = Math.toRadians(45);
		double yy = 45*Math.PI/180;
//		int arc = 30*Math.PI/180;
		System.out.println("xx = "+Math.sin(xx));
		System.out.println("yy = "+Math.sin(yy));
//		System.out.println("y/r2 = "+(double)y/r2);
//		System.out.println("Math.asin((double)y/r2)*180/Math.PI = "+Math.asin((double)y/r2)*180/Math.PI);
		
		int originX = 650, originY = 300;
		int imageX = 150, imageY = 76;
		
		int lastX1 = originX-x-imageX/2;
		int lastY1 = originY-y-imageY/2;
		
		int lastX2 = originX+x-imageX/2;
		int lastY2 = originY-y-imageY/2;
		
//		System.out.println("lastX1 = "+lastX1+",lastY1 = "+lastY1);
//		System.out.println("lastX2 = "+lastX2+",lastY2 = "+lastY2);
//		System.out.println("lastX3 = "+lastX3+",lastY3 = "+lastY3);
//		System.out.println("lastX4 = "+lastX4+",lastY4 = "+lastY4);
//		System.out.println("lastX5 = "+lastX5+",lastY5 = "+lastY5);
//		System.out.println("lastX6 = "+lastX6+",lastY6 = "+lastY6);
		
	}
	
}
