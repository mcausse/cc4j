public class Jou {

    int a, b, c, d, e, f, g, h;

    void jou() {
        if (a <= b && b >= c || c < b && !(b > a || e == f) || f == g || g != h) {
            return a == b && b == c && c == d && d == e;
        }
        if (a > b && b <= c) {
            if (a > b && b <= c) {
                return a == b && b == c && c == d && d == e;
            } else {
                return a == b && b == c && c == d && d == e;
            }
        }
    }
}