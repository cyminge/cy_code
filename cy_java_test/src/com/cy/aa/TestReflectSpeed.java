package com.cy.aa;

import java.lang.reflect.Constructor;

public class TestReflectSpeed {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Fooo f = null;
//        int count = 10000000;
        int count = 10000000;
        long newBegin = System.nanoTime();
        for (int i = 0; i < count; i++) {
            f = new Fooo("aa");
        }
        System.out.println("speed time:" + (System.nanoTime() - newBegin));

        Class<?> fc = null;
        long forNameBegin = System.nanoTime();
        for (int i = 0; i < count; i++) {
            fc = Class.forName("com.cy.aa.Fooo");
        }
        System.out.println("speed time:" + (System.nanoTime() - forNameBegin));
        
        long newBeginParam = System.nanoTime();
        Class<?>[] parameterTypes={String.class};   
        Constructor<?> constructor = null;
        try {
            constructor = fc.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }  
        System.out.println("speed time:" + (System.nanoTime() - newBeginParam));

        long newInstanceBegin = System.nanoTime();
        for (int i = 0; i < count; i++) {
            f = (Fooo) constructor.newInstance("aa");
        }
        System.out.println("speed time:" + (System.nanoTime() - newInstanceBegin));
    }
    
}

class Fooo {
    
    public Fooo(String aa) {
        
    }
    
    public void bar() throws Exception {
        // do nothing
    }
}

