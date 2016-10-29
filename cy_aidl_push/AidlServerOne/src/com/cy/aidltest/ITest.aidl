 package com.cy.aidltest;

import com.cy.aidltest.IPersonCallback;

 interface ITest 
 {

    void setAge(in int age, out String[] str);
	void setName(String name);
	String display();
	
	void testCallback(IPersonCallback callback);

 } 