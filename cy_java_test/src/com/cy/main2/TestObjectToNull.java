package com.cy.main2;

import java.util.ArrayList;

public class TestObjectToNull {

    public static void main(String[] args) {
        TestObject mTestObject = new TestObject("测试测试", 9527);
        TestObject second = mTestObject;
        mTestObject.str = "修改测试测试";
        mTestObject = null;
        System.out.println(second.toString());
        
        ArrayList<TestObject> list = new ArrayList<TestObject>();
        mTestObject = new TestObject("测试测试", 9527);
        list.add(mTestObject);
        mTestObject = new TestObject("测试测试", 9528);
        list.add(mTestObject);
        mTestObject = new TestObject("测试测试", 9529);
        list.add(mTestObject);
        
        ArrayList<TestObject> secondList = list;
        list.clear();
        for(TestObject obj : secondList) {
            System.out.println(obj.toString());
        }
    }
    
    static class TestObject {
        String str;
        int index;
        
        public TestObject(String s, int i) {
            str = s;
            index = i;
        }
        
        @Override
        public String toString() {
            return "str:"+str+", index:"+index;
        }
    }
    
}
