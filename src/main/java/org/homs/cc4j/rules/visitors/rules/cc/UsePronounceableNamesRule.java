package org.homs.cc4j.rules.visitors.rules.cc;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public class UsePronounceableNamesRule extends RuleTreeVisitor<Void> {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 1, "Avoid unpronounceable names.");
    }

    @Override
    public Integer visitClass(ClassTree node, Void p) {
        location.push(node.getSimpleName().toString());

        pronounceableAnalysis(node.getSimpleName().toString());
        var r = super.visitClass(node, p);

        location.pop();
        return r;
    }

    @Override
    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");

        pronounceableAnalysis(node.getName().toString());

        location.pop();
        return super.visitMethod(node, p);
    }

    @Override
    public Integer visitVariable(VariableTree node, Void unused) {
        pronounceableAnalysis(node.getName().toString());
        return super.visitVariable(node, unused);
    }

    void pronounceableAnalysis(String name) {
        int consonants = consonantAnalysis(name);
        generateIssueIfThreshold("use pronounceable names: '" + name +
                        "' scored with %s (>%s warning, >%s critical, >%s error)",
                consonants, 4, 5, 6);
    }

    boolean isVowel(char c) {
        return "aeiouAEIOUwyWY_".indexOf(c) >= 0;
    }

    public int consonantAnalysis(String name) {

        int maxRun = 0;
        int run = 0;
        char last = 'a';
        for (char c : name.toCharArray()) {
            if (isVowel(last) != isVowel(c) || Character.isLowerCase(last) != Character.isLowerCase(c)) {
                run = 1;
            } else {
                run++;
            }

            if (maxRun < run) {
                maxRun = run;
            }

            last = c;
        }
        return maxRun;
    }
}
