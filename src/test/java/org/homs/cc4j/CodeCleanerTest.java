package org.homs.cc4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class CodeCleanerTest {

    @Test
    void name_0() throws Exception {

        String code = TestUtils.loadFromClasspath("SimpleClass.cls");

        // Act
        var cleaned = new CodeCleaner().cleanTheCode(code);

        assertThat(cleaned).isEqualTo(
                "// ***\n" +
                        "Class Juas {\n" +
                        "/* **** * */\n" +
                        "Method AAA() {\n" +
                        "    return \"***\" / 2\n" +
                        "}\n" +
                        "}");
    }

    static Stream<Arguments> test_1_Provider() {
        return Stream.of(
                Arguments.of("/**/", "/**/"),
                Arguments.of("/***/", "/***/"),
                Arguments.of("/****/", "/****/"),
                Arguments.of("/*****/", "/*****/"),
                Arguments.of("/******/", "/******/"),

                Arguments.of("//", "//"),
                Arguments.of("//*", "//*"),
                Arguments.of("//**", "//**"),

                Arguments.of("// *", "// *"),
                Arguments.of("// **", "// **"),

                Arguments.of("// a", "// *"),
                Arguments.of("// aa", "// **")

        );
    }

    @ParameterizedTest
    @MethodSource("test_1_Provider")
    void name_1(String code, String expectedCleaned) {

        // Act
        var cleaned = new CodeCleaner().cleanTheCode(code);

        assertThat(cleaned).isEqualTo(expectedCleaned);
    }

    @Test
    void name_2() throws Exception {

        String code = TestUtils.loadFromClasspath("SimpleClass2.cls");

        // Act
        var cleaned = new CodeCleaner().cleanTheCode(code);

        assertThat(cleaned).isEqualTo(
                "// ***\n" +
                        "Class Juas {\n" +
                        "/* **** * */\n" +
                        "Method AAA() {\n" +
                        "    /************ ******* *** ******* ************* ** ********* ************/\n" +
                        "    return \"***\" / 2\n" +
                        "}\n" +
                        "}");
    }
}