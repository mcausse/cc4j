package org.homs.cc4j.rules.visitors.rules;

public class NestingStatus {

    public final int nestedLevel;
    public final boolean nestingBonus;

    private NestingStatus(int nestedLevel, boolean nestingBonus) {
        this.nestedLevel = nestedLevel;
        this.nestingBonus = nestingBonus;
    }

    public static NestingStatus build() {
        return new NestingStatus(0, true);
    }

    public NestingStatus incNestedLevel() {
        return new NestingStatus(nestedLevel + 1, true);
    }

    public NestingStatus incNestedLevelButAvoidNextNestingBonus() {
        return new NestingStatus(nestedLevel, false);
    }
}

