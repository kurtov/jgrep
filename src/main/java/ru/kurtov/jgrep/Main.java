package ru.kurtov.jgrep;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) { 
        int length = args.length;
        
        if(length < 2) {
            System.err.println("Неверные параметры. jgrep SUBSTR FILE[...]");
            return;
        }

        JGrep grep = new JGrepCharBufferMultiThred(args[0]);

        for (int i = 1; i < length; i++) {
            File f = new File(args[i]);
            try {
                grep.find(f);
            } catch (IOException x) {
                System.err.println(f + ": " + x);
            }
        }
    }
}