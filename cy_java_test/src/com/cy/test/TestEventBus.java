package com.cy.test;

import de.greenrobot.event.EventBus;

public class TestEventBus {
    
    
    public static void main(String[] args) {
        
        TestReceive testReceive = new TestReceive();
        testReceive.init();
        
        TestObject mTestObject = new TestObject();
        
        
        EventBus.getDefault().post(mTestObject);
        EventBus.getDefault().post(mTestObject);
        EventBus.getDefault().post(mTestObject);
        EventBus.getDefault().post(mTestObject);
    }
}

class TestObject {
    
}

class TestReceive {
    public void init() {
        EventBus.getDefault().register(this, TestObject.class);
    }
    
    public void destroy() {
        EventBus.getDefault().unregister(this, TestObject.class);
    }
    
    public void onEvent(TestObject msg) {
        System.out.println("``````````````");
    }
}

