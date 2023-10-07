package org.homs.cc4j.rules.visitors.rules.re;

import org.homs.cc4j.RuleInfo;

public class TooComplexRegexp extends AbstractRegexpRule {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("re", 1, "too comples regexp");
    }

    @Override
    protected void checkString(String value) {
        // TODO
    }
}
