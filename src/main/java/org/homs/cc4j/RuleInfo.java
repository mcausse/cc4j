package org.homs.cc4j;

public class RuleInfo {

    public final String categoryPrefix;
    public final int id;
    public final String description;

    public RuleInfo(String categoryPrefix, int id, String description) {
        this.categoryPrefix = categoryPrefix;
        this.id = id;
        this.description = description;
    }

    public String getFullDescription() {
        return this + " - " + description;
    }

    @Override
    public String toString() {
        return String.format("%s%02d", categoryPrefix, id);
    }
}