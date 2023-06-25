package org.homs.cc4j.util;

public class CodeCleaner2 {

    abstract class State {
        final StringBuilder strb;

        public State(StringBuilder strb) {
            this.strb = strb;
        }

        public abstract State getNextState(char c);
    }

    class CodeState extends State {
        public CodeState(StringBuilder strb) {
            super(strb);
        }

        @Override
        public State getNextState(char c) {
            strb.append(c);
            if (c == '/') {
                return new SlashState(strb);
            }
            if (c == '"') {
                return new StringState(strb);
            }
            return this;
        }
    }

    class SlashState extends State {
        public SlashState(StringBuilder strb) {
            super(strb);
        }

        @Override
        public State getNextState(char c) {
            if (c == '/') {
                strb.append(c);
                return new SingleLineCommentState(strb);
            }
            if (c == '*') {
                strb.append(c);
                return new MultiLineCommentState(strb);
            }
            strb.append(encode(c));
            return new CodeState(strb);
        }
    }

    class SingleLineCommentState extends State {
        public SingleLineCommentState(StringBuilder strb) {
            super(strb);
        }

        @Override
        public State getNextState(char c) {
            if (c == '\n') {
                strb.append(c);
                return new CodeState(strb);
            }
            if (cleanComments) {
                strb.append(encode(c));
            } else {
                strb.append(c);
            }
            return this;
        }
    }

    class MultiLineCommentState extends State {
        public MultiLineCommentState(StringBuilder strb) {
            super(strb);
        }

        @Override
        public State getNextState(char c) {
            if (c == '*') {
                strb.append(c);
                return new StarState(strb);
            }
            if (cleanComments) {
                strb.append(encode(c));
            } else {
                strb.append(c);
            }
            return new MultiLineCommentState(strb);
        }
    }

    class StarState extends State {
        public StarState(StringBuilder strb) {
            super(strb);
        }

        @Override
        public State getNextState(char c) {
            if (c == '/') {
                strb.append(c);
                return new CodeState(strb);
            }
            if (c == '*') { // while *
                strb.append(c);
                return this;
            }
            strb.append(encode(c));
            return new MultiLineCommentState(strb);
        }
    }

    class StringState extends State {
        public StringState(StringBuilder strb) {
            super(strb);
        }

        @Override
        public State getNextState(char c) {
            if (c == '"') {
                strb.append(c);
                return new CodeState(strb);
            }
            if (cleanStrings) {
                strb.append(encode(c));
            } else {
                strb.append(c);
            }
            return this;
        }
    }

    final boolean cleanComments;
    final boolean cleanStrings;

    public CodeCleaner2(boolean cleanComments, boolean cleanStrings) {
        this.cleanComments = cleanComments;
        this.cleanStrings = cleanStrings;
    }

    public String cleanTheCode(String code) {
        var strb = new StringBuilder();
        State state = new CodeState(strb);
        for (char c : code.toCharArray()) {
            state = state.getNextState(c);
        }
        return strb.toString();
    }

    char encode(char c) {
        if (Character.isWhitespace(c)) {
            return c;
        }
        return '*';
    }
}