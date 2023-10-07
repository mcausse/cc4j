import org.junit.jupiter.api.Disabled;

public class Jou {
    //    TODO jou
    //    FIXME juas
    final String jou = "TODO jou2 FIXME juas2";

    @Ignore
    @Disabled
    @Deprecated
    public void ignoredTest() {
    }

    @Ignore("justification")
    @Disabled("justification")
    @org.junit.Ignore("justification")
    @org.junit.jupiter.api.Disabled("justification")
    public void ignoredTestWithJustification() {
    }
}