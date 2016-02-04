/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kurtov.jgrep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import static ru.kurtov.jgrep.Main.find;
import static ru.kurtov.jgrep.Main.printResult;

public abstract class JGrep {
    protected String pattern;
    protected Searcher searcher;
    
    //Подавлять вывод найденных строк
    //Установить в true при тестировании
    protected boolean suppressOutput = false;

    public void setSuppressOutput(boolean suppressOutput) {
        this.suppressOutput = suppressOutput;
    }
    
    public final static int KMP_SEARCHER = 0;
    public final static int SIMPLE_SEARCHER = 1;
    
    public static void main(String[] args) { 
        System.err.println("Понеслась2");
        /*
        if(length < 2) {
            System.err.println("Неверные параметры. jgrep SUBSTR FILE[...]");
            return;
        }
        */
        args = new String[] {"Наполеон", "/Users/mike/NetBeansProjects/jgrep/resources/WarAndPeace.txt"};
        JGrep grep = new JGrepCharBufferMultiThred(args[0]);
         
        int length = args.length;
        for (int i = 1; i < length; i++) {
	    File f = new File(args[i]);
	    try {
		grep.find(f);
	    } catch (IOException x) {
		System.err.println(f + ": " + x);
	    }
	}
        
    }
    
    public JGrep(String pattern) {
        this.pattern = pattern;
        this.searcher = new KmpSearcher(pattern);
    }
    
    public JGrep(String pattern, int SearcherType) {
        this.pattern = pattern;
        
        switch(SearcherType) {
            case KMP_SEARCHER: 
                this.searcher = new KmpSearcher(pattern);
                break;
            case SIMPLE_SEARCHER:
                this.searcher = new SimpleSearcher(pattern);
                break;        
        }
    }    
    
    public abstract void find(File file) throws IOException;
    
    protected void printResult(File f, ArrayList<String> result) {
        if(suppressOutput) {
            return;
        }
        
        for (String string : result) {
            System.out.println(f + ": " + string);
        }
    }
}