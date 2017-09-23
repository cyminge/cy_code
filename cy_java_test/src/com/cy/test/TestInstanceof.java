package com.cy.test;

public class TestInstanceof {

    
    
    public static void main(String[] args) {
        Impl rt = new Impl();
        System.out.println(rt instanceof RunnableTest);
        System.out.println(rt instanceof Impl);
        
        Impl impl11 = new Impl();
        
        System.out.println(impl11 instanceof RunnableTest);
        System.out.println(impl11 instanceof Impl);
        System.out.println(impl11 instanceof Impl11);
    }
    
}

abstract class RunnableTest {
    String str;
}

class Impl extends RunnableTest {
    
}

class Impl11 extends Impl {
    
}
