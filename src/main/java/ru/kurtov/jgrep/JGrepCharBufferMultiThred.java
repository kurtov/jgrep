package ru.kurtov.jgrep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class JGrepCharBufferMultiThred extends JGrep {
    private final BlockingQueue<char[]> queue;
    protected static final char[] TERMINATOR = new char[0];

    public JGrepCharBufferMultiThred(String pattern) {
        super(pattern);
        queue = new LinkedBlockingQueue();
    }

    public JGrepCharBufferMultiThred(String pattern, int SearcherType) {
        super(pattern, SearcherType);
        queue = new LinkedBlockingQueue();
    }
    
    @Override
    public void find(File f) throws IOException {
        Reader rd = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        CharBuffer chbuff = CharBuffer.allocate(4056);
        char[] array;
        int length;
        
        new Thread(new Consumer(queue, f)).start();
        
        
        
        while((length = rd.read(chbuff)) > 0){
            chbuff.flip();
            
            array = new char[length];
            int i = 0;
            while(chbuff.hasRemaining()){
                char ch = chbuff.get();
                array[i++] = ch;
            }
            
            
            try {
                queue.put(array);
            } catch (InterruptedException e) {
                System.err.println(f + ": " + e);
            }
            
            chbuff.clear();
        }
        rd.close();
        
        
        try {
            queue.put(TERMINATOR);
        } catch (InterruptedException e) {
            System.err.println(f + ": " + e);
        }
    }
    
    public class Consumer implements Runnable{

        protected BlockingQueue<char[]> queue = null;
        protected File file = null;

        public Consumer(BlockingQueue<char[]> queue, File file) {
            this.queue = queue;
            this.file = file;
        }

        @Override
        public void run() {
            char[] array;
            
            try {
                array = queue.take();
                while(array != TERMINATOR) {
                    searcher.search(array);
                    array = queue.take();
                }
            } catch (InterruptedException e) {
                System.err.println(file + ": " + e);
            }
            printResult(file, searcher.terminate());
            searcher.reset();
        }
    }
}
