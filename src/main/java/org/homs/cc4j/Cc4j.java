package org.homs.cc4j;

import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.IssuesReportVisitor;
import org.homs.cc4j.issue.SimpleIssuesReportVisitor;
import org.homs.cc4j.rules.text.FileRules;
import org.homs.cc4j.rules.text.TextRule;
import org.homs.cc4j.rules.visitors.AstRules;
import org.homs.cc4j.rules.visitors.MetricsCounterVisitor;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;
import org.homs.cc4j.util.FileUtils;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.homs.cc4j.util.FileUtils.processDirectory;

public class Cc4j {

    final List<RuleTreeVisitor<?>> astRulesList;
    final List<TextRule> textRulesList;
    final IssuesReport issuesReport;
    final MetricsCounterVisitor metricsCounterVisitor;

    public Cc4j(List<RuleTreeVisitor<?>> astRulesList, List<TextRule> textRulesList) {
        this.astRulesList = astRulesList;
        this.textRulesList = textRulesList;
        this.issuesReport = new IssuesReport();
        this.metricsCounterVisitor = new MetricsCounterVisitor();
    }

    public Cc4j() {
        this(AstRules.RULES, FileRules.TEXT_RULES);
    }

    public void analyseDirectory(String directory) {
        List<File> files = processDirectory(new File(directory),
                f -> f.endsWith(".java")
        );

        analyse(files);
    }

    public void analyseFile(String file) {
        analyse(List.of(new File(file)));
    }

    public void analyse(List<File> files) {
        var analizer = FilesAnalyser.forFiles(files);

        /*
         * TEXT-BASED RULES
         */
        analyseTextBasedRules(analizer);

        /*
         * AST-BASED RULES
         */
        analyseAstBasedRules(analizer);

        /*
         * METRICS COUNTER (IS A VISITOR)
         */
        analizer.acceptVisitor(metricsCounterVisitor);
    }

    protected void analyseAstBasedRules(FilesAnalyser analizer) {
        analizer.acceptRuleVisitors(issuesReport, AstRules.RULES);
    }

    protected void analyseTextBasedRules(FilesAnalyser analizer) {
        final FileRules fileRules = new FileRules(issuesReport);
        analizer.forEachFile(file -> {
            String sourceCode = FileUtils.loadFile(file.toString());
            String javaFilename = file.getName();
            fileRules.check(javaFilename, sourceCode);
        });
    }

    public void report() {
        report(System.out);
    }

    public void report(PrintStream ps) {
        report(ps, new SimpleIssuesReportVisitor());
    }

    public void report(PrintStream ps, IssuesReportVisitor... issuesVisitors) {

        /*
         * list all the rules to STDOUT
         */
        List<Rule> rules = new ArrayList<>();
        rules.addAll(AstRules.RULES);
        rules.addAll(FileRules.TEXT_RULES);
        rules.sort(Comparator.comparing(o -> o.getRuleInfo().toString()));
        rules.forEach(r -> System.out.println(r.getRuleInfo().getFullDescription()));

        this.metricsCounterVisitor.printMetricsCount(issuesReport);
        for (var issuesVisitor : issuesVisitors) {
            this.issuesReport.acceptReportVisitor(ps, issuesVisitor);
        }
    }

    public IssuesReport getIssuesReport() {
        return issuesReport;
    }

    public MetricsCounterVisitor getMetricsCounterVisitor() {
        return metricsCounterVisitor;
    }
}

