package ru.kurtov.jgrep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;


public class JGrepMappedByteBuffer extends JGrep {
    private final static Charset charset = Charset.forName("UTF-8"); //"ISO-8859-15"
    private final static CharsetDecoder decoder = charset.newDecoder();

    public JGrepMappedByteBuffer(String pattern) {
        super(pattern);
    }

    public JGrepMappedByteBuffer(String pattern, int SearcherType) {
        super(pattern, SearcherType);
    }
    
    @Override
    public void find() throws IOException {
        for(String fileName : this.fileNames) {
            File f = new File(fileName); 

            // Open the file and then get a channel from the stream
            FileInputStream fis = new FileInputStream(f);
            FileChannel fc = fis.getChannel();

            // Get the file's size and then map it into memory
            int sz = (int)fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

            // Decode the file into a char buffer
            CharBuffer cb = decoder.decode(bb);

            // Perform the search

            searcher.search(cb.array());
            // Close the channel and the stream
            fc.close();

            printResult(f, searcher.terminate());
            searcher.reset();
        }
    }
}
