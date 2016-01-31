/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kurtov.jgrep;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author mike
 */
public class SimpleSearcher implements Searcher {

    private final String pattern;
    private String lastStr = null;
    ArrayList<String> lines = new ArrayList<>();
    
    public SimpleSearcher(String pattern) {
        this.pattern = pattern;
    }
    
    
    @Override
    public void search(char[] buffer) {
        int length = buffer.length;
        int start = 0;
        String str;
        
        
        for(int i=0; i<length; i++) {
            if(buffer[i] == '\n') {
                str = buildStr(buffer, start, i);
                start = i + 1;
                
                if(str.contains(this.pattern)) {
                    lines.add(str);
                }
            }
        }
        
        //Если последний разделитеь не является последним символом буфера
        //то запомнить неполную последовательность
        if(start < length) {
            lastStr = buildStr(buffer, start, length);
        } else {
            lastStr = null;
        }
    }

    @Override
    public ArrayList<String> terminate() {
        search(new char[]{'\n'});
        return lines;
    }

    @Override
    public void reset() {
        lastStr = null;
        lines = new ArrayList<>();
    }
    
    private String buildStr(char[] buffer, int start, int end) {
        String str = new String(Arrays.copyOfRange(buffer, start, end));
                
        if(lastStr != null) {
            str = lastStr + str;
            lastStr = null;
        }
        
        return str;
    }
}
