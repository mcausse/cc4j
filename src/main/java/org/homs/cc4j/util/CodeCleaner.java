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
                strb.append(c);
                if (c == '/') {
                    return State.SLASH;
                }
                if (c == '"') {
                    return State.STRING;
                }
                return State.CODE;
            }
            case SLASH -> {
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
            case SINGLELINE_COMMENT -> {
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
            case MULTILINE_COMMENT -> {
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
            case STAR -> {
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
            case STRING -> {
                // return nextStateInString(c, strb, '"', State.CODE, cleanStrings, State.STRING);
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
            default -> throw new RuntimeException(state.name());
        }
    }

//    private State nextStateInString(char c, StringBuilder strb, char c2, State code, boolean cleanStrings, State string) {
//        if (c == c2) {
//            strb.append(c);
//            return code;
//        }
//        if (cleanStrings) {
//            strb.append(encode(c));
//        } else {
//            strb.append(c);
//        }
//        return string;
//    }
}