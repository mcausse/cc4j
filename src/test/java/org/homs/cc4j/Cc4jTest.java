package org.homs.cc4j;

import org.homs.cc4j.issue.IssuesReport;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.homs.cc4j.issue.IssueSeverity.*;

public class Cc4jTest {

    private File getFile(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
    }

    @Test
    void naming_convention_rules() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/naming_convention_rules.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(7);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "class 'le_class' should comply with a naming convention: ^[A-Z][a-zA-Z0-9]*$ (at [naming_convention_rules.java]: le_class)",
                "constant 'leConstant' should comply with a naming convention: ^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$",
                "property 'LeProperty' should comply with a naming convention: ^[a-z][a-zA-Z0-9]*$",
                "method 'Le_Method' should comply with a naming convention: ^[a-z][a-zA-Z0-9]*$",
                "parameter 'Abc' should comply with a naming convention: ^[a-z][a-zA-Z0-9]*$",
                "class 'le_inner_class' should comply with a naming convention: ^[A-Z][a-zA-Z0-9]*$",
                "class 'le_inner_class2' should comply with a naming convention: ^[A-Z][a-zA-Z0-9]*$"
        );
    }

    @Test
    void todos_and_fixmes() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/todos_and_fixmes.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(2);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "1 pending TODO(s) found",
                "1 pending FIXME(s) found"
        );
    }

    @Test
    void class_max_line_width() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/class_max_line_width.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "file has a line (line #3) of 146 columns width"
        );
    }

    @Test
    void class_members_ordering() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/class_members_ordering.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "class members should be ordered as the convention: 7543261 (pattern is: ^1?2*3*4*5*6*7*$)"
        );
    }

    @Test
    void too_many_methods_in_class() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/too_many_methods_in_class.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "too many methods: 17 (>15 warning, >25 critical, >30 error) (at [too_many_methods_in_class.java]: Jou)"
        );
    }

    @Test
    void too_many_method_arguments() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/too_many_method_arguments.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "too many arguments: 5"
        );
    }

    @Test
    void use_pronounceable_names() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/use_pronounceable_names.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "use pronounceable names: 'setNPLHDirectory' scored with 5"
        );
    }

    @Test
    void too_deply_nested_code() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/too_deply_nested_code.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "5 indent levels"
        );
    }

    @Test
    void too_many_effective_lines_in_method() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/too_many_effective_lines_in_method.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "too many effective lines of code: 26"
        );
    }

    @Test
    void too_many_lines_of_code_per_class() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/too_many_lines_of_code_per_class.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(ir.getIssues().toString()).contains(
                "file has a total of 235 lines"
        );
    }

    @Test
    void too_complicated_relational_expression() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/too_complicated_relational_expression.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(ir.getIssues().toString()).contains(
                "+ too complicated logical condition, rated as 7 (>3 warning, >5 critical, >7 error); expression=(a <= b && b >= c || c < b && !(b > a || e == f) || f == g || g != h) (at [too_complicated_relational_expression.java]: Jou.jou(..))]"
        );
    }

    @Test
    void add_spaces_to_increase_the_readibility() throws IOException {

        var ir = new IssuesReport();

        // act
        new Cc4j(ir).analyseJavaFile(
                getFile("cc4j/rules/add_spaces_to_increase_the_readibility.java"));

        assertThat(ir.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(ir.getIssuesCountBySeverity(WARNING)).isEqualTo(4);
        assertThat(ir.getIssues().toString()).contains(
                "- 2 (after ',') spaces pending to add to increase the readibility (at [add_spaces_to_increase_the_readibility.java]: line 2)",
                "- 1 ('=') spaces pending to add to increase the readibility (at [add_spaces_to_increase_the_readibility.java]: line 3)",
                "- 1 ('){') spaces pending to add to increase the readibility (at [add_spaces_to_increase_the_readibility.java]: line 3)"
        );
    }
}
