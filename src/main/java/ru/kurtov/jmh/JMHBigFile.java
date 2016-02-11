package ru.kurtov.jmh;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import ru.kurtov.jgrep.JGrep;
import ru.kurtov.jgrep.JGrepCharBuffer;
import ru.kurtov.jgrep.JGrepCharBufferMultiThred;
import ru.kurtov.jgrep.JGrepMappedByteBuffer;


@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JMHBigFile {
    
    File file;
    String pattern;
    
    @Setup
    public void setUp(){
        //file = new File("/Users/mike/NetBeansProjects/jgrep/resources/WarAndPeace.txt");
        file = new File("resources/WarAndPeace.txt");
        pattern = "письмо";
    }
    
    @Benchmark
    public void kmpSearcherCharBuffer() throws IOException {
        JGrep grep = new JGrepCharBuffer(pattern, JGrep.KMP_SEARCHER);
        grep.setSuppressOutput(true);
        grep.find(file);
    }

    @Benchmark
    public void simpleSearcherJGrepCharBuffer() throws IOException {
        JGrep grep = new JGrepCharBuffer(pattern, JGrep.SIMPLE_SEARCHER);
        grep.setSuppressOutput(true);
        grep.find(file);
    }
    
    @Benchmark
    public void kmpSearcherMappedByteBuffer() throws IOException {
        JGrep grep = new JGrepMappedByteBuffer(pattern, JGrep.KMP_SEARCHER);
        grep.setSuppressOutput(true);
        grep.find(file);
    }

    @Benchmark
    public void simpleSearcherMappedByteBuffer() throws IOException {
        JGrep grep = new JGrepMappedByteBuffer(pattern, JGrep.SIMPLE_SEARCHER);
        grep.setSuppressOutput(true);
        grep.find(file);
    }
    
    @Benchmark
    public void kmpSearcherCharBufferMultiThred() throws IOException {
        JGrep grep = new JGrepCharBufferMultiThred(pattern, JGrep.KMP_SEARCHER);
        grep.setSuppressOutput(true);
        grep.find(file);
    }
    
    @Benchmark
    public void simpleSearcherCharBufferMultiThred() throws IOException {
        JGrep grep = new JGrepCharBufferMultiThred(pattern, JGrep.SIMPLE_SEARCHER);
        grep.setSuppressOutput(true);
        grep.find(file);
    }

    @Benchmark
    public void Grep() throws IOException {
        Grep.compile(pattern);
        Grep.grep(file);
    }
    
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHBigFile.class.getSimpleName())
                .forks(1)
                .warmupIterations(10)
                .warmupTime(TimeValue.milliseconds(10))
                .measurementIterations(30)
                .measurementTime(TimeValue.milliseconds(10))
                .build();
        
        new Runner(opt).run();
    }
}