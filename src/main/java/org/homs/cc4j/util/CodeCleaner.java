package org.homs.cc4j.util;

public class CodeCleaner {

    enum State {
        CODE, SLASH, SINGLELINE_COMMENT, MULTILINE_COMMENT, STAR, STRING
    }

    final boolean cleanComments;
    final boolean cleanStrings;

    public CodeCleaner(boolean cleanComments, boolean cleanStrings) {
        this.cleanComments = cleanComments;
        this.cleanStrings = cleanStrings;
    }

    public String cleanTheCode(String code) {
        var state = State.CODE;
        var strb = new StringBuilder();
        for (char c : code.toCharArray()) {
            state = consumeChar(state, c, strb);
        }
        return strb.toString();
    }

    char encode(char c) {
        if (Character.isWhitespace(c)) {
            return c;
        }
        return '*';
    }

    State consumeChar(State state, char c, StringBuilder strb) {
        switch (state) {
            case CODE -> {
                return getNextStateForCode(c, strb);
            }
            case SLASH -> {
                return getNextStateForSlash(c, strb);
            }
            case SINGLELINE_COMMENT -> {
                return getNextStateForSingleLineComment(c, strb);
            }
            case MULTILINE_COMMENT -> {
                return getNextStateForMultilineComment(c, strb);
            }
            case STAR -> {
                return getNextStateForStar(c, strb);
            }
            case STRING -> {
                return getNextStateForString(c, strb);
            }
            default -> throw new RuntimeException(state.name());
        }
    }

    private State getNextStateForCode(char c, StringBuilder strb) {
        strb.append(c);
        if (c == '/') {
            return State.SLASH;
        }
        if (c == '"') {
            return State.STRING;
        }
        return State.CODE;
    }

    private State getNextStateForSlash(char c, StringBuilder strb) {
        if (c == '/') {
            strb.append(c);
            return State.SINGLELINE_COMMENT;
        }
        if (c == '*') {
            strb.append(c);
            return State.MULTILINE_COMMENT;
        }
        strb.append(encode(c));
        return State.CODE;
    }

    private State getNextStateForSingleLineComment(char c, StringBuilder strb) {
        if (c == '\n') {
            strb.append(c);
            return State.CODE;
        }
        if (cleanComments) {
            strb.append(encode(c));
        } else {
            strb.append(c);
        }
        return State.SINGLELINE_COMMENT;
    }

    private State getNextStateForMultilineComment(char c, StringBuilder strb) {
        if (c == '*') {
            strb.append(c);
            return State.STAR;
        }
        if (cleanComments) {
            strb.append(encode(c));
        } else {
            strb.append(c);
        }
        return State.MULTILINE_COMMENT;
    }

    private State getNextStateForStar(char c, StringBuilder strb) {
        if (c == '/') {
            strb.append(c);
            return State.CODE;
        }
        if (c == '*') { // while *
            strb.append(c);
            return State.STAR;
        }
        strb.append(encode(c));
        return State.MULTILINE_COMMENT;
    }

    private State getNextStateForString(char c, StringBuilder strb) {
        if (c == '\\') {
            strb.append(c);
            return State.STRING;
        }
        if (!strb.isEmpty() && strb.charAt(strb.length() - 1) == '\\') {
            // lo consumit anteriorment és un backslash que inicia un escape;
            // ignorar doncs el char actual
            strb.append(encode(c));
            return State.STRING;
        }

        if (c == '"') {
            strb.append(c);
            return State.CODE;
        }
        if (cleanStrings) {
            strb.append(encode(c));
        } else {
            strb.append(c);
        }
        return State.STRING;
    }
}