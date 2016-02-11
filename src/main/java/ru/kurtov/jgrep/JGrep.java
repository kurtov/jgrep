package ru.kurtov.jgrep;

import ru.kurtov.jgrep.searcher.KmpSearcher;
import ru.kurtov.jgrep.searcher.Searcher;
import ru.kurtov.jgrep.searcher.SimpleSearcher;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

abstract public class JGrep {
    protected String pattern;
    protected Searcher searcher;
    
    //Подавлять вывод найденных строк
    //Установить в true при тестировании
    protected boolean suppressOutput = false;
    
    //Реализация двух алгоритмов поиска подстроки в строке
    public final static int KMP_SEARCHER = 0;
    public final static int SIMPLE_SEARCHER = 1;
   
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

    public void setSuppressOutput(boolean suppressOutput) {
        this.suppressOutput = suppressOutput;
    }
    
    protected void printResult(File f, ArrayList<String> result) {
        if(suppressOutput) {
            return;
        }
        
        for (String string : result) {
            System.out.println(f + ": " + string);
        }
    }
}