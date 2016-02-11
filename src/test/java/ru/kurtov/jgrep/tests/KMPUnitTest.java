package ru.kurtov.jgrep.tests;

import org.junit.Assert;
import org.junit.Test;
import ru.kurtov.jgrep.searcher.Kmp;

public class KMPUnitTest {

    
    @Test
    public void prefix() {
        String str1 = "abcdabscabcdabia";
        String str2 = "abcdabcabcdabcdab";
        String str3 = "aaaaa";
        
                
        int[] actual1 = Kmp.prefix(str1);
        int[] actual2 = Kmp.prefix(str2);
        int[] actual3 = Kmp.prefix(str3);

        
        int[] expected1 = new int[] {0, 0, 0, 0, 1, 2, 0, 0, 1, 2, 3, 4, 5, 6, 0, 1};
        int[] expected2 = new int[] {0, 0, 0, 0, 1, 2, 3, 1, 2, 3, 4, 5, 6, 7, 4, 5, 6};
        int[] expected3 = new int[] {0, 1, 2, 3, 4};
        
        Assert.assertArrayEquals(expected1, actual1);
        Assert.assertArrayEquals(expected2, actual2);
        Assert.assertArrayEquals(expected3, actual3);
    }
    
    @Test
    public void indexesOf() {
        Assert.assertArrayEquals(new int[] {2}, Kmp.indexesOf("ada", "abadaba"));
        Assert.assertArrayEquals(new int[] {}, Kmp.indexesOf("123", "abadaba"));
    }

    @Test
    public void contain() {
        Assert.assertTrue(Kmp.contain("ada", "abadaba", Kmp.prefix("ada")));
        Assert.assertFalse(Kmp.contain("123", "abadaba", Kmp.prefix("123")));
    }
}