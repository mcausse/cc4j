package org.homs.cc4j;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.homs.cc4j.issue.IssueSeverity.*;

public class Cc4jTest {

    private String getFile(String classPathResourceName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(classPathResourceName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found: " + classPathResourceName);
        }

        return new File(resource.getFile()).toString();
    }

    @Test
    void naming_convention_rules() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/naming_convention_rules.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(7);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "class 'le_class' should comply with a naming convention: ^[A-Z][a-zA-Z0-9]*$ (at ",
                "constant 'leConstant' should comply with a naming convention: ^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$",
                "property 'LeProperty' should comply with a naming convention: ^[a-z][a-zA-Z0-9]*$",
                "method 'Le_Method' should comply with a naming convention: ^[a-z][a-zA-Z0-9]*$",
                "parameter 'Abc' should comply with a naming convention: ^[a-z][a-zA-Z0-9]*$",
                "class 'le_inner_class' should comply with a naming convention: ^[A-Z][a-zA-Z0-9]*$",
                "class 'le_inner_class2' should comply with a naming convention: ^[A-Z][a-zA-Z0-9]*$",

                "]: le_class.Le_Method(..))",
                "]: le_class.le_inner_class.le_inner_class2)"
        );
    }

    @Test
    void todos_and_fixmes_and_posponed_debt() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/todos_and_fixmes_and_posponed_debt.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(5);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "+ [td02] 1 pending TODO(s) found (at [",
                "+ [td01] 1 pending FIXME(s) found (at [",
                "+ [td04] 1 pending @Ignore(s) (without justification) found (at [",
                "+ [td04] 1 pending @Disabled(s) (without justification) found (at [",
                "+ [td03] 1 pending @Deprecated(s) found (at [",
                "]: line 6)",
                "]: line 7)",
                "]: line 8)"
        );
    }

    @Test
    void class_max_line_width() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/class_max_line_width.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "file has a line (line #3) of 146 columns width"
        );
    }

    @Test
    void class_members_ordering() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/class_members_ordering.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "class members should be ordered as the convention: 7543261 (pattern is: ^1?2*3*4*5*6*7*$)"
        );
    }

    @Test
    void too_many_methods_in_class() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/too_many_methods_in_class.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "- [cc07] too many methods: 17 (>15 warning, >25 critical, >30 error) (at [too_many_methods_in_class.java]: Jou)"
        );
    }

    @Test
    void too_many_method_arguments() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/too_many_method_arguments.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "too many arguments: 5"
        );
    }

    @Test
    void use_pronounceable_names() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/use_pronounceable_names.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "use pronounceable names: 'setNPLHDirectory' scored with 5"
        );
    }

    @Test
    void too_deply_nested_code() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/too_deply_nested_code.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "+ [cc05] 5 indent levels (>3 warning, >4 critical, >5 error) (at [too_deply_nested_code.java]: Jou.a(..))"
        );
    }

    @Test
    void too_many_effective_lines_in_method() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/too_many_effective_lines_in_method.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "too many effective lines of code: 26"
        );
    }

    @Test
    void too_many_lines_of_code_per_class() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/too_many_lines_of_code_per_class.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "file has a total of 235 lines"
        );
    }

    @Test
    void too_complicated_relational_expression() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/too_complicated_relational_expression.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(2);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(2);
        assertThat(ir.getIssues().toString()).contains(
                "+ [cc02] too complicated logical condition, rated as 7 (>3 warning, >5 critical, >7 error); " +
                        "expression=(a <= b && b >= c || c < b && !(b > a || e == f) || f == g || g != h) " +
                        "(at [too_complicated_relational_expression.java]: Jou.jou(..))",

                "+ [cy01] cyclomatic complexity too high: 21 (>10 warning, >20 critical, >30 error (at [too_complicated_relational_expression.java]: Jou.jou(..))",
                "- [cy02] cognitive complexity too high: 11 (>10 warning, >20 critical, >30 error (at [too_complicated_relational_expression.java]: Jou.jou(..))"
        );
    }

    @Test
    void add_spaces_to_increase_the_readibility() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/add_spaces_to_increase_the_readibility.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(4);
        assertThat(ir.getIssues().toString()).contains(
                "- [co01] 2 (after ',') spaces pending to add to increase the readibility (at [add_spaces_to_increase_the_readibility.java]: line 2)",
                "- [co01] 1 ('){') spaces pending to add to increase the readibility (at [add_spaces_to_increase_the_readibility.java]: line 2)",
                "- [co01] 1 ('=') spaces pending to add to increase the readibility (at [add_spaces_to_increase_the_readibility.java]: line 3)",
                "- [co01] 1 ('){') spaces pending to add to increase the readibility (at [add_spaces_to_increase_the_readibility.java]: line 3)"
        );
    }

    @Test
    void avoid_optional_arguments() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/avoid_optional_arguments.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(2);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "+ [cc04] avoid Optional<..> arguments (at ",
                "+ [cc04] avoid Optional<..> arguments (at ",

                "]: Jou.a(..))",
                "]: Jou.b(..))"
        );
    }

    @Test
    void cyclomatic_complexity_too_high() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/cyclomatic_complexity_too_high.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(4);
        assertThat(ir.getIssues().toString()).contains(
                "- [cy01] cyclomatic complexity too high: 16 (>10 warning, >20 critical, >30 error (at [cyclomatic_complexity_too_high.java]: Jou.cyclomatic16(..))",
                "- [cy01] cyclomatic complexity too high: 15 (>10 warning, >20 critical, >30 error (at [cyclomatic_complexity_too_high.java]: Jou.cyclomatic15(..))"
        );
    }

    @Test
    void cognitive_complexity_too_high() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/cognitive_complexity_too_high.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(5);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(9);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(9);
//        assertThat(ir.getIssues().toString()).contains(
//                "+ [cy01] cyclomatic complexity too high: 29 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.cognitive28(..))",
//                "- [cy01] cyclomatic complexity too high: 15 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.cognitive29(..))",
//                "- [cy01] cyclomatic complexity too high: 14 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.evalAstCognitive24(..))",
//                "- [cy01] cyclomatic complexity too high: 11 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.aaa21(..))",
//
//                "- [cy02] cognitive complexity too high: 13 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.aaa13(..))",
//                "+ [cy02] cognitive complexity too high: 29 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.cognitive29(..))",
//                "+ [cy02] cognitive complexity too high: 28 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.cognitive28(..))",
//                "+ [cy02] cognitive complexity too high: 24 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.evalAstCognitive24(..))",
//                "+ [cy02] cognitive complexity too high: 21 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.aaa21(..))",
//                "+ [cy02] cognitive complexity too high: 24 (>10 warning, >20 critical, >30 error (at [cognitive_complexity_too_high.java]: Jou.next24(..))"
//        );
    }


    @Test
    void mhoms_cognitive_complexity_too_high() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("cc4j/rules/mhoms_cognitive_complexity_too_high.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(3);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(6);
        assertThat(ir.getIssues().toString()).contains(
                "+ [co03] method 'CucarachaMicroDb_find11' should comply with a naming convention: ^[a-z][a-zA-Z0-9]*$ (at [mhoms_cognitive_complexity_too_high.java]: Jou.CucarachaMicroDb_find11(..))",
                "- [cy03] M.Homs complexity metric is too high: 6 (>4 warning, >9 critical, >14 error (at [mhoms_cognitive_complexity_too_high.java]: Jou.CucarachaMicroDb_find11(..))"
        );
    }

    @Test
    void securityArguments() {

        var cc4j = new Cc4j();
        cc4j.analyseFile(getFile("SecurityArguments.java"));
        cc4j.report();

        var ir = cc4j.getIssuesReport();
        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(2);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(2);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(2);
        assertThat(ir.getIssues().toString()).contains(
                "* [cc02] too complicated logical condition, rated as 12 (>3 warning, >5 critical, >7 error); expression=!isTLSEnable && !isMutualEnable && (crtPath == null || crtPath.isEmpty()) && (keyPath == null || keyPath.isEmpty()) && (caCrtPath == null || caCrtPath.isEmpty()) && (clientAuthHosts == null || clientAuthHosts.isEmpty()) && port == 0 (at [SecurityArguments.java]: SecurityArguments.isEmpty(..))",
                "* [cc03] too many arguments: 7 (>3 warning, >4 critical, >5 error) (at [SecurityArguments.java]: SecurityArguments.<init>(..))",
                "+ [td02] 3 pending TODO(s) found (at [SecurityArguments.java])",
                "+ [cc08] file has a line (line #16) of 177 columns width (>140 warning, >160 critical, >190 error) (at [SecurityArguments.java])",
                "- [cc07] too many methods: 16 (>15 warning, >25 critical, >30 error) (at [SecurityArguments.java]: SecurityArguments)",
                "- [cy01] cyclomatic complexity too high: 11 (>10 warning, >20 critical, >30 error (at [SecurityArguments.java]: SecurityArguments.isEmpty(..))"
        );
    }
}
