/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kurtov.jgrep;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author mike
 */
public class Main {
    
    
    public static void main(String[] args) {
        int length;
        String substr = null;
        Path[] paths = null;
        
        if(args.length < 2) {
            System.out.println("Неверные параметры. jgrep SUBSTR FILE[...]");
            return;
        }
        
        substr = args[0];
        

        length = args.length;
        for(int i = 1; i < length; i++) {
            find(substr, args[i]);
        }
    }
    
    static void find(String substr, String path) {
        int count;
        Searcher searcher = new KmpSearcher(substr);
        ArrayList<String> result;
        
        // Здесь канал открывается по пути, возвращаемому
        // методом Paths.get() в виде объекта типа Path.
        // Переменная filepath больше не нужна
        try (SeekableByteChannel fChan = Files.newByteChannel(Paths.get(path))) {

            // выделить память под буфер
            ByteBuffer mBuf = ByteBuffer.allocate(8);
            //CharBuffer cbuf = Charset.forName("ISO-8859-1").decode(mBuf);
            
            do {
                // читать данные из файла в буфер
                count = fChan.read(mBuf);
                //System.out.println("count " + count);

                // прекратить чтение по достижении конца файла
                if(count != -1) {
                    // подготовить буфер к чтению из него данных
                    mBuf.rewind();

                    //System.out.println(mBuf.asCharBuffer());
                    //Charset chars = Charset.forName("ISO-8859-1");
                    //CharBuffer cbuf = chars.decode(mBuf);
                    //CharBuffer cbuf = Charset.forName("ISO-8859-1").decode(mBuf);
                    //System.out.println(cbuf);
                    //System.out.println(Arrays.toString(cbuf.array()));
                    //System.out.println(Arrays.toString(mBuf.array()));
                    //mBuf.rewind();

                    String encoding = "UTF-8";//"ISO-8859-1"; //"cp1251"; //System.getProperty("file.encoding");
                    CharBuffer cbuf = Charset.forName(encoding).decode(mBuf);
                    mBuf.flip();
  
                    System.out.println(cbuf);
                    searcher.search(cbuf.array());

                    //searcher.search(mBuf.array());
                    
                    // читать байты данных из буфера и
                    // выводить их на экран как символы
                    //for(int i=0; i < count; i++) {
                        //System.out.println(mBuf);
                        //searcher.search(mBuf.asCharBuffer().array());
                        //System.out.print((char)mBuf.get());
                        //System.out.print("["+String.valueOf(count)+"]");
                    //}
                }
            } while(count != -1);

            result = searcher.terminate();
            //result = searcher.getLines();
            searcher.reset();
            printResult(path, result);
            
            
            System.out.println();
        } catch(InvalidPathException e) {
            System.out.println("Ошибка указания пути " + e); 
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода " + e); 
        }   
    }
    
    static void printResult(String path, ArrayList<String> result) {
        for (String string : result) {
            System.out.println(path + ": " + string);
        }
    }
}