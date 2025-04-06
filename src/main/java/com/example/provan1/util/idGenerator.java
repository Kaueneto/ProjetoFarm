package com.example.provan1.util;

public class idGenerator {

    private static long counter = 1;

    public static long generateID(){
        return counter++;
    }
}
