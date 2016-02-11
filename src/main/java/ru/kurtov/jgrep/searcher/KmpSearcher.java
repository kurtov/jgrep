package ru.kurtov.jgrep.searcher;

import ru.kurtov.jgrep.searcher.Searcher;
import java.util.ArrayList;
import java.util.Arrays;

public class KmpSearcher implements Searcher {
    private final int[] prefixFunction;
    private final char[] pattern;
    char[] lastStr = null;
    ArrayList<String> lines = new ArrayList<>();
    
    public KmpSearcher(String pattern) {
        this.pattern = pattern.toCharArray();
        this.prefixFunction = Kmp.prefix(this.pattern);
    }
    
    public KmpSearcher(char[] pattern) {
        this.pattern = pattern;
        this.prefixFunction = Kmp.prefix(this.pattern);
    }

    @Override
    public void search(char[] buffer) {
        int length = buffer.length;
        int start = 0;
        char[] array;
        
        for(int i=0; i<length; i++) {
            if(buffer[i] == '\n') {
                array = buildArray(buffer, start, i);
                start = i + 1;

                if(Kmp.contain(this.pattern, array, this.prefixFunction)) {
                    lines.add(new String(array));
                }
            }
        }
        
        //Если последний разделитеь не является последним символом буфера
        //то запомнить неполную последовательность
        if(start < length) {
            lastStr = buildArray(buffer, start, length);
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
    
    private char[] buildArray(char[] buffer, int start, int end) {
        char[] array = Arrays.copyOfRange(buffer, start, end);
                
        if(lastStr != null) {
            array = concat(lastStr, array);
            lastStr = null;
        }
        
        return array;
    }
    
    //http://stackoverflow.com/a/80503
    public static char[] concat(char[] a, char[] b) {
        int aLen = a.length;
        int bLen = b.length;
        
        char[] c= new char[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}
