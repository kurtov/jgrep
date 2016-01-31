/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kurtov.jgrep;

/**
 *
 * @author mike
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;

public class CharBufferTest {
	public static void main(String[] args) {
		try {
			Reader rd = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/mike/NetBeansProjects/jgrep/resources/shortTest.txt")));
		    CharBuffer chbuff = CharBuffer.allocate(8);
		    while(rd.read(chbuff) > 0){
		    	chbuff.flip();
		        while(chbuff.hasRemaining()){
		        	char ch =  chbuff.get();
		        	System.out.print(ch);
		        }
		        chbuff.clear();
		    }
		    rd.close();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}    
