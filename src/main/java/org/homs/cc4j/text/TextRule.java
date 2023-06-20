package org.homs.cc4j.text;

import org.homs.cc4j.Rule;
import org.homs.cc4j.issue.IssuesReport;

public interface TextRule extends Rule {
    void execute(IssuesReport issuesReport, String javaFileName, String sourceCode);
}
