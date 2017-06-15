package com.cy.aa;

import java.lang.reflect.Field;

public class TestReflectGetConstant {
    
    public static void main(String[] args) {
        try {
            Field field = PackageManager.class.getDeclaredField("GET_ENCRYPTAPPS");
            System.out.println(">>>=== " + Integer.parseInt(field.get(PackageManager.class).toString())); 
        } catch (NumberFormatException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        }
    }
    
}
 class PackageManager {  
    public static final String teacher = "json";  
    public static final int GET_ENCRYPTAPPS = 0x00000002;
    public static final int TESTfds_INT = 123456;
    private String id;  
    private String name;  
}

