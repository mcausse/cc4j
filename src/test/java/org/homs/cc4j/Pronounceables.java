package org.homs.cc4j;

import org.junit.jupiter.api.Test;

public class Pronounceables {


    @Test
    void name() {

        String[] ns = {"GoodName", "nPLHDirectory", "yyyymmdd"};

        for (var n : ns) {
            System.out.println(n + " => " + analisis(n));
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
