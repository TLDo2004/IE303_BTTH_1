//22520751

public class bai_2 {

    public static void main(String[] args) {
        int n = 100000000;
        double pi = piApproximate(n);
        System.out.println("Gia tri xap xi cua pi la: " + pi);
    }

    public static double piApproximate(int n) {
        double dx = 1.0 / n;
        double sum = 0.0;

        for (int i = 0; i < n; i++) {
            double x = i * dx;
            double y = Math.sqrt(1 - x * x);
            sum += y * dx;
        }
        return 4 * sum;
    }
}
