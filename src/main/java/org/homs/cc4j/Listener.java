package org.homs.cc4j;

import org.homs.cc4j.issue.IssuesReportJ;

public interface Listener {

    void verifyClassSourceCode(String javaFileName, String sourceCode);

    void displayReport();

    IssuesReportJ getIssuesReport();
}
