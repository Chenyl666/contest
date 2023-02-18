package com.contest.util;

public class RandomGenerator {

    public static String generateRandomEmailCode(){
        return Integer.toString((int)((Math.random() * 9 + 1) * 1000));
    }

}
