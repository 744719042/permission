package com.example.permisswion.fx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {
    public static void executeGrantedMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PermissionGranted permissionGranted = method.getAnnotation(PermissionGranted.class);
            if (permissionGranted != null && permissionGranted.requestCode() == requestCode) {
                method.setAccessible(true);
                try {
                    method.invoke(object, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    public static void executeDeniedMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PermissionDenied permissionDenied = method.getAnnotation(PermissionDenied.class);
            if (permissionDenied != null && permissionDenied.requestCode() == requestCode) {
                method.setAccessible(true);
                try {
                    method.invoke(object, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}
