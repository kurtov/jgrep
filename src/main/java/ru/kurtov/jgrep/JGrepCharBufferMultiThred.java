package ru.kurtov.jgrep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Arrays;
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
                //System.out.print(new String(array));
                queue.put(array);
            } catch (InterruptedException ex) {
                System.out.println("бага");
            }
            
            chbuff.clear();
        }
        rd.close();
        
        
        try {
            queue.put(TERMINATOR);
        } catch (InterruptedException ex) {
            System.out.println("бага");
        }
        //System.out.print("<end>\n");
/*
        searcher.search(chbuff.array());
        printResult(f, searcher.terminate());
        searcher.reset();
 */
    }
    
    private void printQueue() throws InterruptedException {
        char[] array;
        System.out.println("printQueue");
        while((array = queue.take()) != null) {
            System.out.println("res");
            
            System.out.println(Arrays.toString(array));
        }
    }
    
    public class Consumer implements Runnable{

        protected BlockingQueue<char[]> queue = null;
        protected File file = null;

        public Consumer(BlockingQueue<char[]> queue, File file) {
            this.queue = queue;
            this.file = file;
        }

        public void run() {
            char[] array;
            
            try {
                array = queue.take();
                while(array != TERMINATOR) {
                    //System.out.print(new String(array));
                    searcher.search(array);
                    array = queue.take();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printResult(file, searcher.terminate());
            searcher.reset();
        }
    }
}
