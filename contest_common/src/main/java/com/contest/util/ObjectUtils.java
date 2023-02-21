package com.contest.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ObjectUtils<T> {

    /**
     * 对o1的空字段进行填充，填充来源于o2，并返回o1的引用
     * */
    @SneakyThrows
    public  static <T> T supplementFields(Object o1, Object o2){
        System.out.println(o1);
        System.out.println(o2);
        if(o1 == null || o2 == null){
            throw new NullPointerException();
        }
        Class<?> clazz = o1.getClass();
        if(clazz != o2.getClass()){
            throw new ClassCastException();
        }
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            Object fieldValue2 = field.get(o2);
            if(field.get(o1) != null){
                field.set(o1,fieldValue2);
            }
        }
        return (T)o1;
    }

}
