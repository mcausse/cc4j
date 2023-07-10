package org.homs.cc4j.rules.visitors.rules;

import org.homs.cc4j.Cc4j;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.Thresholds;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.homs.cc4j.TestUtils.getFile;

class CyclomaticComplexityTooHighRuleTest {


    @Test
    void name() {

        var cc4j = new Cc4j(
                List.of(new CyclomaticComplexityTooHighRule(new Thresholds(0, 120, 130))),
                List.of()
        );
        cc4j.analyseFile(getFile("cc4j/rules/cyclomatic_complexity_too_high.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(IssueSeverity.WARNING)).isEqualTo(7);
        assertThat(ir.getIssuesCountBySeverity(IssueSeverity.CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(IssueSeverity.ERROR)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "- [cy01] cyclomatic complexity too high: 3 (>0 warning, >120 critical, >130 error) (at [cyclomatic_complexity_too_high.java]: Jou.{})",
                "- [cy01] cyclomatic complexity too high: 12 (>0 warning, >120 critical, >130 error) (at [cyclomatic_complexity_too_high.java]: Jou.cyclomatic12(..))",
                "- [cy01] cyclomatic complexity too high: 14 (>0 warning, >120 critical, >130 error) (at [cyclomatic_complexity_too_high.java]: Jou.cyclomatic14(..))",
                "- [cy01] cyclomatic complexity too high: 15 (>0 warning, >120 critical, >130 error) (at [cyclomatic_complexity_too_high.java]: Jou.cyclomatic15(..))",
                "- [cy01] cyclomatic complexity too high: 12 (>0 warning, >120 critical, >130 error) (at [cyclomatic_complexity_too_high.java]: Jou.inspectMethod12(..))",
                "- [cy01] cyclomatic complexity too high: 3 (>0 warning, >120 critical, >130 error) (at [cyclomatic_complexity_too_high.java]: Jou.lambdas_and_catches_3_1(..))",
                "- [cy01] cyclomatic complexity too high: 3 (>0 warning, >120 critical, >130 error) (at [cyclomatic_complexity_too_high.java]: Jou.lambdas_and_catches_3_2(..))"
        );
    }


}