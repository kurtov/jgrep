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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import static ru.kurtov.jgrep.Main.find;
import static ru.kurtov.jgrep.Main.printResult;

/**
 *
 * @author mike
 */
public class Main2 {

    static String pattern;
    
    public static void main(String[] args) {
        int length = args.length;
        
        
        if(length < 2) {
            System.err.println("Неверные параметры. jgrep SUBSTR FILE[...]");
            return;
        }
        
        pattern = args[0];
 
        for (int i = 1; i < length; i++) {
	    File f = new File(args[i]);
	    try {
		find(f);
	    } catch (IOException x) {
		System.err.println(f + ": " + x);
	    }
	}
    }
    
    
    static void find(File f) throws FileNotFoundException, IOException {
        Reader rd = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        CharBuffer chbuff = CharBuffer.allocate(16);
        Searcher searcher = new KmpSearcher(pattern);

        while(rd.read(chbuff) > 0){
//	    chbuff.flip();	    
//            System.out.println(Arrays.toString(chbuff.array()));
            searcher.search(chbuff.array());
            chbuff.clear();
        }
        rd.close();
        
        printResult(f, searcher.terminate());
        searcher.reset();
    }
    
    static void printResult(File f, ArrayList<String> result) {
        for (String string : result) {
            System.out.println(f + ": " + string);
        }
    }
}