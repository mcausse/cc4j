package org.homs.cc4j.visitors.rules;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CognitiveComplexityTooHighRuleTest {

    @Test
    void computeExpression() {
        String exp="!isTLSEnable && !isMutualEnable && (crtPath == null || crtPath.isEmpty())\n" +
                "                && (keyPath == null || keyPath.isEmpty()) && (caCrtPath == null || caCrtPath.isEmpty())\n" +
                "                && (clientAuthHosts == null || clientAuthHosts.isEmpty()) && port == 0" +
                "||" +
                "!isTLSEnable && !isMutualEnable && (crtPath == null || crtPath.isEmpty())\\n\" +\n" +
                "                \"                && (keyPath == null || keyPath.isEmpty()) && (caCrtPath == null || caCrtPath.isEmpty())\\n\" +\n" +
                "                \"                && (clientAuthHosts == null || clientAuthHosts.isEmpty()) && port == 0";
        var sut = new CognitiveComplexityTooHighRule();

        // Act
        var r = sut.computeExpression(exp);

        assertThat(r).isEqualTo(19);
    }
}