package ru.kurtov.jgrep;

import java.util.Arrays;

//https://ru.wikibooks.org/wiki/Реализации_алгоритмов/Алгоритм_Кнута_—_Морриса_—_Пратта
public class Kmp {
    public static int[] prefix(String str) {
        return prefix(str.toCharArray());
    }
    
    public static int[] prefix(char[] str) {
        int length = str.length;
        int[] v = new int[length];
        java.util.Arrays.fill(v, 0);
        
        for(int i = 1; i < length; i++) {
            int k = v[i-1];
            while(k>0 && str[i] != str[k])
                k = v[k-1];
            
            if (str[k] == str[i]) {
                k++;
            }
            v[i] = k;
        }
        
        return v;
    }
    
    public static int[] indexesOf(String pattern, String text) {
        return indexesOf(pattern.toCharArray(), text.toCharArray());
    }

    public static int[] indexesOf(char[] pattern, char[] text) {
        int[] pf = prefix(pattern);
        int[] indexes = new int[text.length];
        int size = 0;
        int k = 0;
        for(int i = 0; i < text.length; ++i) {
            while(pattern[k] != text[i] && k > 0) {
                k = pf[k - 1];
            }
            if(pattern[k] == text[i]) {
                k = k + 1;
                if(k == pattern.length) {
                    indexes[size] = i + 1 - k;
                    size += 1;
                    k = pf[k - 1];
                }
            } else {
                k = 0;
            }
        }
        return Arrays.copyOfRange(indexes, 0, size);
    }
    
    public static boolean contain(String pattern, String text, int[] pf) {
        return contain(pattern.toCharArray(), text.toCharArray(), pf);
    }
    
    public static boolean contain(char[] pattern, char[] text, int[] pf) {
        int k = 0;
        for(int i = 0; i < text.length; ++i) {
            while(pattern[k] != text[i] && k > 0) {
                k = pf[k - 1];
            }
            if(pattern[k] == text[i]) {
                k = k + 1;
                if(k == pattern.length) {
                    return true;
                }
            } else {
                k = 0;
            }
        }
        return false;
    }
}