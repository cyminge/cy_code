package com.cy.aa;

public class TestSingleTon {
    
    private TestSingleTon() {
        System.out.println("invoke");
    }
    
    private void printt() {
        System.out.println("haha");
    }
    
    public static void main(String[] args) {
        TestSingleTon ton = Holer.testSingleton;
        ton.printt();
        ton = null;
        if(ton == null) {
            ton = Holer.testSingleton;
        }
        ton.printt();
//       
    }

    private static final class Holer {
        private static final TestSingleTon testSingleton = new TestSingleTon();
    }
    
}
