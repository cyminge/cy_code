package com.mogoo;

public class StringConvert {

	public static void main(String[] args) {
		String str1 = "ad bf 123";
//		String[] str2 = str1.split("\\s");
//		for(int i=str2.length-1;i>=0;i--){
//			System.out.print(str2[i]+" ");
//			for(int j=0;j<str2[i].length();j++){
//				String str3 = String.valueOf(str1.charAt(j));
//				System.out.print(str3);
//			}
			
//		}
		
		for(int i=str1.length()-1;i>=0;i--){
			String str2 = String.valueOf(str1.charAt(i));
			System.out.print(str2);
		}
		
		
		
	}
	
}
