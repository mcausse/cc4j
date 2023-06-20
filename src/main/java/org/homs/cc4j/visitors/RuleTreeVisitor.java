package org.homs.cc4j.visitors;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import org.homs.cc4j.Rule;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;

import java.util.UnknownFormatConversionException;

public abstract class RuleTreeVisitor<T> extends Java19MetricsTreeVisitor<T> implements Rule {

    protected IssuesReport issuesReport;
    protected Location location;

    public void setIssuesReport(IssuesReport issuesReport) {
        this.issuesReport = issuesReport;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Integer visitClass(ClassTree node, T p) {
        this.location.push(node.getSimpleName().toString());
        var r = super.visitClass(node, p);
        this.location.pop();
        return r;
    }

    @Override
    public Integer visitMethod(MethodTree node, T p) {
        location.push(node.getName().toString() + "(..)");
        int r = super.visitMethod(node, p);
        location.pop();
        return r;
    }

    protected void generateIssue(IssueSeverity severity, String message) {
        issuesReport.registerIssue(location, severity, getRuleInfo(), message);
    }

    protected void generateIssueIfThreshold(String message, int metricValue, int warningThr, int criticalThr, int errorThr) {
        try {
            message = String.format(message, metricValue, warningThr, criticalThr, errorThr);
        } catch (UnknownFormatConversionException e) {
            throw new RuntimeException(message, e);
        }
        if (metricValue > errorThr) {
            issuesReport.registerIssue(location, IssueSeverity.ERROR, getRuleInfo(), message);
        } else if (metricValue > criticalThr) {
            issuesReport.registerIssue(location, IssueSeverity.CRITICAL, getRuleInfo(), message);
        } else if (metricValue > warningThr) {
            issuesReport.registerIssue(location, IssueSeverity.WARNING, getRuleInfo(), message);
        }
    }
}
