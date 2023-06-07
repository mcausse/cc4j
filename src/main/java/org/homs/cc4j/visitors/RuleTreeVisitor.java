package org.homs.cc4j.visitors;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import org.homs.cc4j.Listener;
import org.homs.cc4j.Location;
import org.homs.cc4j.issue.IssueSeverity;

import java.util.UnknownFormatConversionException;

public abstract class RuleTreeVisitor extends Java19MetricsTreeVisitor<Void> {

    final Listener listener;
    final Location location;

    public RuleTreeVisitor(Listener listener, Location location) {
        this.listener = listener;
        this.location = location;
    }

    public Integer visitClass(ClassTree node, Void p) {
        this.location.push(node.getSimpleName().toString());
        return super.visitClass(node, p);
    }

    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");
        int r = super.visitMethod(node, p);
        location.pop();
        return r;
    }

    protected void generateIssue(IssueSeverity severity, String message) {
        listener.getIssuesReport().registerIssue(severity, location.toString(), message);
    }

    protected void generateIssueIfThreshold(String message, int metricValue, int warningThr, int criticalThr, int errorThr) {
        try {
            message = String.format(message, metricValue, warningThr, criticalThr, errorThr);
        } catch (UnknownFormatConversionException e) {
            throw new RuntimeException(message, e);
        }
        if (metricValue > errorThr) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.ERROR, location.toString(), message);
        } else if (metricValue > criticalThr) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.CRITICAL, location.toString(), message);
        } else if (metricValue > warningThr) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.WARNING, location.toString(), message);
        }
    }
}
