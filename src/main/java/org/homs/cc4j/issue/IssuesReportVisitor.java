package org.homs.cc4j.issue;

import java.io.PrintStream;

public interface IssuesReportVisitor {

    void visit(IssuesReport issuesReport, PrintStream ps);
}
