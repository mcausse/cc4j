package org.homs.cc4j.issue;

public class Thresholds {

    public final int warningThr;
    public final int criticalThr;
    public final int errorThr;

    public Thresholds(int warningThr, int criticalThr, int errorThr) {
        this.warningThr = warningThr;
        this.criticalThr = criticalThr;
        this.errorThr = errorThr;
    }
}

