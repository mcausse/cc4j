package org.homs.cc4j;

import org.homs.cc4j.visitors.rules.UsePronounceableNamesRule;
import org.junit.jupiter.api.Test;

public class Pronounceables {

    @Test
    void name() {

        String[] ns = {"GoodName", "nPLHDirectory", "yyyymmdd"};

        var sut = new UsePronounceableNamesRule();

        for (var n : ns) {
            System.out.println(n + " => " + sut.consonantAnalysis(n));
        }
    }

    double analisis(String n) {
        String vowelss = "aeiouAEIOUwWyY";
        int vowels = 0;
        int nonvowels = 0;
        for (char c : n.toCharArray()) {
            if (vowelss.indexOf(c) >= 0) {
                vowels++;
            } else {
                nonvowels++;
            }
        }
        return ((double) nonvowels) / vowels;
    }
}
