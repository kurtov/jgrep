package ru.kurtov.jgrep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;


public class JGrepCharBuffer extends JGrep {

    public JGrepCharBuffer(String pattern) {
        super(pattern);
    }

    public JGrepCharBuffer(String pattern, int SearcherType) {
        super(pattern, SearcherType);
    }
    
    @Override
    public void find() throws IOException {
        for(String fileName : this.fileNames) {
            File f = new File(fileName);    
        
            Reader rd = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            CharBuffer chbuff = CharBuffer.allocate(1024);
            char[] array;
            int length;

            while((length = rd.read(chbuff)) > 0){
                chbuff.flip();
                array = new char[length];
                int i = 0;
                while(chbuff.hasRemaining()){
                    char ch = chbuff.get();
                    array[i++] = ch;
                }
                searcher.search(array);

                chbuff.clear();
            }
            rd.close();

            printResult(f, searcher.terminate());
            searcher.reset();
        }
    }
}
