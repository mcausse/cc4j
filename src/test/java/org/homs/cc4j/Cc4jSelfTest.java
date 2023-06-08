package org.homs.cc4j;

import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.PerClassIssuesReportVisitor;
import org.junit.jupiter.api.Disabled;
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

        var ir = new IssuesReport();
        new Cc4j().analyseJavaFiles(files, ir);
        ir.acceptReportVisitor(new PerClassIssuesReportVisitor());
    }

    @Test
    void LechugaScript() throws IOException {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\lispo2021"),
                f -> f.endsWith(".java")
        );

        new Cc4j().analyseJavaFiles(files, new IssuesReport());
    }

    @Test
    void LechugaUML() throws IOException {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\martin-uml"),
                f -> f.endsWith(".java")
        );

        new Cc4j().analyseJavaFiles(files, new IssuesReport());
    }

    @Test
    void ImprovedTerminal() throws IOException {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\tool-improved-terminal"),
                f -> f.endsWith(".java")
        );

        new Cc4j().analyseJavaFiles(files, new IssuesReport());
    }

    @Disabled
    @Test
    void all() throws IOException {

        List<File> files = processDirectory(new File("C:\\java\\workospace"),
                f -> f.endsWith(".java")
        );

        new Cc4j().analyseJavaFiles(files, new IssuesReport());
    }

}
