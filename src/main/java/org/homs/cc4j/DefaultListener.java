package org.homs.cc4j;

import org.homs.cc4j.issue.IssuesReportJ;

import java.util.List;

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
    public void checkClassName(Location location, String className) {
        rules.checkClassNameConvention(location, className, "class");
        rules.pronounceableAnalysis(location, className);
    }

    @Override
    public void checkMethodName(Location location, String methodName) {
        rules.checkMethodNameConvention(location, methodName);
        rules.pronounceableAnalysis(location, methodName);
    }

    @Override
    public void checkConstantName(Location location, String propertyName) {
        rules.checkConstantNameConvention(location, propertyName);
        rules.pronounceableAnalysis(location, propertyName);
    }

    @Override
    public void checkPropertyName(Location location, String propertyName) {
        rules.checkPropertyNameConvention(location, propertyName);
        rules.pronounceableAnalysis(location, propertyName);
    }

    @Override
    public void checkArgumentName(Location location, String argumentName) {
        rules.checkArgumentNameConvention(location, argumentName);
        rules.pronounceableAnalysis(location, argumentName);
    }

    @Override
    public void onClassMembersCount(Location location, int constantsCount, int propertiesCount, int methodsCount, int innerClassesCount) {
        rules.checkTooManyPropertiesPerClass(location, propertiesCount);
//        rules.checkTooManyMethodsPerClass(location, methodsCount);
    }

    @Override
    public void checkMethodMetrics(Location location, int argumentsCount, int linesOfEffectiveCode) {
//        rules.checkTooManyMethodArguments(location, argumentsCount);
        rules.checkTooManyEffectiveLinesPerMethod(location, linesOfEffectiveCode);
    }

    @Override
    public void onMethodMaxIndentLevel(Location location, int indentMaxLevel) {
        rules.checkTooDeplyNestedCode(location, indentMaxLevel);
    }

    // TODO avoid ternary operators: ? .. : ..

    @Override
    public void onClassMembersOrder(Location location, List<Cc4j.Member> memberTypesList) {
        rules.checkClassMembersOrdering(location, memberTypesList);
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