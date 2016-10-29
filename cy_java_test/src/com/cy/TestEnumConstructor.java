package com.cy;

public enum TestEnumConstructor {

	Instance();
	
	private TestEnumConstructor() {
		System.out.println("111");
	}
	
	private TestEnumConstructor(String txt) {
		System.out.println("222");
	}

}
