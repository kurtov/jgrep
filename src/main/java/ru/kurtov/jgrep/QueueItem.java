package ru.kurtov.jgrep;

public class QueueItem {
    public static int TERMINATOR = 1;
    public static int DATA = 2;
    public static int NOT_FOUND = 3;
    public static int EOF = 4;
    
    String fileName;
    char[] data;
    int type;
    
    private QueueItem(String fileName, char[] data, int type) {
        this.fileName = fileName;
        this.data = data;
        this.type = type;
    }
    
    static QueueItem getTerminatorItem() {
        return new QueueItem(null, null, TERMINATOR);
    }
    
    static QueueItem getDataItem(String fileName, char[] data) {
        return new QueueItem(fileName, data, DATA);
    }
    
    static QueueItem getNotFoundItem(String fileName) {
        return new QueueItem(fileName, null, NOT_FOUND);
    }
    
    static QueueItem getEOFItem(String fileName) {
        return new QueueItem(fileName, null, EOF);
    }
    
    public boolean isTerminator() {
        return this.type == TERMINATOR;
    }
   
    public boolean isData() {
        return this.type == DATA;
    }

    public boolean isNotFound() {
        return this.type == NOT_FOUND;
    }
    
    public boolean isEOF() {
        return this.type == EOF;
    }
    
    public char[] getData() {
        return this.data;
    }
    
    public String getFileName() {
        return this.fileName;
    }
}