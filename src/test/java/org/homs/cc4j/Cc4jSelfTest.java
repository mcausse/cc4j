package org.homs.cc4j;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.homs.cc4j.util.FileUtils.processDirectory;

public class Cc4jSelfTest {

    @Test
    void Self() throws IOException {

        List<File> files = processDirectory(new File("."),
                f -> f.endsWith(".java")
                        && f.contains("cc4j")
                        && !f.contains("test")
        );
        var listener = new DefaultListener();

        new Cc4j().analyseJavaFiles(files, listener);
    }

    @Test
    void LechugaScript() throws IOException {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\lispo2021"),
                f -> f.endsWith(".java")
        );
        var listener = new DefaultListener();

        new Cc4j().analyseJavaFiles(files, listener);
    }

    @Test
    void LechugaUML() throws IOException {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\martin-uml"),
                f -> f.endsWith(".java")
        );
        var listener = new DefaultListener();

        new Cc4j().analyseJavaFiles(files, listener);
    }

    @Test
    void ImprovedTerminal() throws IOException {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\tool-improved-terminal"),
                f -> f.endsWith(".java")
        );
        var listener = new DefaultListener();

        new Cc4j().analyseJavaFiles(files, listener);
    }

    @Test
    void all() throws IOException {

        List<File> files = processDirectory(new File("C:\\java\\workospace"),
                f -> f.endsWith(".java")
        );
        var listener = new DefaultListener();

        new Cc4j().analyseJavaFiles(files, listener);
    }

}
