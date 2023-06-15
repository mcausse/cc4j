package org.homs.cc4j;

import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.IssuesReportVisitor;
import org.homs.cc4j.issue.SimpleIssuesReportVisitor;
import org.homs.cc4j.util.FileUtils;
import org.homs.cc4j.visitors.MetricsCounterVisitor;
import org.homs.cc4j.visitors.RuleTreeVisitor;
import org.homs.cc4j.visitors.rules.*;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import static org.homs.cc4j.util.FileUtils.processDirectory;

public class Cc4j {

    final IssuesReport issuesReport;
    final MetricsCounterVisitor metricsCounterVisitor;

    public Cc4j() {
        this.issuesReport = new IssuesReport();
        this.metricsCounterVisitor = new MetricsCounterVisitor();
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
        List<RuleTreeVisitor<?>> rules = List.of(
                new ClassMembersOrderingRule(),
                new MaxIndentLevelRule(),
                new NamingConventionsRule(),
                new TooComplicatedConditionRule(),
                new TooManyArgumentsRule(),
                new TooManyEffectiveLinesPerMethodRule(),
                new TooManyMethodsRule(),
                new UsePronounceableNamesRule(),
                new AvoidOptionalArgumentsRule()
        );
        analizer.acceptRuleVisitors(issuesReport, rules);
    }

    protected void analyseTextBasedRules(FilesAnalyser analizer) {
        analizer.forEachFile(file -> {
            String sourceCode = FileUtils.loadFile(file.toString());
            String uri = file.getName();

            final FileRules fileRules = new FileRules(issuesReport);
            fileRules.checkTodosAndFixmes(uri, sourceCode);
            fileRules.checkClassMaxLineWidth(uri, sourceCode);
            fileRules.checkClassMaxNumberOfLines(uri, sourceCode);
            fileRules.checkForAddSpacesToIncreaseReadibility(uri, sourceCode);
        });
    }

    public void report() {
        report(System.out);
    }

    public void report(PrintStream ps) {
        report(ps, new SimpleIssuesReportVisitor());
    }

    public void report(PrintStream ps, IssuesReportVisitor... issuesVisitors) {
        this.metricsCounterVisitor.printMetricsCount();
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

