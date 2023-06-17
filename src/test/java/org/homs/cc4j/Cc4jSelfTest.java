package org.homs.cc4j;

import org.homs.cc4j.issue.BriefReportVisitor;
import org.homs.cc4j.issue.PerClassIssuesReportVisitor;
import org.homs.cc4j.issue.SimpleIssuesReportVisitor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.homs.cc4j.util.FileUtils.processDirectory;

public class Cc4jSelfTest {

    @Test
    void Self() {

        List<File> files = processDirectory(new File("."),
                f -> f.endsWith(".java")
                        && f.contains("cc4j")
                        && !f.contains("test")
        );

        var cc4j = new Cc4j();
        cc4j.analyse(files);
        cc4j.report(System.out,
                new SimpleIssuesReportVisitor(),
                new BriefReportVisitor(),
                new PerClassIssuesReportVisitor());
    }

    @Test
    void LechugaScript() {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\lispo2021"),
                f -> f.endsWith(".java")
        );

        var cc4j = new Cc4j();
        cc4j.analyse(files);
        cc4j.report(System.out,
                new SimpleIssuesReportVisitor(),
                new BriefReportVisitor());
    }

    @Test
    void LechugaUML() {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\martin-uml"),
                f -> f.endsWith(".java")
        );

        var cc4j = new Cc4j();
        cc4j.analyse(files);
        cc4j.report(System.out,
                new SimpleIssuesReportVisitor(),
                new BriefReportVisitor());
    }

    @Test
    void ImprovedTerminal() {

        List<File> files = processDirectory(new File("C:\\java\\workospace\\tool-improved-terminal"),
                f -> f.endsWith(".java")
        );

        var cc4j = new Cc4j();
        cc4j.analyse(files);
        cc4j.report(System.out,
                new SimpleIssuesReportVisitor(),
                new BriefReportVisitor());
    }

    @Disabled
    @Test
    void all() {

        List<File> files = processDirectory(new File("C:\\java\\workospace"),
                f -> f.endsWith(".java")
        );

        var cc4j = new Cc4j();
        cc4j.analyse(files);
        cc4j.report();
    }


    @Test
    void all2() {

        var cc4j = new Cc4j();
        cc4j.analyseFile("C:\\java\\workospace\\lispo2021\\src\\main\\java\\org\\homs\\lechugascript\\eval\\Evaluator.java");
        cc4j.report();
    }
}
