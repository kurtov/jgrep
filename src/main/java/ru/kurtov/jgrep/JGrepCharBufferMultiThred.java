package ru.kurtov.jgrep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class JGrepCharBufferMultiThred extends JGrep {
    private final BlockingQueue<QueueItem> queue;
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
    public void find() throws IOException {
        new Thread(new Consumer(queue)).start();
        
        try{
            for(String fileName : this.fileNames) {
                File f = new File(fileName);
                
                if(!f.exists()) {
                    queue.put(QueueItem.getNotFoundItem(fileName));
                    continue;
                }
                
                try (Reader rd = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
                    CharBuffer chbuff = CharBuffer.allocate(BUFFER_SIZE);
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

                        queue.put(QueueItem.getDataItem(fileName, array));

                        chbuff.clear();
                    }
                    queue.put(QueueItem.getEOFItem(fileName));
                }
            }

            queue.put(QueueItem.getTerminatorItem());
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
    
    public class Consumer implements Runnable{
        
  
        protected BlockingQueue<QueueItem> queue;

        public Consumer(BlockingQueue<QueueItem> queue) {
            this.queue = queue;
        }

        @Override
        public void run(){
            try {
                QueueItem item = queue.take();
                while(!item.isTerminator()) {
                    if(item.isData()) {
                        searcher.search(item.getData());
                    } else if(item.isEOF()) {
                        printResult(item.getFileName(), searcher.terminate());
                        searcher.reset();
                    } else if(item.isNotFound()) {
                        System.err.println(item.getFileName() + ": файл не найден");
                    }
                    
                    item = queue.take();
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }
}
