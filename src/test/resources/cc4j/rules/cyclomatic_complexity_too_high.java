public class Jou {

    void cyclomatic16(int a, int b, int c) {
        while (a > 0 && b > 0 && c > 0) {
            switch (b) {
                case 0:
                    System.out.println(0);
                    break;
                case 1:
                    System.out.println(1);
                    break;
                default:
                    System.out.println(a == 5 || b == 5 || c == 5 ? -1 : -2);
            }
        }
        if (a == b || a == 1 || b == 0 || a + b + c % 2 == 0) {
            return;
        }
        if (a == b) {
            if (b == c) {
                System.out.println("a");
            } else {
                System.out.println("b");
            }
        }
    }

    void cyclomatic15(int a, int b, int c, List<String> ks) {
        for (var k : ks) {
        }
        do {
        } while (a > 0 && b > 0 || c > 0);
        for (int i = 0; i < ks.size() && a > 0 ||b>0; i++) {
        }

        for (var k : ks) {
        }
        do {
        } while (a > 0 && b > 0 || c > 0);
        for (int i = 0; i < ks.size() && a > 0 ||b>0; i++) {
        }
    }
}