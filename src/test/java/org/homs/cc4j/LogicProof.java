package org.homs.cc4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicProof {

    boolean a(int a, int b, int c) {
        return a > 0 && b > 1 || (((a + b) % 3) == 0) && !(c < 0 || (a - c) > 0);
    }

    boolean b(int a, int b, int c) {
        return !(a > 0 && b > 1 || (((a + b) % 3) == 0) && !(c < 0 || (a - c) > 0));
    }

    // XXX invertir operadors, pero mantenint la prioritat dels originals: usar ( i )
    boolean c(int a, int b, int c) {
        return (!(a > 0) || !(b > 1)) && (!(((a + b) % 3) == 0) || !!(c < 0 || (a - c) > 0));
    }

    boolean d(int a, int b, int c) {
        return (a <= 0 || b <= 1) && ((a + b) % 3 != 0 || c < 0 || (a - c) > 0);
    }

    @Test
    void name() {
        for (int a = -10; a < 10; a++) {
            for (int b = -10; b < 10; b++) {
                for (int c = -10; c < 10; c++) {
                    assertThat(a(a, b, c)).as("%s %s %s", a, b, c).isEqualTo(!b(a, b, c));
                    assertThat(a(a, b, c)).as("%s %s %s", a, b, c).isEqualTo(!c(a, b, c));
                }
            }
        }
    }
}
