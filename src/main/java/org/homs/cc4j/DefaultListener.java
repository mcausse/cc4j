package org.homs.cc4j;

import org.homs.cc4j.issue.IssuesReportJ;

public class DefaultListener implements Listener {

    final Rules rules = new Rules(new IssuesReportJ());

    @Override
    public void verifyClassSourceCode(String javaFileName, String sourceCode) {
        rules.checkTodosAndFixmes(javaFileName, sourceCode);
        rules.checkClassMaxLineWidth(javaFileName, sourceCode);
        rules.checkClassMaxNumberOfLines(javaFileName, sourceCode);

        // TODO add an space to increase the readability (please format the code)
        // TODO follow the Java curly braces style
    }

    @Override
    public void displayReport() {
        rules.getIssuesReport().displayReport();
    }

    @Override
    public IssuesReportJ getIssuesReport() {
        return rules.getIssuesReport();
    }
}