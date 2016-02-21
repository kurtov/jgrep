package ru.kurtov.jgrep;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException { 
        int length = args.length;
        
        if(length < 2) {
            System.err.println("Неверные параметры. jgrep SUBSTR FILE[...]");
            return;
        }

        JGrep grep = new JGrepCharBufferMultiThred(args[0]);

        for (int i = 1; i < length; i++) {
            grep.addFileName(args[i]);
        }
        grep.find();
    }
}