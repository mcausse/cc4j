package org.homs.cc4j;

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

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/naming_convention_rules.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(6);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(r.getIssues().toString()).contains(
                "class name should comply with a naming convention: le_class",
                "constant name should comply with a naming convention: leConstant",
                "property name should comply with a naming convention: LeProperty",
                "method name should comply with a naming convention: Le_Method",
                "argument name should comply with a naming convention: Abc",
                "class name should comply with a naming convention: le_inner_class"
        );
    }

    @Test
    void todos_and_fixmes() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/todos_and_fixmes.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(2);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(r.getIssues().toString()).contains(
                "1 pending TODO(s) found",
                "1 pending FIXME(s) found"
        );
    }

    @Test
    void class_max_line_width() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/class_max_line_width.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(r.getIssues().toString()).contains(
                "class has a line (line #3) of 136 columns width"
        );
    }

    @Test
    void class_members_ordering() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/class_members_ordering.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(r.getIssues().toString()).contains(
                "class members should be ordered as the convention: 7543261 (pattern is: ^1?2*3*4*5*6*7*$)"
        );
    }

    @Test
    void too_many_methods_in_class() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/too_many_methods_in_class.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(r.getIssues().toString()).contains(
                "too many methods: 17 (>15 warning, >25 critical, >30 error) (at [too_many_methods_in_class.java])"
        );
    }

    @Test
    void too_many_properties_in_class() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/too_many_properties_in_class.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(r.getIssues().toString()).contains(
                "too many properties in a class: 17"
        );
    }

    @Test
    void too_many_method_arguments() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/too_many_method_arguments.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(r.getIssues().toString()).contains(
                "too many arguments: 5"
        );
    }

    @Test
    void use_pronounceable_names() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/use_pronounceable_names.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(r.getIssues().toString()).contains(
                "use pronounceable names: 'setNPLHDirectory' scored with 5"
        );
    }

    @Test
    void too_deply_nested_code() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/too_deply_nested_code.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(r.getIssues().toString()).contains(
                "5 indent levels"
        );
    }

    @Test
    void too_many_effective_lines_in_method() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/too_many_effective_lines_in_method.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(r.getIssues().toString()).contains(
                "too many effective lines of code: 26"
        );
    }

    @Test
    void too_many_lines_of_code_per_class() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/too_many_lines_of_code_per_class.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(1);
        assertThat(r.getIssues().toString()).contains(
                "class has a total of 235 lines"
        );
    }

    @Test
    void too_complicated_relational_expression() throws IOException {

        // act
        var r = new Cc4j().analyseJavaFile(
                getFile("cc4j/rules/too_complicated_relational_expression.java"),
                new DefaultListener());

        assertThat(r.getIssuesCountBySeverity(ERROR)).isEqualTo(0);
        assertThat(r.getIssuesCountBySeverity(CRITICAL)).isEqualTo(1);
        assertThat(r.getIssuesCountBySeverity(WARNING)).isEqualTo(0);
        assertThat(r.getIssues().toString()).contains(
                "+ too complicated logical condition, rated as 7 (>3 warning, >5 critical, >7 error); expression=(a <= b && b >= c || c < b && !(b > a || e == f) || f == g || g != h) (at [too_complicated_relational_expression.java]: Jou.jou(..))]"
        );
    }
}
