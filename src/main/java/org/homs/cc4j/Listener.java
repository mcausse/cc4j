package org.homs.cc4j;

import org.homs.cc4j.issue.IssuesReportJ;

import java.util.List;

public interface Listener {

    void verifyClassSourceCode(String javaFileName, String sourceCode);

    void checkClassName(Location location, String className);

    void checkMethodName(Location location, String methodName);

    void checkConstantName(Location location, String propertyName);

    void checkPropertyName(Location location, String propertyName);

    void checkArgumentName(Location location, String argumentName);

    void onClassMembersCount(Location location, int constantsCount, int propertiesCount, int methodsCount, int innerClassesCount);

    void checkMethodMetrics(Location location, int argumentsCount, int linesOfEffectiveCode);

    void onMethodMaxIndentLevel(Location location, int indentMaxLevel);

    void onClassMembersOrder(Location location, List<Cc4j.Member> r);

    void displayReport();

    IssuesReportJ getIssuesReport();
}
