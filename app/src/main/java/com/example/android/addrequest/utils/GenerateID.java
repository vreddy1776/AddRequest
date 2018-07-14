package com.example.android.addrequest.utils;


import java.util.Random;

public class GenerateID {

    public static int newID(){

        final int min = 100000000;
        final int max = 200000000;

        Random random = new Random();
        final int ID = random.nextInt((max - min) + 1) + min;

        return ID;

    }

}
