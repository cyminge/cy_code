package com.cy.test;

public class TestClassLoaderOrder {
    public static void main(String[] args) {
        new A();
        System.out.println("======================");
        new B();
        System.out.println("======================");
        new C();
    }
}

class A {
    
    public String testStr = "A";
    
    public A() {
        
        System.out.println("testStr:"+testStr);
        System.out.println("Constructor A");
    }

    {
        testStr = "b";
        System.out.println("Field A");
    }
    
    static {
        System.out.println("Static A");
    }
}

class B extends A {
    public B() {
        System.out.println("Constructor B");
    }

    {
        System.out.println("Field B");
    }
    static {
        System.out.println("Static B");
    }

}

class C extends B {
    public C() {
        System.out.println("Constructor C");
    }

    {
        System.out.println("Field C");
    }
    static {
        System.out.println("Static C");
    }

}