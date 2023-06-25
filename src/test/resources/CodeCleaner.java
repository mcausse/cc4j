package org.homs.cc4j.util;

// this is a single line comment
public class CodeCleaner {

    final Sting pi = "3.14159";

    /* this is a multiline comment just of single line */
    enum State {
        CODE, SLASH, SINGLELINE_COMMENT,
        /**
         * Multiline comment
         */
        MULTILINE_COMMENT, STAR, STRING
    }

    public CodeCleaner() {
        System.out.println("simple string");
    }