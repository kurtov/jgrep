package ru.kurtov.jgrep.tests;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import ru.kurtov.jgrep.searcher.KmpSearcher;
import ru.kurtov.jgrep.searcher.Searcher;
import ru.kurtov.jgrep.searcher.SimpleSearcher;

public class SearcherUnitTest {
    String[] buffer1 = new String[] {
       "Строка1 123\nСтрока2\nСтр123ока3123\n",
       "Очень очень оченьочень длинная строка",
       "Которая разбилась    на 3 буффера, 12",
       "и кончилась здeсь\n А это другая   12",
       "3 длинная строка"
   };

   String[] buffer2 = new String[] {
       "кор",
       "от",
       "кий",
       "1",
       "2",
       "3",
       "буфер\n",
       "\n\n\n",
       "123123123\n"    
   };

   String[] expected1 = new String[] {
       "Строка1 123",
       "Стр123ока3123",
       " А это другая   123 длинная строка"
   };

   String[] expected2 = new String[] {
       "короткий123буфер",
       "123123123"
   };

    
    @Test
    public void simpleSearcher() {
        searcher(new SimpleSearcher("123"));
    }

    @Test
    public void kmpSearcher() {
        searcher(new KmpSearcher("123"));
    }
    
    private void searcher(Searcher searcher) {
        String[] actual1 = searchHelper(searcher, buffer1);
        String[] actual2 = searchHelper(searcher, buffer2);
        
                
        Assert.assertArrayEquals(expected1, actual1);
        Assert.assertArrayEquals(expected2, actual2);        
    }
    
    private String[] searchHelper(Searcher searcher, String[]buffer) {
        int length = buffer.length;
        ArrayList<String> actual;
        
        for(int i=0; i<length; i++) {
            searcher.search(buffer[i].toCharArray());
        }
        actual = searcher.terminate();
        searcher.reset();
        
        return actual.toArray(new String[actual.size()]);
    }
}